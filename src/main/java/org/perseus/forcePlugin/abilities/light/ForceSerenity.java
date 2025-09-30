package org.perseus.forcePlugin.abilities.light;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceSerenity implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceSerenity(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_SERENITY"; }
    @Override public String getName() { return "Force Serenity"; }
    @Override public String getDescription() { return "Create a healing sanctuary for you and your allies."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 60.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 75.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 5) * 20;
        double healPerSecond = configManager.getDoubleValue(getID(), level, "heal-per-second-hearts", 1.0) * 2;
        int radius = configManager.getIntValue(getID(), level, "radius", 7);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 200));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 2));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                if (ticks % 20 == 0) {
                    for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                        if (entity instanceof Player) {
                            Player ally = (Player) entity;
                            double newHealth = Math.min(ally.getHealth() + healPerSecond, ally.getMaxHealth());
                            ally.setHealth(newHealth);
                        }
                    }
                }
                Location center = player.getLocation();
                for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 16) {
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, new Location(player.getWorld(), x, center.getY() + 0.5, z), 1, 0, 0, 0, 0);
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}