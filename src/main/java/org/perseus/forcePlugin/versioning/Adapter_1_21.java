package org.perseus.forcePlugin.versioning;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.perseus.forcePlugin.data.ForceSide;

import java.util.UUID;

// This class will still have "errors" in the IDE because it's using 1.17+ features
// while compiling against 1.16.5. This is expected and will work on the server.
public class Adapter_1_21 implements VersionAdapter {

    // We store the "future" particles in variables to avoid errors.
    private Particle sonicBoomParticle;
    private Particle explosionEmitterParticle;

    public Adapter_1_21() {
        // Use reflection to safely get the particles that don't exist in 1.16
        try {
            sonicBoomParticle = Particle.valueOf("SONIC_BOOM");
        } catch (IllegalArgumentException e) {
            // Fallback for versions like 1.17/1.18 that don't have SONIC_BOOM
            sonicBoomParticle = Particle.EXPLOSION_LARGE;
        }
        try {
            explosionEmitterParticle = Particle.valueOf("EXPLOSION_EMITTER");
        } catch (IllegalArgumentException e) {
            // This should not fail on 1.17+ but is a good safety measure
            explosionEmitterParticle = Particle.EXPLOSION_HUGE;
        }
    }

    @Override
    public ItemStack createCustomHead(String textureValue, ForceSide side) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        ProfileProperty property = new ProfileProperty("textures", textureValue);
        profile.setProperty(property);
        meta.setPlayerProfile(profile);

        head.setItemMeta(meta);
        return head;
    }

    @Override
    public void playSonicBoom(Location location) {
        location.getWorld().spawnParticle(sonicBoomParticle, location, 1);
    }

    @Override
    public void playExplosionEmitter(Location location) {
        location.getWorld().spawnParticle(explosionEmitterParticle, location, 1);
    }
}