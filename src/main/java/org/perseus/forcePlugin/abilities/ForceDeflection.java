package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.listeners.ProjectileDeflectionListener;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceDeflection implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceDeflection(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_DEFLECTION"; }
    @Override public String getName() { return "Force Deflection"; }
    @Override public String getDescription() { return "Briefly reflect incoming projectiles."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 20.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 12.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double durationSeconds = configManager.getDoubleValue(getID(), level, "duration-seconds", 1.5);
        int durationTicks = (int) (durationSeconds * 20);

        ProjectileDeflectionListener.addDeflectingPlayer(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= durationTicks || !player.isOnline() || !ProjectileDeflectionListener.isDeflecting(player.getUniqueId())) {
                    ProjectileDeflectionListener.removeDeflectingPlayer(player.getUniqueId());
                    this.cancel();
                    return;
                }
                Location center = player.getEyeLocation();
                for (int i = 0; i < 5; i++) {
                    Vector direction = player.getLocation().getDirection().clone();
                    Vector random = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().multiply(0.8);
                    direction.add(random).normalize();
                    Location particleLoc = center.clone().add(direction);
                    player.getWorld().spawnParticle(Particle.CRIT, particleLoc, 1, 0, 0, 0, 0);
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}