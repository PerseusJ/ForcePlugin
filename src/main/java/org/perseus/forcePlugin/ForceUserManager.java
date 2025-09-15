package org.perseus.forcePlugin;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForceUserManager {

    private final ForcePlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, ForceUser> onlineUsers = new HashMap<>();

    public ForceUserManager(ForcePlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void loadPlayerData(Player player) {
        // Load data from the database asynchronously to prevent lag on join
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            ForceUser forceUser = databaseManager.loadPlayerData(player.getUniqueId());

            // If the player is new (no abilities unlocked), give them the defaults
            if (forceUser.getUnlockedAbilities().isEmpty()) {
                forceUser.unlockAbility("FORCE_PUSH");
                forceUser.unlockAbility("FORCE_PULL");
                forceUser.setActiveAbilityId("FORCE_PUSH");
            }

            // Add the loaded data to the main server thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                onlineUsers.put(player.getUniqueId(), forceUser);
                plugin.getLogger().info("Loaded data for " + player.getName());
                // Now that data is loaded, update their bars
                plugin.getLevelingManager().updateXpBar(player);
                plugin.getForceBarManager().addPlayer(player);
            });
        });
    }

    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) return;

        // Save data to the database asynchronously to prevent lag on quit
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            databaseManager.savePlayerData(forceUser);
            plugin.getLogger().info("Saved data for " + player.getName());
        });
    }

    public void removePlayerFromCache(Player player) {
        onlineUsers.remove(player.getUniqueId());
    }

    public ForceUser getForceUser(Player player) {
        return onlineUsers.get(player.getUniqueId());
    }
}