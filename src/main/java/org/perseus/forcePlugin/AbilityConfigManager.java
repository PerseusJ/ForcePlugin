package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;

public class AbilityConfigManager {

    private final FileConfiguration config;

    public AbilityConfigManager(ForcePlugin plugin) {
        this.config = plugin.getConfig();
    }

    public int getMaxLevel(String abilityId) {
        return config.getInt("abilities." + abilityId + ".max-level", 1);
    }

    // --- NEW, DEDICATED METHOD FOR UNLOCK COST ---
    /**
     * Gets the unlock cost for an ability, which is outside the 'levels' section.
     * @param abilityId The unique ID of the ability.
     * @return The unlock cost from the config.
     */
    public int getUnlockCost(String abilityId) {
        return config.getInt("abilities." + abilityId + ".unlock-cost", 1);
    }
    // --- END NEW ---

    public double getDoubleValue(String abilityId, int level, String path, double defaultValue) {
        String configPath = "abilities." + abilityId + ".levels." + level + "." + path;
        // This fallback logic is for non-tiered abilities, which is correct.
        if (!config.contains("abilities." + abilityId + ".levels")) {
            configPath = "abilities." + abilityId + "." + path;
        }
        return config.getDouble(configPath, defaultValue);
    }

    public int getIntValue(String abilityId, int level, String path, int defaultValue) {
        String configPath = "abilities." + abilityId + ".levels." + level + "." + path;
        if (!config.contains("abilities." + abilityId + ".levels")) {
            configPath = "abilities." + abilityId + "." + path;
        }
        return config.getInt(configPath, defaultValue);
    }
}