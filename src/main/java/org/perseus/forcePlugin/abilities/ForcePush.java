package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForcePush implements Ability {
    private final AbilityConfigManager configManager;
    public ForcePush(AbilityConfigManager configManager) { this.configManager = configManager; }
    @Override public String getID() { return "FORCE_PUSH"; }
    @Override public String getName() { return "Force Push"; }
    @Override public String getDescription() { return "Knocks back and disorients entities in front of you."; }
    @Override public ForceSide getSide() { return ForceSide.NONE; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 15.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 4.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double strength = configManager.getDoubleValue(getID(), level, "strength", 2.0);
        double upwardForce = configManager.getDoubleValue(getID(), level, "upward-force", 1.2);
        int range = 10;
        int nauseaDuration = configManager.getIntValue(getID(), level, "nausea-duration-seconds", 3) * 20;
        int nauseaAmplifier = configManager.getIntValue(getID(), level, "nausea-amplifier", 1) - 1;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.7f, 1.5f);
        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation().add(0, 1, 0).add(player.getLocation().getDirection()), 5, 0.5, 0.5, 0.5, 0.0);

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            Vector playerDirection = player.getLocation().getDirection();
            Vector entityDirection = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            if (playerDirection.dot(entityDirection) > 0.7) {
                Vector knockback = player.getLocation().getDirection().multiply(strength).setY(upwardForce);
                entity.setVelocity(knockback);
                if (entity instanceof LivingEntity && nauseaDuration > 0) {
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, nauseaDuration, nauseaAmplifier));
                }
            }
        }
    }
}