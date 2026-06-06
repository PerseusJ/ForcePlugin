package org.perseus.forcePlugin.abilities;

import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public abstract class AbstractAbility implements Ability {
    protected final AbilityConfigManager configManager;
    protected final ForcePlugin plugin;

    public AbstractAbility(AbilityConfigManager configManager, ForcePlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    protected double cfg(int level, String key, double def) {
        return configManager.getDoubleValue(getID(), level, key, def);
    }

    protected int cfgInt(int level, String key, int def) {
        return configManager.getIntValue(getID(), level, key, def);
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 0.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 0.0);
    }

    public String getLevelDescription(int level) {
        return ""; // Can be overridden by subclasses
    }
}
