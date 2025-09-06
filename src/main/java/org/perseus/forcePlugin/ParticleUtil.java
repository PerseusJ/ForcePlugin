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
        // This method calls the more complex one with 'null' for the data.
        drawParticleBeam(caster, target, particle, density, null);
    }

    /**
     * Draws a line of complex particles that require extra data (like REDSTONE).
     *
     * @param data The extra data, e.g., a Particle.DustOptions object.
     */
    public static void drawParticleBeam(Player caster, LivingEntity target, Particle particle, double density, Object data) {
        Location start = caster.getEyeLocation();
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();

        for (double i = 0; i < distance; i += (1 / density)) {
            Location point = start.clone().add(direction.clone().multiply(i));
            // The spawnParticle method can accept the extra data object.
            caster.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0, data);
        }
    }
}