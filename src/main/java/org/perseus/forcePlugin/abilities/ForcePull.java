package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.*;

public class ForcePull implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForcePull(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_PULL"; }
    @Override public String getName() { return "Force Pull"; }
    @Override public String getDescription() { return "Yanks a single target towards you."; }
    @Override public ForceSide getSide() { return ForceSide.NONE; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 15.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 10.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int range = 20;
        double strength = configManager.getDoubleValue(getID(), level, "strength", 2.5);

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        new BukkitRunnable() {
            final Location start = target.getEyeLocation();
            final Location end = player.getEyeLocation();
            final Vector direction = end.toVector().subtract(start.toVector()).normalize();
            final double distance = start.distance(end);
            double traveled = 0;
            @Override
            public void run() {
                if (traveled >= distance) {
                    this.cancel();
                    Vector pullDirection = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
                    target.setVelocity(pullDirection.multiply(strength));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.0f, 0.8f);
                    return;
                }
                Location point = start.clone().add(direction.clone().multiply(traveled));
                player.getWorld().spawnParticle(Particle.ENCHANTED_HIT, point, 1, 0, 0, 0, 0);
                traveled += 1.0;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}