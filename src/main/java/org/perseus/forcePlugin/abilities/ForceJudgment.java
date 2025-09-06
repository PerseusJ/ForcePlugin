package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.*;

public class ForceJudgment implements Ability {

    private final AbilityConfigManager configManager;

    public ForceJudgment(AbilityConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getID() { return "FORCE_JUDGMENT"; }

    @Override
    public String getName() { return "Force Judgment"; }

    @Override
    public String getDescription() { return "Hurl a blast of pure energy at a target."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

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

        if (target instanceof Player) {
            ForceUser targetUser = ForcePlugin.getPlugin(ForcePlugin.class).getForceUserManager().getForceUser((Player) target);
            if (targetUser != null && targetUser.getSide() == ForceSide.DARK) {
                damage *= configManager.getDoubleValue(getID(), "bonus-damage-multiplier-dark-side", 1.5);
            }
        } else if (target.getType() == EntityType.ZOMBIE || target.getType() == EntityType.SKELETON || target.getType() == EntityType.WITHER) {
            damage *= configManager.getDoubleValue(getID(), "bonus-damage-multiplier-undead", 1.5);
        }

        ParticleUtil.drawZigZagBeam(player, target, Particle.END_ROD, 4.0, 0.3, null);
        target.damage(damage, player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 1.5f);
    }
}