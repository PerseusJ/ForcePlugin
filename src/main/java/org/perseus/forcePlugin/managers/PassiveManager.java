package org.perseus.forcePlugin.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.Passive;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassiveManager {

    private final ForcePlugin plugin;
    private final Map<String, Passive> passives = new HashMap<>();
    private final Map<ForceSide, List<String>> passivesBySide = new HashMap<>();
    // --- NEW: Store the passives config file ---
    private FileConfiguration passivesConfig;

    public PassiveManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadPassives();
    }

    public void loadPassives() {
        passives.clear();
        passivesBySide.clear();
        File passivesFile = new File(plugin.getDataFolder(), "passives.yml");
        if (!passivesFile.exists()) {
            plugin.saveResource("passives.yml", false);
        }

        // --- MODIFIED: Load the config into our class field ---
        this.passivesConfig = YamlConfiguration.loadConfiguration(passivesFile);
        ConfigurationSection mainSection = passivesConfig.getConfigurationSection("passives");
        if (mainSection == null) return;

        for (String sideKey : mainSection.getKeys(false)) {
            try {
                ForceSide side = ForceSide.valueOf(sideKey.toUpperCase());
                List<String> sidePassiveIds = new ArrayList<>();
                ConfigurationSection sideSection = mainSection.getConfigurationSection(sideKey);
                if (sideSection == null) continue;

                for (String passiveId : sideSection.getKeys(false)) {
                    String displayName = sideSection.getString(passiveId + ".display-name");
                    List<String> description = sideSection.getStringList(passiveId + ".description");
                    passives.put(passiveId, new Passive(passiveId, displayName, description));
                    sidePassiveIds.add(passiveId);
                }
                passivesBySide.put(side, sidePassiveIds);

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid Force Side '" + sideKey + "' found in passives.yml.");
            }
        }
        plugin.getLogger().info("Loaded " + passives.size() + " passive abilities from passives.yml.");
    }

    public Passive getPassive(String id) {
        return passives.get(id);
    }

    public List<String> getPassiveIdsForSide(ForceSide side) {
        return passivesBySide.getOrDefault(side, new ArrayList<>());
    }

    // --- THE FIX: Read values from the correct passivesConfig file ---
    public double getPassiveDoubleValue(String passiveId, int level, String path, double defaultValue) {
        // Construct the path for the specific level
        String configPath = "passives." + getSideForPassive(passiveId) + "." + passiveId + ".levels." + level + "." + path;
        return passivesConfig.getDouble(configPath, defaultValue);
    }

    public int getPassiveIntValue(String passiveId, int level, String path, int defaultValue) {
        // This method is now for level-specific values
        String configPath = "passives." + getSideForPassive(passiveId) + "." + passiveId + ".levels." + level + "." + path;
        return passivesConfig.getInt(configPath, defaultValue);
    }

    // --- NEW: Dedicated methods for top-level values like max-level and unlock-cost ---
    public int getPassiveTopLevelIntValue(String passiveId, String path, int defaultValue) {
        String configPath = "passives." + getSideForPassive(passiveId) + "." + passiveId + "." + path;
        return passivesConfig.getInt(configPath, defaultValue);
    }

    // Helper to find which side a passive belongs to for path construction
    private String getSideForPassive(String passiveId) {
        for (Map.Entry<ForceSide, List<String>> entry : passivesBySide.entrySet()) {
            if (entry.getValue().contains(passiveId)) {
                return entry.getKey().name();
            }
        }
        return ""; // Should not happen
    }
}