package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.*;

public class ForceBarrier implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceBarrier(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_BARRIER"; }
    @Override public String getName() { return "Force Barrier"; }
    @Override public String getDescription() { return "Grants temporary Absorption hearts."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 25.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 10) * 20;
        int amplifier = configManager.getIntValue(getID(), level, "shield-hearts", 2) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier));
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.2f);

        new BukkitRunnable() {
            int ticks = 0;
            double radius = 1.0;
            double angle = 0;
            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) { this.cancel(); return; }
                Location playerLoc = player.getLocation();
                double x = playerLoc.getX() + radius * Math.cos(angle);
                double z = playerLoc.getZ() + radius * Math.sin(angle);
                Location particleLoc = new Location(player.getWorld(), x, playerLoc.getY() + 1, z);
                player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
                angle += Math.PI / 8;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}