package org.perseus.forcePlugin;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForceBarManager {

    private final ForcePlugin plugin;
    private final ForceUserManager userManager;
    private final Map<UUID, BossBar> playerBars = new HashMap<>();
    private double regenAmountPerSecond;

    public ForceBarManager(ForcePlugin plugin, ForceUserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
        reloadConfig();
        startEnergyRegenTask();
        // --- DEBUG ---
        plugin.getLogger().info("ForceBarManager initialized successfully.");
    }

    public void reloadConfig() {
        this.regenAmountPerSecond = plugin.getConfig().getDouble("force-energy.regeneration-per-second", 2.5);
        // --- DEBUG ---
        plugin.getLogger().info("ForceBarManager config reloaded. Regen rate is now: " + regenAmountPerSecond);
    }

    public void addPlayer(Player player) {
        // --- DEBUG ---
        plugin.getLogger().info("Attempting to add Boss Bar for player: " + player.getName());
        ForceUser user = userManager.getForceUser(player);
        if (user == null) {
            // --- DEBUG ---
            plugin.getLogger().warning("Could not add Boss Bar because ForceUser data was null for " + player.getName());
            return;
        }

        BossBar bar = Bukkit.createBossBar("Force Energy", BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(player);
        playerBars.put(player.getUniqueId(), bar);

        updateBar(player);
        // --- DEBUG ---
        plugin.getLogger().info("Successfully added Boss Bar for " + player.getName());
    }

    public void removePlayer(Player player) {
        BossBar bar = playerBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
            // --- DEBUG ---
            plugin.getLogger().info("Removed Boss Bar for " + player.getName());
        }
    }

    public void updateBar(Player player) {
        BossBar bar = playerBars.get(player.getUniqueId());
        ForceUser user = userManager.getForceUser(player);
        if (bar == null || user == null) return;

        if (user.getSide() == ForceSide.LIGHT) {
            bar.setColor(BarColor.BLUE);
        } else if (user.getSide() == ForceSide.DARK) {
            bar.setColor(BarColor.RED);
        } else {
            bar.setColor(BarColor.WHITE);
        }

        double progress = user.getCurrentForceEnergy() / 100.0;
        bar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
    }

    private void startEnergyRegenTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : playerBars.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        ForceUser user = userManager.getForceUser(player);
                        if (user != null && user.getSide() != ForceSide.NONE) {
                            double currentEnergy = user.getCurrentForceEnergy();
                            if (currentEnergy < 100.0) {
                                user.setCurrentForceEnergy(currentEnergy + regenAmountPerSecond);
                                updateBar(player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}