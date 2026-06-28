package org.perseus.forcePlugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.DatabaseManager;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ForceUserManager {

    private final ForcePlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, ForceUser> onlineUsers = new HashMap<>();
    private final AtomicInteger pendingSaves = new AtomicInteger(0);

    public ForceUserManager(ForcePlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void loadPlayerData(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            ForceUser forceUser = databaseManager.loadPlayerData(player.getUniqueId());

            forceUser.unlockAbility("FORCE_PUSH");
            forceUser.unlockAbility("FORCE_PULL");
            if (forceUser.getSlotBinds().isEmpty()) {
                forceUser.setSlotBind(0, "FORCE_PUSH");
                forceUser.setSlotBind(1, "FORCE_PULL");
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                onlineUsers.put(player.getUniqueId(), forceUser);
                forceUser.setDataLoaded(true);
                plugin.getLogger().info("Loaded data for " + player.getName());
                plugin.getLevelingManager().updateXpBar(player);
                plugin.getForceBarManager().addPlayer(player);
                plugin.getHudManager().addPlayer(player);
            });
        });
    }

    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) return;

        pendingSaves.incrementAndGet();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                databaseManager.savePlayerData(forceUser);
            } finally {
                pendingSaves.decrementAndGet();
            }
        });
    }

    public void saveAllOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }
    }

    public void removePlayerFromCache(Player player) {
        onlineUsers.remove(player.getUniqueId());
    }

    public ForceUser getForceUser(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser != null && !forceUser.isDataLoaded()) {
            return null;
        }
        return forceUser;
    }

    public int getPendingSaveCount() {
        return pendingSaves.get();
    }
}