package org.perseus.forcePlugin;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Manages the ambient particle effects for players who have their powers active.
 */
public class AmbientEffectsManager {

    private final ForcePlugin plugin;

    public AmbientEffectsManager(ForcePlugin plugin) {
        this.plugin = plugin;
        startEffectTask();
    }

    /**
     * Starts a single, repeating task that handles effects for all online players.
     */
    private void startEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Loop through all online players on the server.
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);

                    // Check if the player's powers are active and they have chosen a side.
                    if (forceUser != null && forceUser.arePowersActive() && forceUser.getSide() != ForceSide.NONE) {
                        // If they are a valid Force user, spawn a particle.
                        spawnAmbientParticle(player, forceUser.getSide());
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 10L); // Run every 10 ticks (twice per second). Asynchronous for performance.
    }

    /**
     * Spawns a single, slow-moving particle near the player's feet.
     * @param player The player to spawn the particle for.
     * @param side The player's chosen Force side.
     */
    private void spawnAmbientParticle(Player player, ForceSide side) {
        Location location = player.getLocation().add(
                (Math.random() - 0.5) * 1.5, // Random X offset
                0.2,                         // Slightly above the floor
                (Math.random() - 0.5) * 1.5  // Random Z offset
        );

        if (side == ForceSide.LIGHT) {
            // A faint, white/blue end rod particle for the Light Side.
            player.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0, 0, 0, 0);
        } else { // Dark Side
            // A faint, red dust particle for the Dark Side.
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(150, 0, 0), 0.7F);
            player.getWorld().spawnParticle(Particle.DUST, location, 1, 0, 0, 0, 0, dustOptions);
        }
    }
}