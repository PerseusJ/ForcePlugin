package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.ForceSide;

public class ForceBarrier implements Ability {

    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin; // We need the plugin instance to run a task

    public ForceBarrier(AbilityConfigManager configManager, ForcePlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public String getID() { return "FORCE_BARRIER"; }

    @Override
    public String getName() { return "Force Barrier"; }

    @Override
    public String getDescription() { return "Grants temporary Absorption hearts."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 30.0); }

    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 25.0); }

    @Override
    public void execute(Player player) {
        int duration = configManager.getIntValue(getID(), "duration-seconds", 10) * 20;
        int amplifier = configManager.getIntValue(getID(), "shield-hearts", 2) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier));
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.2f);

        // --- NEW: Dynamic Particle Shield Effect ---
        new BukkitRunnable() {
            int ticks = 0;
            double radius = 1.0; // The radius of the particle ring
            double angle = 0;    // The starting angle for the particle

            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) {
                    this.cancel();
                    return;
                }

                // Calculate the position of the particle in a circle around the player
                Location playerLoc = player.getLocation();
                double x = playerLoc.getX() + radius * Math.cos(angle);
                double z = playerLoc.getZ() + radius * Math.sin(angle);
                Location particleLoc = new Location(player.getWorld(), x, playerLoc.getY() + 1, z); // Spawn at head level

                player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);

                // Update the angle to make the particle swirl
                angle += Math.PI / 8; // Increase for faster swirl
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run every tick for the duration
        // --- END NEW ---
    }
}