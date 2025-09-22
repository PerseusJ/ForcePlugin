package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.listeners.UltimateAbilityListener;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class MarkOfTheHunt implements Ability {
    private final AbilityConfigManager configManager;
    public MarkOfTheHunt(AbilityConfigManager configManager) { this.configManager = configManager; }
    @Override public String getID() { return "MARK_OF_THE_HUNT"; }
    @Override public String getName() { return "Mark of the Hunt"; }
    @Override public String getDescription() { return "Mark a target for death, revealing them and their weaknesses."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 90.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 60) * 20;
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 30,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0));
        UltimateAbilityListener.addMarkedEntity(target.getUniqueId());

        player.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 0.8f);
        target.getWorld().spawnParticle(Particle.CRIMSON_SPORE, target.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5);
        player.sendMessage(ChatColor.RED + "You have marked " + target.getName() + " for the hunt.");
    }
}