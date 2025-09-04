package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceRepulse implements Ability {

    private final AbilityConfigManager configManager;

    public ForceRepulse(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_REPULSE"; }

    @Override
    public String getName() { return "Force Repulse"; }

    @Override
    public String getDescription() { return "A 360-degree blast that knocks all nearby enemies away."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 35.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 18.0);
    }

    @Override
    public void execute(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.2f);
        player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 1);

        int radius = configManager.getIntValue(getID(), "radius", 6);
        double strength = configManager.getDoubleValue(getID(), "strength", 2.5);
        double upwardForce = configManager.getDoubleValue(getID(), "upward-force", 0.8);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            direction.setY(upwardForce).multiply(strength);
            entity.setVelocity(direction);
        }
    }
}