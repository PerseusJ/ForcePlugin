package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all ForceUser objects for online players, including saving and loading data from files.
 */
public class ForceUserManager {

    private final ForcePlugin plugin;
    private final Map<UUID, ForceUser> onlineUsers = new HashMap<>();

    public ForceUserManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads a player's data from their file when they join the server.
     * If no file exists, it creates a fresh data object for the new player.
     * @param player The player whose data is being loaded.
     */
    public void loadPlayerData(Player player) {
        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        ForceUser forceUser = new ForceUser(player.getUniqueId());

        // Load the player's chosen side. Default to NONE if not found.
        forceUser.setSide(ForceSide.valueOf(playerData.getString("side", "NONE")));

        // Load the player's bound abilities.
        if (playerData.isConfigurationSection("bound-abilities")) {
            for (String key : playerData.getConfigurationSection("bound-abilities").getKeys(false)) {
                int slot = Integer.parseInt(key);
                String abilityId = playerData.getString("bound-abilities." + key);
                forceUser.setBoundAbility(slot, abilityId);
            }
        }

        onlineUsers.put(player.getUniqueId(), forceUser);
        plugin.getLogger().info("Loaded data for " + player.getName());
    }

    /**
     * Saves a player's data to their file. This is typically called when they quit.
     * @param player The player whose data is being saved.
     */
    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) {
            plugin.getLogger().warning("Could not find ForceUser data for " + player.getName() + " to save.");
            return;
        }

        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration playerData = new YamlConfiguration(); // Create a fresh config to avoid old data.

        // Save the player's side.
        playerData.set("side", forceUser.getSide().name());

        // Save the bound abilities.
        for (int i = 1; i <= 3; i++) {
            String abilityId = forceUser.getBoundAbility(i);
            if (abilityId != null) {
                // The path will look like "bound-abilities.1", "bound-abilities.2", etc.
                playerData.set("bound-abilities." + i, abilityId);
            }
        }

        try {
            playerData.save(playerFile);
            plugin.getLogger().info("Saved data for " + player.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("!!! Could not save player data for " + player.getName() + " !!!");
            e.printStackTrace();
        }
    }

    /**
     * Removes a player from the online cache.
     * @param player The player to remove.
     */
    public void removePlayerFromCache(Player player) {
        onlineUsers.remove(player.getUniqueId());
    }

    public ForceUser getForceUser(Player player) {
        return onlineUsers.get(player.getUniqueId());
    }
}