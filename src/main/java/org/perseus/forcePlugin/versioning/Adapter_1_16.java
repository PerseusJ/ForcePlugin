package org.perseus.forcePlugin.versioning;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.perseus.forcePlugin.data.ForceSide;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * The implementation of the VersionAdapter for Minecraft 1.16.5.
 */
public class Adapter_1_16 implements VersionAdapter {

    @Override
    public ItemStack createCustomHead(String textureValue, ForceSide side) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "HolocronProfile");
        profile.getProperties().put("textures", new Property("textures", textureValue));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            // If reflection fails, return a simple fallback item.
            return new ItemStack(side == ForceSide.LIGHT ? Material.NETHER_STAR : Material.DRAGON_BREATH);
        }

        head.setItemMeta(meta);
        return head;
    }

    @Override
    public void playSonicBoom(Location location) {
        // SONIC_BOOM does not exist in 1.16. We use a similar-looking fallback.
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 1);
    }

    @Override
    public void playExplosionEmitter(Location location) {
        // EXPLOSION_EMITTER does not exist in 1.16. This is the closest equivalent.
        location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
    }
}