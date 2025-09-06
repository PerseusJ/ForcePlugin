package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
     */
    public void loadPlayerData(Player player) {
        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        ForceUser forceUser = new ForceUser(player.getUniqueId());

        // If the file doesn't exist, it's a new player. Give them default abilities.
        if (!playerFile.exists()) {
            forceUser.unlockAbility("FORCE_PUSH");
            forceUser.unlockAbility("FORCE_PULL");
        } else {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            // Load core data
            forceUser.setSide(ForceSide.valueOf(playerData.getString("side", "NONE")));

            // Load bound abilities
            if (playerData.isConfigurationSection("bound-abilities")) {
                for (String key : playerData.getConfigurationSection("bound-abilities").getKeys(false)) {
                    int slot = Integer.parseInt(key);
                    String abilityId = playerData.getString("bound-abilities." + key);
                    forceUser.setBoundAbility(slot, abilityId);
                }
            }

            // --- NEW: Load RPG data ---
            forceUser.setForceLevel(playerData.getInt("rpg.level", 1));
            forceUser.setForceXp(playerData.getDouble("rpg.xp", 0.0));
            forceUser.setForcePoints(playerData.getInt("rpg.points", 0));
            // Load the list of unlocked abilities. If it's a new player file, default to Push and Pull.
            forceUser.getUnlockedAbilities().addAll(playerData.getStringList("rpg.unlocked-abilities"));
            if (forceUser.getUnlockedAbilities().isEmpty()) {
                forceUser.unlockAbility("FORCE_PUSH");
                forceUser.unlockAbility("FORCE_PULL");
            }
            // --- END NEW ---
        }

        onlineUsers.put(player.getUniqueId(), forceUser);
        plugin.getLogger().info("Loaded data for " + player.getName());
    }

    /**
     * Saves a player's data to their file.
     */
    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) {
            plugin.getLogger().warning("Could not find ForceUser data for " + player.getName() + " to save.");
            return;
        }

        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration playerData = new YamlConfiguration();

        // Save core data
        playerData.set("side", forceUser.getSide().name());

        // Save bound abilities
        for (int i = 1; i <= 3; i++) {
            String abilityId = forceUser.getBoundAbility(i);
            if (abilityId != null) {
                playerData.set("bound-abilities." + i, abilityId);
            }
        }

        // --- NEW: Save RPG data ---
        playerData.set("rpg.level", forceUser.getForceLevel());
        playerData.set("rpg.xp", forceUser.getForceXp());
        playerData.set("rpg.points", forceUser.getForcePoints());
        playerData.set("rpg.unlocked-abilities", forceUser.getUnlockedAbilities());
        // --- END NEW ---

        try {
            playerData.save(playerFile);
            plugin.getLogger().info("Saved data for " + player.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("!!! Could not save player data for " + player.getName() + " !!!");
            e.printStackTrace();
        }
    }

    public void removePlayerFromCache(Player player) {
        onlineUsers.remove(player.getUniqueId());
    }

    public ForceUser getForceUser(Player player) {
        return onlineUsers.get(player.getUniqueId());
    }
}