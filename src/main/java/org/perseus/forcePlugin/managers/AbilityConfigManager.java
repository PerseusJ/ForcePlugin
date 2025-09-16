package org.perseus.forcePlugin.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.perseus.forcePlugin.ForcePlugin;

public class AbilityConfigManager {

    private final FileConfiguration config;

    public AbilityConfigManager(ForcePlugin plugin) {
        this.config = plugin.getConfig();
    }

    public int getMaxLevel(String abilityId) {
        return config.getInt("abilities." + abilityId + ".max-level", 1);
    }

    public int getUnlockCost(String abilityId) {
        return config.getInt("abilities." + abilityId + ".unlock-cost", 1);
    }

    public double getDoubleValue(String abilityId, int level, String path, double defaultValue) {
        String configPath = "abilities." + abilityId + ".levels." + level + "." + path;
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