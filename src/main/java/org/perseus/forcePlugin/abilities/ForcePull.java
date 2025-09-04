package org.perseus.forcePlugin.abilities;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForcePull implements Ability {

    private final AbilityConfigManager configManager;

    public ForcePull(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_PULL"; }

    @Override
    public String getName() { return "Force Pull"; }

    @Override
    public String getDescription() { return "Yanks a single target towards you."; }

    @Override
    public ForceSide getSide() {
        // --- CORRECTED: Mark as Universal ---
        return ForceSide.NONE;
    }

    // ... (rest of the class is unchanged)
    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 15.0); }
    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 10.0); }
    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 20);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
        double strength = configManager.getDoubleValue(getID(), "strength", 2.5);
        Vector direction = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
        target.setVelocity(direction.multiply(strength));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.0f, 0.8f);
    }
}