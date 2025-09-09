package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForceUserManager {

    private final ForcePlugin plugin;
    private final Map<UUID, ForceUser> onlineUsers = new HashMap<>();

    public ForceUserManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayerData(Player player) {
        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        ForceUser forceUser = new ForceUser(player.getUniqueId());

        if (!playerFile.exists()) {
            // This is a new player. Set their active ability to one of the defaults.
            forceUser.setActiveAbilityId("FORCE_PUSH");
        } else {
            // This is an existing player. Load their data.
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            forceUser.setSide(ForceSide.valueOf(playerData.getString("side", "NONE")));
            forceUser.setForceLevel(playerData.getInt("rpg.level", 1));
            forceUser.setForceXp(playerData.getDouble("rpg.xp", 0.0));
            forceUser.setForcePoints(playerData.getInt("rpg.points", 0));
            forceUser.setActiveAbilityId(playerData.getString("active-ability", "FORCE_PUSH"));

            if (playerData.isConfigurationSection("rpg.unlocked-abilities")) {
                for (String abilityId : playerData.getConfigurationSection("rpg.unlocked-abilities").getKeys(false)) {
                    int level = playerData.getInt("rpg.unlocked-abilities." + abilityId);
                    forceUser.getUnlockedAbilities().put(abilityId, level);
                }
            }
        }

        // --- THE FIX: Ensure all players, new and old, always have the default universal abilities unlocked ---
        // This will add them if they are missing for any reason.
        forceUser.unlockAbility("FORCE_PUSH");
        forceUser.unlockAbility("FORCE_PULL");
        // --- END FIX ---

        onlineUsers.put(player.getUniqueId(), forceUser);
        plugin.getLogger().info("Loaded data for " + player.getName());
    }

    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) return;

        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration playerData = new YamlConfiguration();

        playerData.set("side", forceUser.getSide().name());
        playerData.set("active-ability", forceUser.getActiveAbilityId());

        playerData.set("rpg.level", forceUser.getForceLevel());
        playerData.set("rpg.xp", forceUser.getForceXp());
        playerData.set("rpg.points", forceUser.getForcePoints());

        for (Map.Entry<String, Integer> entry : forceUser.getUnlockedAbilities().entrySet()) {
            playerData.set("rpg.unlocked-abilities." + entry.getKey(), entry.getValue());
        }

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