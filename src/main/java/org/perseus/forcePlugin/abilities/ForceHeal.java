package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceHeal implements Ability {

    private final AbilityConfigManager configManager;

    public ForceHeal(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_HEAL"; }

    @Override
    public String getName() { return "Force Heal"; }

    @Override
    public String getDescription() { return "Instantly regenerates health."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 30.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 12.0);
    }

    @Override
    public void execute(Player player) {
        if (player.getHealth() >= player.getMaxHealth()) return;

        double healAmount = configManager.getDoubleValue(getID(), "heal-amount-hearts", 2.0) * 2;
        double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
        player.setHealth(newHealth);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5);
    }
}