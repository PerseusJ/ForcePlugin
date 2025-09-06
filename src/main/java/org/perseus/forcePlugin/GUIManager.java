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
    private final AbilityManager abilityManager;
    private final ForceUserManager userManager;
    private final LevelingManager levelingManager; // --- NEW ---

    public GUIManager(AbilityManager abilityManager, ForceUserManager userManager, LevelingManager levelingManager) {
        this.abilityManager = abilityManager;
        this.userManager = userManager;
        this.levelingManager = levelingManager; // --- NEW ---
    }

    public void openAbilityGUI(Player player) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, ABILITY_GUI_TITLE);

        // --- Filler Item ---
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        // --- NEW: Player Status Item ---
        ItemStack statusItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Status");
        statusMeta.setLore(List.of(
                ChatColor.GRAY + "Level: " + ChatColor.WHITE + forceUser.getForceLevel(),
                ChatColor.GRAY + "Points Available: " + ChatColor.GREEN + forceUser.getForcePoints(),
                "",
                ChatColor.DARK_AQUA + "Use points to unlock new abilities!"
        ));
        gui.setItem(4, statusItem);

        // --- Place Ability Icons (Locked or Unlocked) ---
        List<Ability> availableAbilities = new ArrayList<>(abilityManager.getAbilitiesBySide(forceUser.getSide()));
        int[] abilitySlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        for (int i = 0; i < availableAbilities.size() && i < abilitySlots.length; i++) {
            Ability ability = availableAbilities.get(i);
            // Check if the player has unlocked the ability.
            if (forceUser.hasUnlockedAbility(ability.getID())) {
                gui.setItem(abilitySlots[i], createUnlockedAbilityIcon(ability, forceUser.getSide()));
            } else {
                gui.setItem(abilitySlots[i], createLockedAbilityIcon(ability));
            }
        }

        // --- Place Binding Slots ---
        int[] bindingSlots = {48, 49, 50}; // Moved to the bottom row
        Material[] slotColors = {Material.BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE};
        for (int i = 0; i < bindingSlots.length; i++) {
            int slotNum = i + 1;
            String boundAbilityId = forceUser.getBoundAbility(slotNum);
            ItemStack slotIcon;
            if (boundAbilityId != null) {
                Ability boundAbility = abilityManager.getAbility(boundAbilityId);
                slotIcon = createUnlockedAbilityIcon(boundAbility, forceUser.getSide());
            } else {
                slotIcon = new ItemStack(slotColors[i]);
                ItemMeta meta = slotIcon.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "Binding Slot " + slotNum);
                meta.setLore(List.of(
                        ChatColor.GRAY + "Select an unlocked ability,",
                        ChatColor.GRAY + "then click here to bind it."
                ));
                slotIcon.setItemMeta(meta);
            }
            gui.setItem(bindingSlots[i], slotIcon);
        }
        player.openInventory(gui);
    }

    private ItemStack createUnlockedAbilityIcon(Ability ability, ForceSide viewerSide) {
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
        meta.setDisplayName(nameColor + "" + ChatColor.BOLD + ability.getName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ability.getDescription());
        lore.add("");
        lore.add(ChatColor.BLUE + "Energy Cost: " + ChatColor.WHITE + ability.getEnergyCost());
        lore.add(ChatColor.GREEN + "Cooldown: " + ChatColor.WHITE + ability.getCooldown() + "s");
        lore.add("");
        lore.add(ChatColor.GOLD + "Unlocked");
        meta.setLore(lore);

        icon.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        icon.setItemMeta(meta);
        return icon;
    }

    // --- NEW METHOD for creating locked icons ---
    private ItemStack createLockedAbilityIcon(Ability ability) {
        ItemStack icon = new ItemStack(Material.BARRIER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + ability.getName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ability.getDescription());
        lore.add("");
        lore.add(ChatColor.RED + "Locked");
        lore.add(ChatColor.GREEN + "Cost: 1 Force Point");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to unlock!");
        meta.setLore(lore);

        icon.setItemMeta(meta);
        return icon;
    }
}