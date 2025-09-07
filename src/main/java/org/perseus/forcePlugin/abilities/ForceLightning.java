package org.perseus.forcePlugin.abilities;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.*;

public class ForceLightning implements Ability {
    private final AbilityConfigManager configManager;
    public ForceLightning(AbilityConfigManager configManager) { this.configManager = configManager; }
    @Override public String getID() { return "FORCE_LIGHTNING"; }
    @Override public String getName() { return "Force Lightning"; }
    @Override public String getDescription() { return "Unleash a dash of lightning at your target."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 25.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 8.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int range = 20;
        double damage = configManager.getDoubleValue(getID(), level, "damage-hearts", 3.0) * 2;

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(100, 150, 255), 1.0F);
        ParticleUtil.drawZigZagBeam(player, target, Particle.DUST, 4.0, 0.3, dustOptions);
        target.damage(damage, player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.8f);
    }
}