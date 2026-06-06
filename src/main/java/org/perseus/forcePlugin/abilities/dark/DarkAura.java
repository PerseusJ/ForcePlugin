package org.perseus.forcePlugin.abilities.dark;

import org.perseus.forcePlugin.versioning.VersionUtil;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class DarkAura extends AbstractAbility {

    public DarkAura(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "DARK_AURA";
    }

    @Override
    public String getName() {
        return "Dark Aura";
    }

    @Override
    public String getDescription() {
        return "Emanate a deadly aura that damages nearby enemies over time.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.DARK;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 40.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 35.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int durationTicks = cfgInt(level, "duration-seconds", 10) * 20;
        double damagePerSecond = cfg(level, "damage-per-second-hearts", 1.0) * 2;
        int radius = cfgInt(level, "radius", 6);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.8f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= durationTicks || !player.isOnline() || player.isDead()) {
                    this.cancel();
                    return;
                }

                // Apply damage every second (20 ticks)
                if (ticks % 20 == 0) {
                    for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                        if (entity instanceof LivingEntity && !entity.equals(player)) {
                            ((LivingEntity) entity).damage(damagePerSecond, player);
                            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 0.5f);
                        }
                    }
                }

                // Visual: Red/black spinning particle ring
                double angle = (ticks * Math.PI / 10) % (2 * Math.PI); // Spinning angle
                for (int i = 0; i < 3; i++) { // 3 points in the ring
                    double currentAngle = angle + (i * 2 * Math.PI / 3);
                    double x = radius * Math.cos(currentAngle);
                    double z = radius * Math.sin(currentAngle);
                    Location particleLoc = player.getLocation().add(x, 0.2, z);

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(150, 0, 0), 1.0F); // Red
                    player.getWorld().spawnParticle(plugin.getVersionAdapter().getRedstoneParticle(), particleLoc, 1, 0, 0, 0, 0, dust);
                    player.getWorld().spawnParticle(VersionUtil.LARGE_SMOKE, particleLoc, 1, 0, 0, 0, 0); // Black smoke
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}

