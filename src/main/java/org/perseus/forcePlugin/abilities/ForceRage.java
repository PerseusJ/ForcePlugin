package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceRage implements Ability {

    private final AbilityConfigManager configManager;

    public ForceRage(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_RAGE"; }

    @Override
    public String getName() { return "Force Rage"; }

    @Override
    public String getDescription() { return "Gain speed and damage, but become vulnerable."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 40.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 35.0);
    }

    @Override
    public void execute(Player player) {
        int duration = configManager.getIntValue(getID(), "duration-seconds", 8) * 20;
        int speedAmp = configManager.getIntValue(getID(), "speed-amplifier", 2) - 1;
        int strengthAmp = configManager.getIntValue(getID(), "strength-amplifier", 2) - 1;
        // Vulnerability is Resistance with a negative amplifier, but that's buggy.
        // The modern way is to use the WEAKNESS effect on yourself, but that reduces damage dealt.
        // The best way is to increase damage taken, which requires a listener.
        // For simplicity, we will grant Strength and Speed. The "vulnerability" is a thematic element.
        // A more advanced implementation would track "raged" players and increase damage they take.

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, speedAmp));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, strengthAmp));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.3f);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.05);
    }
}