package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceScream implements Ability {

    private final AbilityConfigManager configManager;

    public ForceScream(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_SCREAM"; }

    @Override
    public String getName() { return "Force Scream"; }

    @Override
    public String getDescription() { return "Weakens and slows all nearby enemies."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 25.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 20.0);
    }

    @Override
    public void execute(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 0.9f);
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 1);

        int radius = configManager.getIntValue(getID(), "radius", 8);
        int duration = configManager.getIntValue(getID(), "duration-seconds", 5) * 20;
        int weaknessAmp = configManager.getIntValue(getID(), "weakness-amplifier", 1) - 1;
        int slownessAmp = configManager.getIntValue(getID(), "slowness-amplifier", 2) - 1;

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && !entity.equals(player)) {
                LivingEntity target = (LivingEntity) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, weaknessAmp));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slownessAmp));
            }
        }
    }
}