package org.perseus.forcePlugin.data;

import org.bukkit.enchantments.Enchantment;

public class ForceEnchantment {
    private final Enchantment enchantment;
    private final int energyCostPerLevel;
    private final int maxLevel;

    public ForceEnchantment(Enchantment enchantment, int energyCostPerLevel, int maxLevel) {
        this.enchantment = enchantment;
        this.energyCostPerLevel = energyCostPerLevel;
        this.maxLevel = maxLevel;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getEnergyCostPerLevel() {
        return energyCostPerLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
