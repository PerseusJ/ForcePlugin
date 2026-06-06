package org.perseus.forcePlugin.abilities.light;

import org.perseus.forcePlugin.versioning.VersionUtil;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceValor extends AbstractAbility {

    public ForceValor(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "FORCE_VALOR";
    }

    @Override
    public String getName() {
        return "Force Valor";
    }

    @Override
    public String getDescription() {
        return "Inspire yourself and nearby allies with the Light Side.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.LIGHT;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 50.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 45.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int radius = cfgInt(level, "radius", 15);
        int durationTicks = cfgInt(level, "duration-seconds", 10) * 20;
        int strAmp = cfgInt(level, "strength-amplifier", 1) - 1;
        int speedAmp = cfgInt(level, "speed-amplifier", 1) - 1;

        PotionEffect strength = new PotionEffect(VersionUtil.STRENGTH, durationTicks, Math.max(0, strAmp));
        PotionEffect resistance = new PotionEffect(VersionUtil.RESISTANCE, durationTicks, 0);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, durationTicks, Math.max(0, speedAmp));

        // Apply to caster
        player.addPotionEffect(strength);
        player.addPotionEffect(resistance);
        player.addPotionEffect(speed);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.5f);

        // Radial burst from caster
        Location center = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.CLOUD, center, 50, 0.5, 0.5, 0.5, 0.2);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player) {
                Player ally = (Player) entity;
                ally.addPotionEffect(strength);
                ally.addPotionEffect(resistance);
                ally.addPotionEffect(speed);

                ally.getWorld().spawnParticle(VersionUtil.TOTEM_OF_UNDYING, ally.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
                ally.playSound(ally.getLocation(), Sound.ITEM_TOTEM_USE, 0.5f, 1.8f);
            }
        }
    }
}

