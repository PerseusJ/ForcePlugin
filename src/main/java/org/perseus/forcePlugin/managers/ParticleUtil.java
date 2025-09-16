package org.perseus.forcePlugin.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {

    public static void drawParticleBeam(Player caster, LivingEntity target, Particle particle, double density) {
        drawParticleBeam(caster, target, particle, density, null);
    }

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

    public static void drawZigZagBeam(Player caster, LivingEntity target, Particle particle, double density, double amplitude, Object data) {
        Location start = caster.getEyeLocation();
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            Location point = start.clone().add(direction.clone().multiply(i));
            double offset = Math.sin(i * 2) * amplitude;
            point.add(perpendicular.clone().multiply(offset));
            caster.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }
}