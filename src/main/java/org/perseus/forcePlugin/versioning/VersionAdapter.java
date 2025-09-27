package org.perseus.forcePlugin.versioning;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.data.ForceSide;

public interface VersionAdapter {
    ItemStack createCustomHead(String textureValue, ForceSide side);
    void playSonicBoom(Location location);
    void playExplosionEmitter(Location location);
}