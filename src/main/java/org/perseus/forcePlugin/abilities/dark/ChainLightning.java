package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;
import org.perseus.forcePlugin.managers.ParticleUtil;

import java.util.ArrayList;
import java.util.List;

public class ChainLightning implements Ability {
    private final AbilityConfigManager configManager;
    public ChainLightning(AbilityConfigManager configManager) { this.configManager = configManager; }
    @Override public String getID() { return "CHAIN_LIGHTNING"; }
    @Override public String getName() { return "Chain Lightning"; }
    @Override public String getDescription() { return "Unleash lightning that arcs between multiple foes."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 55.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 45.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double initialDamage = configManager.getDoubleValue(getID(), level, "initial-damage-hearts", 4.0) * 2;
        int maxJumps = configManager.getIntValue(getID(), level, "max-jumps", 3);
        double falloff = configManager.getDoubleValue(getID(), level, "damage-falloff-per-jump", 0.75);

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 20,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity currentTarget = (LivingEntity) rayTrace.getHitEntity();
        List<Entity> hitTargets = new ArrayList<>();
        hitTargets.add(currentTarget);

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(100, 150, 255), 1.0F);
        ParticleUtil.drawZigZagBeam(player, currentTarget, Particle.DUST, 4.0, 0.3, dustOptions);
        currentTarget.damage(initialDamage, player);
        player.getWorld().playSound(currentTarget.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.8f);

        double currentDamage = initialDamage;
        for (int i = 0; i < maxJumps; i++) {
            LivingEntity lastTarget = currentTarget;
            currentTarget = findNextTarget(lastTarget, hitTargets);
            if (currentTarget == null) break;

            hitTargets.add(currentTarget);
            currentDamage *= falloff;

            // --- THE FIX: Use the new overloaded method that takes two Locations ---
            ParticleUtil.drawZigZagBeam(lastTarget.getEyeLocation(), currentTarget.getEyeLocation(), Particle.DUST, 4.0, 0.3, dustOptions);
            currentTarget.damage(currentDamage, player);
            player.getWorld().playSound(currentTarget.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.4f, 1.9f);
        }
    }

    private LivingEntity findNextTarget(LivingEntity from, List<Entity> alreadyHit) {
        LivingEntity bestTarget = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : from.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity && !alreadyHit.contains(entity)) {
                double distance = from.getLocation().distanceSquared(entity.getLocation());
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestTarget = (LivingEntity) entity;
                }
            }
        }
        return bestTarget;
    }
}