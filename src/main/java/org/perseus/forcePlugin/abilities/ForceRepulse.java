package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceRepulse implements Ability {
    private final AbilityConfigManager configManager;
    public ForceRepulse(AbilityConfigManager configManager) { this.configManager = configManager; }
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

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.2f);
        player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 1);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            direction.setY(upwardForce).multiply(strength);
            entity.setVelocity(direction);
        }
    }
}