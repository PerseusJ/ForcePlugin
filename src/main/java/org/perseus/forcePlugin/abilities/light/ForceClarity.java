package org.perseus.forcePlugin.abilities.light;

import org.perseus.forcePlugin.versioning.VersionUtil;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceClarity extends AbstractAbility {

    public ForceClarity(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "FORCE_CLARITY";
    }

    @Override
    public String getName() {
        return "Force Clarity";
    }

    @Override
    public String getDescription() {
        return "Clear your mind, removing impairments and enhancing focus.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.LIGHT;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 20.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 30.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int durationTicks = cfgInt(level, "duration-seconds", 15) * 20;
        int hasteAmp = cfgInt(level, "haste-amplifier", 1) - 1;

        // Remove impairments
        if (player.hasPotionEffect(VersionUtil.SLOWNESS)) {
            player.removePotionEffect(VersionUtil.SLOWNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }

        // Add buffs
        player.addPotionEffect(new PotionEffect(VersionUtil.HASTE, durationTicks, Math.max(0, hasteAmp)));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, durationTicks, 0));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

        // Visual: Spiral of white/yellow particles
        new BukkitRunnable() {
            double t = 0;
            @Override
            public void run() {
                t += Math.PI / 8;
                double x = Math.cos(t) * 1.0;
                double y = t * 0.2;
                double z = Math.sin(t) * 1.0;

                Location loc = player.getLocation().add(x, y, z);
                player.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0, 0, 0, 0.02);

                if (t > Math.PI * 4) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}

