package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.listeners.UltimateAbilityListener;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class UnstoppableVengeance implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public UnstoppableVengeance(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "UNSTOPPABLE_VENGEANCE"; }
    @Override public String getName() { return "Unstoppable Vengeance"; }
    @Override public String getDescription() { return "Become an unstoppable juggernaut fueled by pain."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 50.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 60.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 8) * 20;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 1)); // DAMAGE_RESISTANCE is the correct name for 1.16
        UltimateAbilityListener.addVengefulPlayer(player.getUniqueId());

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.2f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) {
                    UltimateAbilityListener.removeVengefulPlayer(player.getUniqueId());
                    this.cancel();
                    return;
                }
                Location center = player.getLocation();
                player.getWorld().spawnParticle(Particle.SMOKE_LARGE, center.clone().add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0);
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.MAROON, 1.5F);
                player.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(Math.random() - 0.5, Math.random() * 2, Math.random() - 0.5), 1, 0, 0, 0, 0, dustOptions);
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}