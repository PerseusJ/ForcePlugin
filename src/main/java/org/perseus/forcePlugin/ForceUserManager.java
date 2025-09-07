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
            forceUser.unlockAbility("FORCE_PUSH");
            forceUser.unlockAbility("FORCE_PULL");
        } else {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            forceUser.setSide(ForceSide.valueOf(playerData.getString("side", "NONE")));

            if (playerData.isConfigurationSection("bound-abilities")) {
                for (String key : playerData.getConfigurationSection("bound-abilities").getKeys(false)) {
                    forceUser.setBoundAbility(Integer.parseInt(key), playerData.getString("bound-abilities." + key));
                }
            }

            forceUser.setForceLevel(playerData.getInt("rpg.level", 1));
            forceUser.setForceXp(playerData.getDouble("rpg.xp", 0.0));
            forceUser.setForcePoints(playerData.getInt("rpg.points", 0));

            if (playerData.isConfigurationSection("rpg.unlocked-abilities")) {
                for (String abilityId : playerData.getConfigurationSection("rpg.unlocked-abilities").getKeys(false)) {
                    int level = playerData.getInt("rpg.unlocked-abilities." + abilityId);
                    forceUser.getUnlockedAbilities().put(abilityId, level);
                }
            }
            if (forceUser.getUnlockedAbilities().isEmpty()) {
                forceUser.unlockAbility("FORCE_PUSH");
                forceUser.unlockAbility("FORCE_PULL");
            }
        }
        onlineUsers.put(player.getUniqueId(), forceUser);
        plugin.getLogger().info("Loaded data for " + player.getName());
    }

    public void savePlayerData(Player player) {
        ForceUser forceUser = onlineUsers.get(player.getUniqueId());
        if (forceUser == null) return;

        File playerFile = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration playerData = new YamlConfiguration();

        playerData.set("side", forceUser.getSide().name());

        // --- THE FIX: Call the new getter method ---
        for (Map.Entry<Integer, String> entry : forceUser.getBoundAbilities().entrySet()) {
            playerData.set("bound-abilities." + entry.getKey(), entry.getValue());
        }

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