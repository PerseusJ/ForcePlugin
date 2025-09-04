package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceBarrier implements Ability {

    private final AbilityConfigManager configManager;

    public ForceBarrier(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_BARRIER"; }

    @Override
    public String getName() { return "Force Barrier"; }

    @Override
    public String getDescription() { return "Grants temporary Absorption hearts."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 30.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 25.0);
    }

    @Override
    public void execute(Player player) {
        int duration = configManager.getIntValue(getID(), "duration-seconds", 10) * 20;
        // Absorption amplifier 0 = 2 hearts, 1 = 4 hearts, etc.
        int amplifier = configManager.getIntValue(getID(), "shield-hearts", 2) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier));
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.2f);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 40, 0.7, 0.7, 0.7, 0);
    }
}