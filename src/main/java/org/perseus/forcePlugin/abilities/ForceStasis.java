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

public class ForceStasis implements Ability {

    private final AbilityConfigManager configManager;

    public ForceStasis(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_STASIS"; }

    @Override
    public String getName() { return "Force Stasis"; }

    @Override
    public String getDescription() { return "Freezes a single target in place."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() {
        return configManager.getDoubleValue(getID(), "energy-cost", 20.0);
    }

    @Override
    public double getCooldown() {
        return configManager.getDoubleValue(getID(), "cooldown", 15.0);
    }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 15);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        int duration = configManager.getIntValue(getID(), "duration-seconds", 3) * 20;
        // A very high level of Slowness effectively freezes the entity.
        int slownessAmplifier = 200;

        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slownessAmplifier, false, false));
        player.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.8f);
        player.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5);
    }
}