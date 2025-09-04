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

    // --- MODIFIED: No longer final ---
    private double regenAmountPerSecond;

    public ForceBarManager(ForcePlugin plugin, ForceUserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
        // Load the initial value from the config.
        reloadConfig();
        startEnergyRegenTask();
    }

    // --- NEW METHOD ---
    /**
     * Reloads the configuration values used by this manager from the config.yml.
     */
    public void reloadConfig() {
        this.regenAmountPerSecond = plugin.getConfig().getDouble("force-energy.regeneration-per-second", 2.5);
    }
    // --- END NEW ---

    // ... (addPlayer, removePlayer, updateBar methods remain exactly the same) ...
    public void addPlayer(Player player) { /* ... */ }
    public void removePlayer(Player player) { /* ... */ }
    public void updateBar(Player player) { /* ... */ }

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