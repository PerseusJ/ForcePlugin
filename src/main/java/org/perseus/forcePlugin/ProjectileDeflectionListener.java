package org.perseus.forcePlugin;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProjectileDeflectionListener implements Listener {

    private static final Set<UUID> deflectingPlayers = new HashSet<>();

    public static void addDeflectingPlayer(UUID uuid) {
        deflectingPlayers.add(uuid);
    }

    public static void removeDeflectingPlayer(UUID uuid) {
        deflectingPlayers.remove(uuid);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Player)) return;

        Player player = (Player) event.getHitEntity();
        if (!deflectingPlayers.contains(player.getUniqueId())) return;

        Projectile projectile = event.getEntity();

        // Check if the projectile is coming from the front.
        Vector playerDirection = player.getLocation().getDirection();
        Vector projectileDirection = projectile.getVelocity().clone().normalize();
        // A negative dot product means the vectors are pointing towards each other.
        if (playerDirection.dot(projectileDirection) < -0.5) {
            event.setCancelled(true); // Stop the projectile from hitting the player.
            projectile.remove(); // Remove the original projectile.

            // Create a new projectile of the same type and fire it back.
            Projectile newProjectile = player.launchProjectile(projectile.getClass());
            newProjectile.setShooter(player);
            // Reflect it back along the player's line of sight with increased speed.
            newProjectile.setVelocity(player.getLocation().getDirection().multiply(2.5));
        }
    }
}