package org.perseus.forcePlugin.listeners;

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

    public static boolean isDeflecting(UUID uuid) {
        return deflectingPlayers.contains(uuid);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Player)) return;

        Player player = (Player) event.getHitEntity();
        if (!deflectingPlayers.contains(player.getUniqueId())) return;

        Projectile projectile = event.getEntity();

        Vector playerDirection = player.getLocation().getDirection();
        Vector projectileDirection = projectile.getVelocity().clone().normalize();
        if (playerDirection.dot(projectileDirection) < -0.5) {
            event.setCancelled(true);
            projectile.remove();

            Projectile newProjectile = player.launchProjectile(projectile.getClass());
            newProjectile.setShooter(player);
            newProjectile.setVelocity(player.getLocation().getDirection().multiply(2.5));
        }
    }
}