package org.perseus.forcePlugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceEnchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ForceEnchantGUI {
    public static final String GUI_TITLE = "Force Enchanting";
    private final ForcePlugin plugin;

    public ForceEnchantGUI(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Map<Enchantment, Integer> selections = plugin.getForceEnchantManager().getPlayerSelections(player);

        // --- Create GUI Boundary ---
        ItemStack boundary = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta boundaryMeta = boundary.getItemMeta();
        boundaryMeta.setDisplayName(" ");
        boundary.setItemMeta(boundaryMeta);
        for (int i = 0; i < gui.getSize(); i++) {
            if (i < 10 || i > 43 || i % 9 == 0 || i % 9 == 8) {
                gui.setItem(i, boundary);
            }
        }

        // --- Item to be enchanted ---
        gui.setItem(4, itemInHand);

        // --- Control Buttons ---
        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Confirm Enchantments");
        int totalCost = calculateTotalCost(itemInHand, selections);
        List<String> confirmLore = new ArrayList<>();
        confirmLore.add(ChatColor.GRAY + "Total Energy Cost: " + ChatColor.AQUA + totalCost);
        confirmMeta.setLore(confirmLore);
        confirm.setItemMeta(confirmMeta);
        gui.setItem(49, confirm);

        // --- Enchantment Icons ---
        List<ForceEnchantment> applicableEnchantments = plugin.getForceEnchantManager().getApplicableEnchantments(itemInHand);
        int slot = 10;
        for (ForceEnchantment forceEnchant : applicableEnchantments) {
            if (slot > 43 || (slot + 1) % 9 == 0 || slot % 9 == 0) {
                continue; // Should not happen with 54 size GUI, but good practice
            }

            ItemStack enchantIcon = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = enchantIcon.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + getFriendlyEnchantmentName(forceEnchant.getEnchantment()));

            int currentLevel = itemInHand.getEnchantmentLevel(forceEnchant.getEnchantment());
            int selectedLevel = selections.getOrDefault(forceEnchant.getEnchantment(), currentLevel);
            int nextLevel = selectedLevel + 1;
            int maxLevel = forceEnchant.getMaxLevel();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Current Level: " + ChatColor.YELLOW + currentLevel);

            if (selectedLevel > currentLevel) {
                enchantIcon.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                lore.add(ChatColor.GREEN + "Selected Level: " + ChatColor.YELLOW + selectedLevel);
            }

            if (nextLevel > maxLevel) {
                lore.add(ChatColor.RED + "Max level reached!");
            } else {
                lore.add(ChatColor.GRAY + "Upgrade to Level: " + ChatColor.YELLOW + nextLevel);
                lore.add(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + (forceEnchant.getEnergyCostPerLevel() * nextLevel) + " Energy");
            }
            lore.add(" ");
            if (selectedLevel > currentLevel) {
                lore.add(ChatColor.YELLOW + "Click to remove from selection.");
            } else {
                lore.add(ChatColor.DARK_AQUA + "Click to add to selection.");
            }
            meta.setLore(lore);
            enchantIcon.setItemMeta(meta);

            gui.setItem(slot++, enchantIcon);
        }

        player.openInventory(gui);
    }

    private int calculateTotalCost(ItemStack item, Map<Enchantment, Integer> selections) {
        int totalCost = 0;
        for (Map.Entry<Enchantment, Integer> entry : selections.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int selectedLevel = entry.getValue();
            int currentLevel = item.getEnchantmentLevel(enchantment);

            if (selectedLevel > currentLevel) {
                ForceEnchantment fe = plugin.getForceEnchantManager().getEnchantmentData().get(enchantment);
                if (fe != null) {
                    // Cost is incremental for each level gained
                    for (int i = currentLevel + 1; i <= selectedLevel; i++) {
                        totalCost += fe.getEnergyCostPerLevel() * i;
                    }
                }
            }
        }
        return totalCost;
    }

    public String getFriendlyEnchantmentName(Enchantment enchantment) {
        String name = enchantment.getKey().getKey();
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }
        return String.join(" ", words);
    }
}
