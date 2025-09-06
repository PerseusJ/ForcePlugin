package org.perseus.forcePlugin.abilities;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.ForceSide;
import org.perseus.forcePlugin.ProjectileDeflectionListener;

public class ForceDeflection implements Ability {

    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;

    public ForceDeflection(AbilityConfigManager configManager, ForcePlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public String getID() { return "FORCE_DEFLECTION"; }

    @Override
    public String getName() { return "Force Deflection"; }

    @Override
    public String getDescription() { return "Briefly reflect incoming projectiles."; }

    @Override
    public ForceSide getSide() { return ForceSide.LIGHT; }

    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 20.0); }

    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 12.0); }

    @Override
    public void execute(Player player) {
        double durationSeconds = configManager.getDoubleValue(getID(), "duration-seconds", 1.5);
        int durationTicks = (int) (durationSeconds * 20);

        // Add the player to the set of "deflecting" players.
        ProjectileDeflectionListener.addDeflectingPlayer(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 20, 0.5, 0.5, 0.5);

        // Schedule a task to remove them from the set after the duration expires.
        new BukkitRunnable() {
            @Override
            public void run() {
                ProjectileDeflectionListener.removeDeflectingPlayer(player.getUniqueId());
            }
        }.runTaskLater(plugin, durationTicks);
    }
}