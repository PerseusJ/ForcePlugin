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

    public GUIManager(AbilityManager abilityManager, ForceUserManager userManager) {
        this.abilityManager = abilityManager;
        this.userManager = userManager;
    }

    public void openAbilityGUI(Player player) {
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, ABILITY_GUI_TITLE);

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        ItemStack infoBook = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoBook.getItemMeta();
        infoMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "How to Use");
        infoMeta.setLore(List.of(
                ChatColor.GRAY + "1. " + ChatColor.WHITE + "Left-click an ability to select it.",
                ChatColor.GRAY + "2. " + ChatColor.WHITE + "Left-click an empty slot below to bind it.",
                "",
                ChatColor.GRAY + "To unbind an ability, " + ChatColor.YELLOW + "right-click",
                ChatColor.GRAY + "it in one of the slots below."
        ));
        infoBook.setItemMeta(infoMeta);
        gui.setItem(4, infoBook);

        // --- MODIFIED: Get all abilities in one list and place them together ---
        List<Ability> availableAbilities = new ArrayList<>(abilityManager.getAbilitiesBySide(forceUser.getSide()));

        // Define the slots where ability icons will be placed.
        int[] abilitySlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i = 0; i < availableAbilities.size() && i < abilitySlots.length; i++) {
            gui.setItem(abilitySlots[i], createAbilityIcon(availableAbilities.get(i), forceUser.getSide()));
        }
        // --- END MODIFIED ---

        // --- MODIFIED: Use colored glass panes for empty binding slots ---
        int[] bindingSlots = {40, 41, 42};
        Material[] slotColors = {Material.BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE};

        for (int i = 0; i < bindingSlots.length; i++) {
            int slotNum = i + 1;
            String boundAbilityId = forceUser.getBoundAbility(slotNum);
            ItemStack slotIcon;
            if (boundAbilityId != null) {
                Ability boundAbility = abilityManager.getAbility(boundAbilityId);
                slotIcon = createAbilityIcon(boundAbility, forceUser.getSide());
            } else {
                // Use a different colored pane for each empty slot.
                slotIcon = new ItemStack(slotColors[i]);
                ItemMeta meta = slotIcon.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "Binding Slot " + slotNum);
                meta.setLore(List.of(ChatColor.GRAY + "Click an ability above,", ChatColor.GRAY + "then click here to bind it."));
                slotIcon.setItemMeta(meta);
            }
            gui.setItem(bindingSlots[i], slotIcon);
        }
        // --- END MODIFIED ---

        player.openInventory(gui);
    }

    private ItemStack createAbilityIcon(Ability ability, ForceSide viewerSide) {
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
        meta.setLore(lore);

        icon.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        icon.setItemMeta(meta);
        return icon;
    }
}