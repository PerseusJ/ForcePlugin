package org.perseus.forcePlugin.abilities.light;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceRepulse implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceRepulse(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_REPULSE"; }
    @Override public String getName() { return "Force Repulse"; }
    @Override public String getDescription() { return "A 360-degree blast that knocks all nearby enemies away."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 35.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 18.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int radius = configManager.getIntValue(getID(), level, "radius", 6);
        double strength = configManager.getDoubleValue(getID(), level, "strength", 2.5);
        double upwardForce = 0.8;

        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.2f);
        plugin.getVersionAdapter().playExplosionEmitter(player.getLocation());

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player && entity.equals(player)) continue;
            Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            direction.setY(upwardForce).multiply(strength);
            entity.setVelocity(direction);
        }
    }
}