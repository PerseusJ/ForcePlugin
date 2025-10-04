package org.perseus.forcePlugin.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceEnchantment;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class ForceEnchantManager {

    private final ForcePlugin plugin;
    private final Map<Enchantment, ForceEnchantment> enchantmentData = new HashMap<>();
    private final Map<UUID, Map<Enchantment, Integer>> playerSelections = new HashMap<>();

    public ForceEnchantManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadEnchantmentData();
    }

    public void loadEnchantmentData() {
        enchantmentData.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("force-enchanting.enchantments");
        if (section == null) {
            plugin.getLogger().warning("Could not find 'force-enchanting.enchantments' section in config.yml!");
            return;
        }

        for (String key : section.getKeys(false)) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
            if (enchantment == null) {
                plugin.getLogger().warning("Invalid enchantment in config: " + key);
                continue;
            }
            int cost = section.getInt(key + ".energy-cost-per-level");
            int maxLevel = section.getInt(key + ".max-level");
            enchantmentData.put(enchantment, new ForceEnchantment(enchantment, cost, maxLevel));
        }
    }

    public boolean canUse(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) {
            return false;
        }
        // A player can use this feature if they have chosen a specialization.
        return forceUser.getSpecialization() != null;
    }

    public List<ForceEnchantment> getApplicableEnchantments(ItemStack item) {
        return enchantmentData.keySet().stream()
                .filter(enchant -> enchant.canEnchantItem(item))
                .map(enchantmentData::get)
                .collect(Collectors.toList());
    }

    public Map<Enchantment, ForceEnchantment> getEnchantmentData() {
        return enchantmentData;
    }

    public Map<Enchantment, Integer> getPlayerSelections(Player player) {
        return playerSelections.getOrDefault(player.getUniqueId(), new HashMap<>());
    }

    public void clearPlayerSelections(Player player) {
        playerSelections.remove(player.getUniqueId());
    }

    public void updatePlayerSelection(Player player, Enchantment enchantment, int level) {
        playerSelections.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(enchantment, level);
    }

    public void applyEnchantments(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        ItemStack item = player.getInventory().getItemInMainHand();
        Map<Enchantment, Integer> selections = getPlayerSelections(player);

        if (forceUser == null || item == null || item.getType() == Material.AIR || selections.isEmpty()) {
            clearPlayerSelections(player);
            player.closeInventory();
            return;
        }

        int totalCost = calculateTotalCost(item, selections);

        if (forceUser.getCurrentForceEnergy() < totalCost) {
            player.sendMessage(ChatColor.RED + "You do not have enough Force Energy! Requires " + totalCost);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // --- Apply enchantments ---
        ItemStack newItem = item.clone();
        for (Map.Entry<Enchantment, Integer> entry : selections.entrySet()) {
            if (entry.getValue() > item.getEnchantmentLevel(entry.getKey())) {
                newItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }
        player.getInventory().setItemInMainHand(newItem);

        // --- Deduct cost and apply debuffs ---
        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - totalCost);
        applyDebuffs(player, totalCost);

        player.sendMessage(ChatColor.GREEN + "Your item has been infused with the Force!");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);

        clearPlayerSelections(player);
        player.closeInventory();
    }

    private int calculateTotalCost(ItemStack item, Map<Enchantment, Integer> selections) {
        int totalCost = 0;
        for (Map.Entry<Enchantment, Integer> entry : selections.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int selectedLevel = entry.getValue();
            int currentLevel = item.getEnchantmentLevel(enchantment);

            if (selectedLevel > currentLevel) {
                ForceEnchantment fe = getEnchantmentData().get(enchantment);
                if (fe != null) {
                    for (int i = currentLevel + 1; i <= selectedLevel; i++) {
                        totalCost += fe.getEnergyCostPerLevel() * i;
                    }
                }
            }
        }
        return totalCost;
    }

    private void applyDebuffs(Player player, int totalCost) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;
        ConfigurationSection debuffConfig = plugin.getConfig().getConfigurationSection("force-enchanting.debuffs");
        if (debuffConfig == null) return;

        // Regen Slowdown
        double baseDuration = debuffConfig.getDouble("regen-slowdown-base-duration", 1.5);
        double modifier = debuffConfig.getDouble("regen-slowdown-modifier", 0.5);
        long expiry = System.currentTimeMillis() + (long) (baseDuration * totalCost * 1000);
        forceUser.setForceRegenModifier(modifier);
        forceUser.setRegenDebuffExpiry(expiry);

        // Hunger
        int hungerLevel = debuffConfig.getInt("hunger-level", 1);
        if (hungerLevel > 0) {
            int duration = debuffConfig.getInt("hunger-duration-seconds", 20) * 20;
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, duration, hungerLevel - 1));
        }

        // Fatigue
        int fatigueLevel = debuffConfig.getInt("fatigue-level", 1);
        if (fatigueLevel > 0) {
            int duration = debuffConfig.getInt("fatigue-duration-seconds", 20) * 20;
            PotionEffectType fatigueType = plugin.getVersionAdapter().getMiningFatigueEffectType();
            player.addPotionEffect(new PotionEffect(fatigueType, duration, fatigueLevel - 1));
        }
    }

    // TODO: Implement the enchanting logic
    // TODO: Implement energy costs and debuffs
}
