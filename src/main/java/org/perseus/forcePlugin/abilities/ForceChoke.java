package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.ForceSide;

public class ForceChoke implements Ability {

    private final ForcePlugin plugin;
    private final AbilityConfigManager configManager;

    public ForceChoke(ForcePlugin plugin, AbilityConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_CHOKE"; }

    @Override
    public String getName() { return "Force Choke"; }

    @Override
    public String getDescription() { return "Lifts and damages a single target."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 30.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 12.0);
    }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 15);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        int duration = configManager.getIntValue(getID(), "duration-seconds", 3) * 20;
        int levitationAmp = configManager.getIntValue(getID(), "levitation-amplifier", 1) - 1;
        double damagePerSecond = configManager.getDoubleValue(getID(), "damage-per-second-hearts", 1.0) * 2;

        target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, levitationAmp));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 0.8f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || target.isDead() || !target.isValid()) {
                    this.cancel();
                    return;
                }
                if (ticks % 20 == 0) {
                    target.damage(damagePerSecond, player);
                }
                Location particleLoc = target.getLocation().add(0, target.getHeight() * 0.8, 0);
                target.getWorld().spawnParticle(Particle.SMOKE, particleLoc, 2, 0.1, 0.1, 0.1, 0);
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}