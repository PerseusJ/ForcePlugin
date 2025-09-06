package org.perseus.forcePlugin.abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.ForceSide;

public class ForceRage implements Ability {

    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;

    public ForceRage(AbilityConfigManager configManager, ForcePlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public String getID() { return "FORCE_RAGE"; }

    @Override
    public String getName() { return "Force Rage"; }

    @Override
    public String getDescription() { return "Gain speed and damage, but become vulnerable."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 40.0); }

    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 35.0); }

    @Override
    public void execute(Player player) {
        int duration = configManager.getIntValue(getID(), "duration-seconds", 8) * 20;
        int speedAmp = configManager.getIntValue(getID(), "speed-amplifier", 2) - 1;
        int strengthAmp = configManager.getIntValue(getID(), "strength-amplifier", 2) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, speedAmp));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, strengthAmp));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.3f);

        // --- NEW: Swirling Rage Aura ---
        new BukkitRunnable() {
            int ticks = 0;
            double radius = 0.8;
            double angle = 0;

            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) {
                    this.cancel();
                    return;
                }

                Location playerLoc = player.getLocation();
                // Calculate two points opposite each other to create a double helix swirl
                double x1 = playerLoc.getX() + radius * Math.cos(angle);
                double z1 = playerLoc.getZ() + radius * Math.sin(angle);
                Location particleLoc1 = new Location(player.getWorld(), x1, playerLoc.getY() + (ticks % 20) / 10.0, z1); // Moves up and down

                double x2 = playerLoc.getX() + radius * Math.cos(angle + Math.PI); // Opposite side
                double z2 = playerLoc.getZ() + radius * Math.sin(angle + Math.PI);
                Location particleLoc2 = new Location(player.getWorld(), x2, playerLoc.getY() + (20 - (ticks % 20)) / 10.0, z2); // Moves opposite

                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(50, 0, 0), 1.0F);
                player.getWorld().spawnParticle(Particle.DUST, particleLoc1, 1, 0, 0, 0, 0, dustOptions);
                player.getWorld().spawnParticle(Particle.SMOKE, particleLoc2, 1, 0, 0, 0, 0);

                angle += Math.PI / 6;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        // --- END NEW ---
    }
}