package org.perseus.forcePlugin.versioning;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.data.ForceSide;


public interface VersionAdapter {


    ItemStack createCustomHead(String textureValue, ForceSide side);


    void playSonicBoom(Location location);

    void playExplosionEmitter(Location location);

    void applyGlowingEffect(Player target, int durationSeconds);
    FallingBlock spawnFallingBlock(Location location, ItemStack itemStack);
    void launchFallingBlock(FallingBlock block, Vector velocity);
    Particle getRedstoneParticle();
    PotionEffectType getMiningFatigueEffectType();
}