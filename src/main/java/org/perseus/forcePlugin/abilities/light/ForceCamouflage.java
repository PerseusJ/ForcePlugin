package org.perseus.forcePlugin.abilities.light;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.listeners.UltimateAbilityListener;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceCamouflage implements Ability {
    private final AbilityConfigManager configManager;
    public ForceCamouflage(AbilityConfigManager configManager) { this.configManager = configManager; }
    @Override public String getID() { return "FORCE_CAMOUFLAGE"; }
    @Override public String getName() { return "Force Camouflage"; }
    @Override public String getDescription() { return "Vanish from sight and empower your next attack."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 40.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 50.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 8) * 20;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1));
        UltimateAbilityListener.addCamouflagedPlayer(player.getUniqueId());

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.01);
    }
}