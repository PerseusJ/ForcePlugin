package org.perseus.forcePlugin.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {

    public static void drawParticleBeam(Location start, Location end, Particle particle, double density, Object data) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            Location point = start.clone().add(direction.clone().multiply(i));
            start.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }

    public static void drawZigZagBeam(Location start, Location end, Particle particle, double density, double amplitude, Object data) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            Location point = start.clone().add(direction.clone().multiply(i));
            double offset = Math.sin(i * 2) * amplitude;
            point.add(perpendicular.clone().multiply(offset));
            start.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }
}