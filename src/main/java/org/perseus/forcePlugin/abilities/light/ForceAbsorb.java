package org.perseus.forcePlugin.abilities.light;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.listeners.UltimateAbilityListener;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceAbsorb implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceAbsorb(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_ABSORB"; }
    @Override public String getName() { return "Force Absorb"; }
    @Override public String getDescription() { return "Absorb incoming damage and unleash it as a shockwave."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 50.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 60.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double duration = configManager.getDoubleValue(getID(), level, "duration-seconds", 3.0);
        int durationTicks = (int) (duration * 20);

        UltimateAbilityListener.addAbsorbingPlayer(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.5f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= durationTicks || !player.isOnline()) {
                    this.cancel();
                    double absorbedDamage = UltimateAbilityListener.removeAbsorbingPlayer(player.getUniqueId());
                    double knockbackBonus = configManager.getDoubleValue(getID(), level, "knockback-per-heart", 0.2) * (absorbedDamage / 2);
                    double finalKnockback = 2.5 + knockbackBonus;

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
                    plugin.getVersionAdapter().playExplosionEmitter(player.getLocation());

                    for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                        if (entity instanceof Player && entity.equals(player)) continue;
                        Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        direction.setY(0.8).multiply(finalKnockback);
                        entity.setVelocity(direction);
                    }
                    return;
                }
                Location center = player.getLocation();
                for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 8) {
                    double x = center.getX() + 1.5 * Math.cos(angle + ticks);
                    double z = center.getZ() + 1.5 * Math.sin(angle + ticks);
                    player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), x, center.getY() + 1, z), 1, 0, 0, 0, 0);
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}