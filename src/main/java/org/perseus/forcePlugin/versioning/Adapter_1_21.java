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
import org.bukkit.potion.PotionEffectType;

public class Adapter_1_21 implements VersionAdapter {

    private Particle sonicBoomParticle;
    private Particle explosionEmitterParticle;

    @Override
    public org.bukkit.entity.FallingBlock spawnFallingBlock(Location location, ItemStack itemStack) {
        org.bukkit.World world = location.getWorld();
        if (world == null) {
            return null;
        }
        org.bukkit.Material material = itemStack.getType();
        org.bukkit.block.data.BlockData blockData;
        try {
            if (material.isBlock()) {
                blockData = material.createBlockData();
            } else {
                blockData = org.bukkit.Material.SAND.createBlockData();
            }
        } catch (Exception e) {
            blockData = org.bukkit.Material.SAND.createBlockData();
        }
        return world.spawnFallingBlock(location, blockData);
    }

    @Override
    public void launchFallingBlock(org.bukkit.entity.FallingBlock fallingBlock, org.bukkit.util.Vector velocity) {
        fallingBlock.setVelocity(velocity);
    }

    @Override
    public void applyGlowingEffect(org.bukkit.entity.Player player, int durationTicks) {
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.GLOWING, durationTicks, 0, false, false));
    }

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

    @Override
    public Particle getRedstoneParticle() {
        try {
            return Particle.valueOf("DUST");
        } catch (IllegalArgumentException e) {
            return Particle.REDSTONE;
        }
    }

    @Override
    public PotionEffectType getMiningFatigueEffectType() {
        return PotionEffectType.SLOW_DIGGING;
    }
}