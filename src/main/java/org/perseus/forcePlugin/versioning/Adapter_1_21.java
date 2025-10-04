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

@SuppressWarnings("deprecation")
public class Adapter_1_21 implements VersionAdapter {

    private Particle sonicBoomParticle;
    private Particle explosionEmitterParticle;

    public Adapter_1_21() {
        try {
            sonicBoomParticle = Particle.valueOf("SONIC_BOOM");
        } catch (IllegalArgumentException e) {
            sonicBoomParticle = Particle.EXPLOSION_LARGE;
        }
        try {
            explosionEmitterParticle = Particle.valueOf("EXPLOSION_EMITTER");
        } catch (IllegalArgumentException e) {
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