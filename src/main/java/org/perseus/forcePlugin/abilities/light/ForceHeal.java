package org.perseus.forcePlugin.abilities.light;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

import java.util.Arrays;
import java.util.List;

public class ForceHeal implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    private static final List<PotionEffectType> DEBUFFS = Arrays.asList(
            PotionEffectType.SLOW, PotionEffectType.WEAKNESS, PotionEffectType.POISON,
            PotionEffectType.WITHER, PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION,
            PotionEffectType.HUNGER, PotionEffectType.LEVITATION
    );
    public ForceHeal(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_HEAL"; }
    @Override public String getName() { return "Force Heal"; }
    @Override public String getDescription() { return "Restores health and cleanses negative effects."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 12.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double healAmount = configManager.getDoubleValue(getID(), level, "heal-amount-hearts", 2.0) * 2;
        boolean hasDebuff = player.getActivePotionEffects().stream().anyMatch(effect -> DEBUFFS.contains(effect.getType()));
        if (player.getHealth() >= player.getMaxHealth() && !hasDebuff) return;

        double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
        player.setHealth(newHealth);
        for (PotionEffectType debuffType : DEBUFFS) {
            if (player.hasPotionEffect(debuffType)) {
                player.removePotionEffect(debuffType);
            }
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);

        new BukkitRunnable() {
            double radius = 0.2;
            double yOffset = 0;
            @Override
            public void run() {
                if (yOffset > 2.0) { this.cancel(); return; }
                Location center = player.getLocation().add(0, yOffset, 0);
                for (int i = 0; i < 5; i++) {
                    double angle = Math.random() * 2 * Math.PI;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, new Location(player.getWorld(), x, center.getY(), z), 1, 0, 0, 0, 0);
                }
                radius += 0.1;
                yOffset += 0.1;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}