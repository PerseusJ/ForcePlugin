package org.perseus.forcePlugin.abilities;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForceSide;
import org.perseus.forcePlugin.ParticleUtil;

public class ForceLightning implements Ability {

    private final AbilityConfigManager configManager;

    public ForceLightning(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_LIGHTNING"; }

    @Override
    public String getName() { return "Force Lightning"; }

    @Override
    public String getDescription() { return "Unleash a dash of lightning at your target."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 25.0); }

    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 8.0); }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 20);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
        double damage = configManager.getDoubleValue(getID(), "damage-hearts", 3.0) * 2;

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(100, 150, 255), 1.0F);
        ParticleUtil.drawZigZagBeam(player, target, Particle.DUST, 4.0, 0.3, dustOptions);
        target.damage(damage, player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.8f);
    }
}