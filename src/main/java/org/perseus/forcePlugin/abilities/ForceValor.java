package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceValor implements Ability {

    private final AbilityConfigManager configManager;

    public ForceValor(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_VALOR"; }

    @Override
    public String getName() { return "Force Valor"; }

    @Override
    public String getDescription() { return "Grants Resistance and Strength."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 40.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 30.0);
    }

    @Override
    public void execute(Player player) {
        int duration = configManager.getIntValue(getID(), "duration-seconds", 10) * 20;
        int resistanceAmp = configManager.getIntValue(getID(), "resistance-amplifier", 1) - 1;
        int strengthAmp = configManager.getIntValue(getID(), "strength-amplifier", 1) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, duration, resistanceAmp));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, strengthAmp));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 1, 0), 25, 0.5, 0.5, 0.5);
    }
}