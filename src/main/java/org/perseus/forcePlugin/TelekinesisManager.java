package org.perseus.forcePlugin;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.abilities.Ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TelekinesisManager {

    private final ForcePlugin plugin;
    private final Map<UUID, LivingEntity> liftingTargets = new HashMap<>();
    private final Map<UUID, BukkitTask> liftingTasks = new HashMap<>();

    public TelekinesisManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isLifting(Player player) {
        return liftingTargets.containsKey(player.getUniqueId());
    }

    public LivingEntity getLiftedTarget(Player player) {
        return liftingTargets.get(player.getUniqueId());
    }

    public void startLifting(Player caster, LivingEntity target) {
        if (isLifting(caster)) return;

        liftingTargets.put(caster.getUniqueId(), target);
        target.addPotionEffect(PotionEffectType.LEVITATION.createEffect(Integer.MAX_VALUE, 0));

        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (!caster.isOnline() || target.isDead() || !target.isValid()) {
                    stopLifting(caster, false);
                    return;
                }

                Location destination = caster.getEyeLocation().add(caster.getLocation().getDirection().multiply(5));
                target.teleport(destination);

                if (ticks % 20 == 0) {
                    Ability ability = plugin.getAbilityManager().getAbility("TELEKINESIS");
                    ForceUser forceUser = plugin.getForceUserManager().getForceUser(caster);
                    if (ability == null || forceUser == null) {
                        stopLifting(caster, false);
                        return;
                    }
                    double energyCost = plugin.getAbilityConfigManager().getDoubleValue(ability.getID(), "energy-cost-per-second", 5.0);
                    if (forceUser.getCurrentForceEnergy() < energyCost) {
                        stopLifting(caster, true);
                    } else {
                        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - energyCost);
                        plugin.getForceBarManager().updateBar(caster);
                    }
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);

        liftingTasks.put(caster.getUniqueId(), task);
    }

    public void stopLifting(Player caster, boolean applyFallDamage) {
        BukkitTask task = liftingTasks.remove(caster.getUniqueId());
        if (task != null) {
            task.cancel();
        }
        LivingEntity target = liftingTargets.remove(caster.getUniqueId());
        if (target != null) {
            target.removePotionEffect(PotionEffectType.LEVITATION);
            if (!applyFallDamage) {
                target.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(60, 0));
            }
        }
    }

    public void launch(Player caster) {
        LivingEntity target = getLiftedTarget(caster);
        stopLifting(caster, true);

        if (target != null) {
            Ability ability = plugin.getAbilityManager().getAbility("TELEKINESIS");
            if (ability == null) return;
            double strength = plugin.getAbilityConfigManager().getDoubleValue(ability.getID(), "launch-strength", 3.0);
            Vector launchVector = caster.getLocation().getDirection().multiply(strength);
            target.setVelocity(launchVector);
        }
    }
}