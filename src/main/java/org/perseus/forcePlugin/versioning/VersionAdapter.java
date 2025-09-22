package org.perseus.forcePlugin.versioning;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.data.ForceSide;

/**
 * An interface that defines all methods that have version-specific implementations.
 */
public interface VersionAdapter {

    /**
     * Creates a custom player head with the given texture.
     * @param textureValue The Base64 texture string.
     * @param side The side, used for a fallback material if head creation fails.
     * @return The custom head ItemStack.
     */
    ItemStack createCustomHead(String textureValue, ForceSide side);

    /**
     * Plays the SONIC_BOOM particle effect, or a fallback for older versions.
     * @param location The location to play the particle at.
     */
    void playSonicBoom(Location location);

    /**
     * Plays the EXPLOSION_EMITTER particle effect, or a fallback for older versions.
     * @param location The location to play the particle at.
     */
    void playExplosionEmitter(Location location);

}