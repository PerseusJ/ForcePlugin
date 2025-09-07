package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages reading and providing configuration values from the config.yml file.
 * This class centralizes all configuration access, making it easy to manage.
 */
public class AbilityConfigManager {

    private final FileConfiguration config;

    public AbilityConfigManager(ForcePlugin plugin) {
        this.config = plugin.getConfig();
    }

    /**
     * Gets the maximum level for a specific ability from the config.
     * @param abilityId The unique ID of the ability (e.g., "FORCE_PUSH").
     * @return The max level, or 1 if not specified.
     */
    public int getMaxLevel(String abilityId) {
        return config.getInt("abilities." + abilityId + ".max-level", 1);
    }

    /**
     * A generic method to get any double value for a specific level of an ability.
     * @param abilityId The unique ID of the ability.
     * @param level The level of the ability to get the value for.
     * @param path The specific value to get (e.g., "energy-cost").
     * @param defaultValue A fallback value.
     * @return The value from the config.
     */
    public double getDoubleValue(String abilityId, int level, String path, double defaultValue) {
        // The full path will look like: "abilities.FORCE_PUSH.levels.1.energy-cost"
        String configPath = "abilities." + abilityId + ".levels." + level + "." + path;

        // If the ability is not tiered, check the old path format.
        if (!config.contains("abilities." + abilityId + ".levels")) {
            configPath = "abilities." + abilityId + "." + path;
        }

        return config.getDouble(configPath, defaultValue);
    }

    /**
     * A generic method to get any integer value for a specific level of an ability.
     */
    public int getIntValue(String abilityId, int level, String path, int defaultValue) {
        String configPath = "abilities." + abilityId + ".levels." + level + "." + path;

        if (!config.contains("abilities." + abilityId + ".levels")) {
            configPath = "abilities." + abilityId + "." + path;
        }

        return config.getInt(configPath, defaultValue);
    }
}