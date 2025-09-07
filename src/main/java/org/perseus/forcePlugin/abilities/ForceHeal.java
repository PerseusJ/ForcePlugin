package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.*;

public class ForceHeal implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceHeal(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_HEAL"; }
    @Override public String getName() { return "Force Heal"; }
    @Override public String getDescription() { return "Instantly regenerates health."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 12.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        if (player.getHealth() >= player.getMaxHealth()) return;
        int level = forceUser.getAbilityLevel(getID());
        double healAmount = configManager.getDoubleValue(getID(), level, "heal-amount-hearts", 2.0) * 2;
        double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
        player.setHealth(newHealth);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);

        new BukkitRunnable() {
            double radius = 0.2;
            double yOffset = 0;
            @Override
            public void run() {
                if (yOffset > 2.0) { this.cancel(); return; }
                Location center = player.getLocation().add(0, yOffset, 0);
                for (int i = 0; i < 5; i++) {
                    double angle = Math.random() * 2 * Math.PI;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, new Location(player.getWorld(), x, center.getY(), z), 1, 0, 0, 0, 0);
                }
                radius += 0.1;
                yOffset += 0.1;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}