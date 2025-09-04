package org.perseus.forcePlugin;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages reading and providing configuration values from the config.yml file.
 * This class centralizes all configuration access, making it easy to manage.
 */
public class AbilityConfigManager {

    private final FileConfiguration config;

    /**
     * Constructor for the AbilityConfigManager.
     * @param plugin A reference to the main plugin class to get its config.
     */
    public AbilityConfigManager(ForcePlugin plugin) {
        this.config = plugin.getConfig();
    }

    /**
     * A generic method to get any double value (like cost, cooldown, damage) for an ability.
     * @param abilityId The unique ID of the ability (e.g., "FORCE_PUSH").
     * @param path The specific value to get (e.g., "energy-cost").
     * @param defaultValue A fallback value in case the path is missing from the config.
     * @return The value from the config, or the default value if not found.
     */
    public double getDoubleValue(String abilityId, String path, double defaultValue) {
        // The full path in the config will look like: "abilities.FORCE_PUSH.energy-cost"
        return config.getDouble("abilities." + abilityId + "." + path, defaultValue);
    }

    /**
     * A generic method to get any integer value (like duration, amplifier) for an ability.
     * @param abilityId The unique ID of the ability (e.g., "FORCE_PUSH").
     * @param path The specific value to get (e.g., "duration-seconds").
     * @param defaultValue A fallback value in case the path is missing from the config.
     * @return The value from the config, or the default value if not found.
     */
    public int getIntValue(String abilityId, String path, int defaultValue) {
        return config.getInt("abilities." + abilityId + "." + path, defaultValue);
    }
}