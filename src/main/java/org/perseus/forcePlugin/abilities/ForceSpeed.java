package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceSpeed implements Ability {

    private final AbilityConfigManager configManager;

    public ForceSpeed(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_SPEED"; }

    @Override
    public String getName() { return "Force Speed"; }

    @Override
    public String getDescription() { return "Grants you a burst of speed."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

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
        int duration = configManager.getIntValue(getID(), "duration-seconds", 8) * 20;
        int amplifier = configManager.getIntValue(getID(), "amplifier", 2) - 1; // Potion levels are 0-indexed

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1, 0), 20, 0.4, 0.4, 0.4, 0.0);
    }
}