package org.perseus.forcePlugin.managers;

import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.DatabaseManager;
import org.perseus.forcePlugin.data.ForceUser;

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
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            ForceUser forceUser = databaseManager.loadPlayerData(player.getUniqueId());

            forceUser.unlockAbility("FORCE_PUSH");
            forceUser.unlockAbility("FORCE_PULL");
            if (forceUser.getActiveAbilityId() == null) {
                forceUser.setActiveAbilityId("FORCE_PUSH");
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                onlineUsers.put(player.getUniqueId(), forceUser);
                plugin.getLogger().info("Loaded data for " + player.getName());
                plugin.getLevelingManager().updateXpBar(player);
                plugin.getForceBarManager().addPlayer(player);
            });
        });
    }

    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) return;

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