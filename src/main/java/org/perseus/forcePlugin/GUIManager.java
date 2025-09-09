package org.perseus.forcePlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.perseus.forcePlugin.abilities.Ability;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    public static final String ABILITY_GUI_TITLE = "Select Your Abilities";
    public static final String UPGRADE_GUI_TITLE_PREFIX = "Manage: ";

    private final AbilityManager abilityManager;
    private final ForceUserManager userManager;
    private final AbilityConfigManager configManager;

    public GUIManager(AbilityManager abilityManager, ForceUserManager userManager, AbilityConfigManager configManager) {
        this.abilityManager = abilityManager;
        this.userManager = userManager;
        this.configManager = configManager;
    }

    public void openAbilityGUI(Player player) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, ABILITY_GUI_TITLE);

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) { gui.setItem(i, filler); }

        ItemStack statusItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Status");
        statusMeta.setLore(List.of(
                ChatColor.GRAY + "Level: " + ChatColor.WHITE + forceUser.getForceLevel(),
                ChatColor.GRAY + "Points Available: " + ChatColor.GREEN + forceUser.getForcePoints()
        ));
        gui.setItem(4, statusItem);

        List<Ability> availableAbilities = new ArrayList<>(abilityManager.getAbilitiesBySide(forceUser.getSide()));
        int[] abilitySlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        for (int i = 0; i < availableAbilities.size() && i < abilitySlots.length; i++) {
            Ability ability = availableAbilities.get(i);
            if (forceUser.hasUnlockedAbility(ability.getID())) {
                gui.setItem(abilitySlots[i], createUnlockedAbilityIcon(ability, forceUser));
            } else {
                gui.setItem(abilitySlots[i], createLockedAbilityIcon(ability));
            }
        }
        player.openInventory(gui);
    }

    public void openUpgradeGUI(Player player, Ability ability) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 27, UPGRADE_GUI_TITLE_PREFIX + ability.getName());
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) { gui.setItem(i, filler); }

        int currentLevel = forceUser.getAbilityLevel(ability.getID());
        int maxLevel = configManager.getMaxLevel(ability.getID());

        gui.setItem(11, createLevelIcon(ability, currentLevel, "Current Level"));

        if (currentLevel < maxLevel) {
            gui.setItem(15, createLevelIcon(ability, currentLevel + 1, "Next Level"));
            ItemStack upgradeButton = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta upgradeMeta = upgradeButton.getItemMeta();
            upgradeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Upgrade Ability");
            upgradeMeta.setLore(List.of(ChatColor.GRAY + "Cost: " + ChatColor.GREEN + "1 Force Point"));
            upgradeButton.setItemMeta(upgradeMeta);
            gui.setItem(13, upgradeButton);
        } else {
            ItemStack maxLevelItem = new ItemStack(Material.DIAMOND_BLOCK);
            ItemMeta maxMeta = maxLevelItem.getItemMeta();
            maxMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Max Level Reached");
            maxLevelItem.setItemMeta(maxMeta);
            gui.setItem(13, maxLevelItem);
        }

        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Skill Tree");
        backButton.setItemMeta(backMeta);
        gui.setItem(26, backButton);

        player.openInventory(gui);
    }

    private ItemStack createUnlockedAbilityIcon(Ability ability, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(ability.getID());
        int maxLevel = configManager.getMaxLevel(ability.getID());
        ForceSide viewerSide = forceUser.getSide();

        Material iconMaterial;
        ChatColor nameColor;
        ForceSide abilitySide = ability.getSide() == ForceSide.NONE ? viewerSide : ability.getSide();
        if (abilitySide == ForceSide.LIGHT) {
            iconMaterial = Material.NETHER_STAR;
            nameColor = ChatColor.AQUA;
        } else {
            iconMaterial = Material.REDSTONE;
            nameColor = ChatColor.RED;
        }

        ItemStack icon = new ItemStack(iconMaterial);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(nameColor + "" + ChatColor.BOLD + ability.getName() + " - Level " + level);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ability.getDescription());
        lore.add("");
        lore.add(ChatColor.BLUE + "Energy Cost: " + ChatColor.WHITE + ability.getEnergyCost(level));
        lore.add(ChatColor.GREEN + "Cooldown: " + ChatColor.WHITE + ability.getCooldown(level) + "s");
        lore.add("");
        if (level < maxLevel) {
            lore.add(ChatColor.YELLOW + "Click to Manage/Upgrade!");
        } else {
            lore.add(ChatColor.AQUA + "Max Level | Click to Manage");
        }
        meta.setLore(lore);

        icon.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack createLockedAbilityIcon(Ability ability) {
        ItemStack icon = new ItemStack(Material.BARRIER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + ability.getName());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ability.getDescription());
        lore.add("");
        lore.add(ChatColor.RED + "Locked");
        lore.add(ChatColor.GREEN + "Cost: " + configManager.getIntValue(ability.getID(), 1, "unlock-cost", 1) + " Force Point(s)");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to unlock!");
        meta.setLore(lore);
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack createLevelIcon(Ability ability, int level, String title) {
        ItemStack icon = new ItemStack(Material.PAPER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + title + ": " + ChatColor.WHITE + level);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Energy Cost: " + ChatColor.WHITE + ability.getEnergyCost(level));
        lore.add(ChatColor.GREEN + "Cooldown: " + ChatColor.WHITE + ability.getCooldown(level) + "s");
        meta.setLore(lore);
        icon.setItemMeta(meta);
        return icon;
    }
}