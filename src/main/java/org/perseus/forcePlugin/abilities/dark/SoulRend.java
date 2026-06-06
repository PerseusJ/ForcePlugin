package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class SoulRend extends AbstractAbility {

    public SoulRend(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "SOUL_REND";
    }

    @Override
    public String getName() {
        return "Soul Rend";
    }

    @Override
    public String getDescription() {
        return "Tear at the target's spirit, greatly reducing their physical strength.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.DARK;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 25.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 15.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int durationTicks = cfgInt(level, "duration-seconds", 8) * 20;
        int weaknessAmp = cfgInt(level, "weakness-amplifier", 1) - 1;

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 20,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, durationTicks, Math.max(0, weaknessAmp)));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VEX_CHARGE, 1.0f, 0.5f);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 0.8f);

        // Visual: Grey soul-like particles spiral out of the target
        new BukkitRunnable() {
            double t = 0;
            @Override
            public void run() {
                if (target.isDead() || !target.isValid() || t > Math.PI * 4) {
                    this.cancel();
                    return;
                }
                t += Math.PI / 8;
                double x = Math.cos(t) * 1.5;
                double y = t * 0.2;
                double z = Math.sin(t) * 1.5;

                Location loc = target.getLocation().add(x, y, z);
                target.getWorld().spawnParticle(Particle.SOUL, loc, 3, 0.1, 0.1, 0.1, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
