package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForcePush implements Ability {

    private final AbilityConfigManager configManager;

    public ForcePush(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_PUSH"; }

    @Override
    public String getName() { return "Force Push"; }

    @Override
    public String getDescription() { return "Unleashes a wave of energy, knocking back entities in front of you."; }

    @Override
    public ForceSide getSide() {
        // --- CORRECTED: Mark as Universal ---
        return ForceSide.NONE;
    }

    // ... (rest of the class is unchanged)
    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 15.0); }
    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 4.0); }
    @Override
    public void execute(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.7f, 1.5f);
        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation().add(0, 1, 0).add(player.getLocation().getDirection()), 5, 0.5, 0.5, 0.5, 0.0);
        double strength = configManager.getDoubleValue(getID(), "strength", 2.0);
        double upwardForce = configManager.getDoubleValue(getID(), "upward-force", 1.2);
        int range = configManager.getIntValue(getID(), "range", 10);
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            Vector playerDirection = player.getLocation().getDirection();
            Vector entityDirection = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            if (playerDirection.dot(entityDirection) > 0.7) {
                Vector knockback = player.getLocation().getDirection().multiply(strength).setY(upwardForce);
                entity.setVelocity(knockback);
            }
        }
    }
}