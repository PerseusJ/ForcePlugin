package org.perseus.forcePlugin.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceDrain implements Ability {

    private final AbilityConfigManager configManager;

    public ForceDrain(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_DRAIN"; }

    @Override
    public String getName() { return "Force Drain"; }

    @Override
    public String getDescription() { return "Steal health from a target to heal yourself."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 40.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 25.0);
    }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 15);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
        EntityType type = target.getType();

        if (type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.WITHER || type == EntityType.STRAY) {
            player.sendMessage(ChatColor.RED + "You cannot drain life from the undead.");
            return;
        }

        double drainAmount = configManager.getDoubleValue(getID(), "drain-amount-hearts", 2.0) * 2;
        target.damage(drainAmount, player);
        double newHealth = Math.min(player.getHealth() + drainAmount, player.getMaxHealth());
        player.setHealth(newHealth);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5);
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5);
    }
}