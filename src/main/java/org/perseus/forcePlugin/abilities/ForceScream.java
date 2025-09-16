package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceScream implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceScream(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_SCREAM"; }
    @Override public String getName() { return "Force Scream"; }
    @Override public String getDescription() { return "Weakens and slows all nearby enemies."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 25.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 20.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int radius = configManager.getIntValue(getID(), level, "radius", 8);
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 5) * 20;
        int weaknessAmp = 0;
        int slownessAmp = 1;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 0.9f);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && !entity.equals(player)) {
                LivingEntity target = (LivingEntity) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, weaknessAmp));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slownessAmp));
            }
        }

        new BukkitRunnable() {
            double currentRadius = 1.0;
            @Override
            public void run() {
                if (currentRadius > radius) { this.cancel(); return; }
                Location center = player.getLocation();
                for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 16) {
                    double x = center.getX() + currentRadius * Math.cos(angle);
                    double z = center.getZ() + currentRadius * Math.sin(angle);
                    player.getWorld().spawnParticle(Particle.SONIC_BOOM, new Location(player.getWorld(), x, center.getY() + 1, z), 1, 0, 0, 0, 0);
                }
                currentRadius += 0.5;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}