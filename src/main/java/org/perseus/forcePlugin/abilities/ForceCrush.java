package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;

public class ForceCrush implements Ability {

    private final AbilityConfigManager configManager;

    public ForceCrush(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_CRUSH"; }

    @Override
    public String getName() { return "Force Crush"; }

    @Override
    public String getDescription() { return "Deals high damage and slows a single target."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 35.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 15.0);
    }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 20);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        double damage = configManager.getDoubleValue(getID(), "damage-hearts", 4.0) * 2;
        int duration = configManager.getIntValue(getID(), "slowness-duration-seconds", 3) * 20;
        int slownessAmp = configManager.getIntValue(getID(), "slowness-amplifier", 3) - 1;

        target.damage(damage, player);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slownessAmp));

        player.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.8f, 0.8f);
        player.getWorld().spawnParticle(Particle.SQUID_INK, target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5);
    }
}