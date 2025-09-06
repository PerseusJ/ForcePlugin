package org.perseus.forcePlugin;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {

    /**
     * Draws a line of simple particles (like END_ROD).
     */
    public static void drawParticleBeam(Player caster, LivingEntity target, Particle particle, double density) {
        drawParticleBeam(caster, target, particle, density, null);
    }

    /**
     * Draws a line of complex particles that require extra data (like DUST).
     */
    public static void drawParticleBeam(Player caster, LivingEntity target, Particle particle, double density, Object data) {
        Location start = caster.getEyeLocation();
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            Location point = start.clone().add(direction.clone().multiply(i));
            caster.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }

    /**
     * Draws a zig-zagging line of particles.
     * @param amplitude How far the zig-zag deviates from the center line.
     */
    public static void drawZigZagBeam(Player caster, LivingEntity target, Particle particle, double density, double amplitude, Object data) {
        Location start = caster.getEyeLocation();
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        // Get a perpendicular vector to create the zig-zag offset
        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            // Calculate the point on the straight line
            Location point = start.clone().add(direction.clone().multiply(i));
            // Add a sine wave offset to create the zig-zag
            double offset = Math.sin(i * 2) * amplitude; // The '2' controls the frequency of the zig-zag
            point.add(perpendicular.clone().multiply(offset));

            caster.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }
}