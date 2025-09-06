package org.perseus.forcePlugin.abilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;
import org.perseus.forcePlugin.TelekinesisManager;

public class Telekinesis implements Ability {

    private final AbilityConfigManager configManager;
    private final TelekinesisManager telekinesisManager;

    public Telekinesis(AbilityConfigManager configManager, TelekinesisManager telekinesisManager) {
        this.configManager = configManager;
        this.telekinesisManager = telekinesisManager;
    }

    @Override
    public String getID() {
        return "TELEKINESIS";
    }

    @Override
    public String getName() {
        return "Telekinesis";
    }

    @Override
    public String getDescription() {
        return "Lift and control entities with your mind.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.NONE; // Universal
    }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "initial-energy-cost", 10.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 20.0);
    }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 20);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace != null && rayTrace.getHitEntity() != null) {
            LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
            telekinesisManager.startLifting(player, target);
        }
    }
}