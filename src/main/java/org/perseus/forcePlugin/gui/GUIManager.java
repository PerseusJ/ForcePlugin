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
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.PassiveAbility;
import org.perseus.forcePlugin.data.Rank;
import org.perseus.forcePlugin.managers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIManager {

    public static final String ABILITY_GUI_TITLE = "Select Your Abilities";
    public static final String UPGRADE_GUI_TITLE_PREFIX = "Manage: ";
    public static final String SPECIALIZATION_GUI_TITLE = "Choose Your Final Path";
    public static final String PASSIVE_GUI_TITLE_PREFIX = "Passive Tree: ";

    private final AbilityManager abilityManager;
    private final ForceUserManager userManager;
    private final AbilityConfigManager configManager;
    private final RankManager rankManager;
    private final PassiveManager passiveManager;

    public GUIManager(AbilityManager abilityManager, ForceUserManager userManager, AbilityConfigManager configManager, RankManager rankManager, PassiveManager passiveManager) {
        this.abilityManager = abilityManager;
        this.userManager = userManager;
        this.configManager = configManager;
        this.rankManager = rankManager;
        this.passiveManager = passiveManager;
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
        statusMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level: " + ChatColor.WHITE + forceUser.getForceLevel(),
                ChatColor.GRAY + "Points Available: " + ChatColor.GREEN + forceUser.getForcePoints()
        ));
        gui.setItem(4, statusItem);

        // Add Passive Tree Button if specialized
        if (forceUser.getSpecialization() != null) {
            ItemStack passiveButton = new ItemStack(Material.NETHER_STAR);
            ItemMeta passiveMeta = passiveButton.getItemMeta();
            passiveMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "View Passive Tree");
            passiveMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to view and upgrade your", ChatColor.GRAY + "specialization passives."));
            passiveButton.setItemMeta(passiveMeta);
            gui.setItem(49, passiveButton); // Center of bottom row
        }

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

        if (forceUser.getSpecialization() != null) {
            String ultimateId = getUltimateForSpec(forceUser.getSpecialization());
            if (ultimateId != null && forceUser.hasUnlockedAbility(ultimateId)) {
                Ability ultimate = abilityManager.getAbility(ultimateId);
                if (ultimate != null) {
                    gui.setItem(40, createUnlockedAbilityIcon(ultimate, forceUser));
                }
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
            int upgradeCost = configManager.getIntValue(ability.getID(), currentLevel, "upgrade-cost", 1);
            String pointString = (upgradeCost == 1) ? " Force Point" : " Force Points";
            ItemStack upgradeButton = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta upgradeMeta = upgradeButton.getItemMeta();
            upgradeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Upgrade Ability");
            upgradeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + ChatColor.GREEN + upgradeCost + pointString));
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

    public void openSpecializationGUI(Player player) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || forceUser.getSide() == ForceSide.NONE) return;

        Inventory gui = Bukkit.createInventory(null, 27, SPECIALIZATION_GUI_TITLE);
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) { gui.setItem(i, filler); }

        List<Rank> specs = rankManager.getSpecializations(forceUser.getSide());
        int[] specSlots = {11, 13, 15};

        for (int i = 0; i < specs.size() && i < specSlots.length; i++) {
            Rank spec = specs.get(i);
            ItemStack icon = new ItemStack(spec.getMaterial());
            ItemMeta meta = icon.getItemMeta();
            meta.setDisplayName(spec.getDisplayName());
            meta.setLore(spec.getDescription());
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            icon.setItemMeta(meta);
            gui.setItem(specSlots[i], icon);
        }
        player.openInventory(gui);
    }

    public void openPassiveGUI(Player player) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || forceUser.getSpecialization() == null) return;

        String specName = rankManager.getRank(forceUser);
        Inventory gui = Bukkit.createInventory(null, 45, PASSIVE_GUI_TITLE_PREFIX + specName);

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) { gui.setItem(i, filler); }

        ItemStack statusItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Status");
        statusMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Points Available: " + ChatColor.GREEN + forceUser.getForcePoints()
        ));
        gui.setItem(4, statusItem);

        List<PassiveAbility> passives = passiveManager.getPassivesForSpec(forceUser.getSpecialization());
        int[] tier1Slots = {12, 13, 14};
        int[] tier2Slots = {21, 22, 23};
        int[] tier3Slots = {30, 31, 32};

        for (PassiveAbility passive : passives) {
            int slot = -1;
            if (passive.getTier() == 1) slot = getNextAvailableSlot(gui, tier1Slots);
            if (passive.getTier() == 2) slot = getNextAvailableSlot(gui, tier2Slots);
            if (passive.getTier() == 3) slot = getNextAvailableSlot(gui, tier3Slots);

            if (slot != -1) {
                gui.setItem(slot, createPassiveIcon(passive, forceUser));
            }
        }

        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Skill Tree");
        backButton.setItemMeta(backMeta);
        gui.setItem(40, backButton);

        player.openInventory(gui);
    }

    private ItemStack createPassiveIcon(PassiveAbility passive, ForceUser user) {
        int currentRank = user.getPassiveRank(passive.getId());
        int maxRank = passive.getMaxRank();
        int pointsSpentInTree = user.getPassiveRanks().values().stream().mapToInt(Integer::intValue).sum();

        ItemStack icon = new ItemStack(passive.getMaterial());
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(passive.getDisplayName() + " " + ChatColor.YELLOW + "[" + currentRank + "/" + maxRank + "]");

        List<String> lore = new ArrayList<>(passive.getDescription());
        lore.add("");
        if (currentRank > 0) {
            lore.add(ChatColor.GRAY + "Current Bonus: " + ChatColor.GREEN + passive.getValue(currentRank));
        }
        if (currentRank < maxRank) {
            lore.add(ChatColor.GRAY + "Next Rank Bonus: " + ChatColor.GREEN + passive.getValue(currentRank + 1));
            lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GREEN + "1 Force Point");
        } else {
            lore.add(ChatColor.AQUA + "Max Rank Reached");
        }
        lore.add("");

        int requiredPoints = (passive.getTier() - 1) * 5;
        if (pointsSpentInTree >= requiredPoints) {
            if (currentRank < maxRank) {
                lore.add(ChatColor.YELLOW + "Click to upgrade!");
            }
            icon.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            lore.add(ChatColor.RED + "Requires " + requiredPoints + " points spent in this tree.");
        }

        meta.setLore(lore);
        icon.setItemMeta(meta);
        return icon;
    }

    private int getNextAvailableSlot(Inventory gui, int[] slots) {
        for (int slot : slots) {
            if (gui.getItem(slot) == null || gui.getItem(slot).getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return slot;
            }
        }
        return -1;
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

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        icon.setItemMeta(meta);
        return icon;
    }

    private ItemStack createLockedAbilityIcon(Ability ability) {
        ItemStack icon = new ItemStack(Material.BARRIER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + ability.getName());
        int unlockCost = configManager.getUnlockCost(ability.getID());
        String pointString = (unlockCost == 1) ? " Force Point" : " Force Points";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ability.getDescription());
        lore.add("");
        lore.add(ChatColor.RED + "Locked");
        lore.add(ChatColor.GREEN + "Cost: " + unlockCost + pointString);
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

    private String getUltimateForSpec(String specId) {
        if (specId == null) return null;
        switch (specId) {
            case "GUARDIAN": return "FORCE_ABSORB";
            case "SENTINEL": return "FORCE_CAMOUFLAGE";
            case "CONSULAR": return "FORCE_SERENITY";
            case "WARRIOR": return "UNSTOPPABLE_VENGEANCE";
            case "INQUISITOR": return "MARK_OF_THE_HUNT";
            case "SORCERER": return "CHAIN_LIGHTNING";
            default: return null;
        }
    }
}