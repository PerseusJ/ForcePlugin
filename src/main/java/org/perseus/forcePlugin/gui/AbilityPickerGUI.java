package org.perseus.forcePlugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.versioning.VersionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbilityPickerGUI {

    public static final String TITLE_PREFIX = "Bind Slot ";

    private final ForcePlugin plugin;

    public AbilityPickerGUI(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, int slot) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, TITLE_PREFIX + (slot + 1));

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        List<Ability> unlockedAbilities = new ArrayList<>();
        for (String abilityId : forceUser.getUnlockedAbilities().keySet()) {
            Ability ability = plugin.getAbilityManager().getAbility(abilityId);
            if (ability != null) {
                unlockedAbilities.add(ability);
            }
        }

        int[] abilitySlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (int i = 0; i < unlockedAbilities.size() && i < abilitySlots.length; i++) {
            Ability ability = unlockedAbilities.get(i);
            int level = forceUser.getAbilityLevel(ability.getID());

            ForceSide abilitySide = ability.getSide() == ForceSide.NONE ? forceUser.getSide() : ability.getSide();
            Material iconMaterial = (abilitySide == ForceSide.LIGHT) ? Material.NETHER_STAR : Material.REDSTONE;
            ChatColor nameColor = (abilitySide == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;

            String currentBind = forceUser.getBoundAbilityId(slot);
            boolean isBound = ability.getID().equals(currentBind);

            ItemStack icon = new ItemStack(iconMaterial);
            ItemMeta meta = icon.getItemMeta();
            if (isBound) {
                meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + ability.getName() + " " + ChatColor.GRAY + "(Currently Bound)");
            } else {
                meta.setDisplayName(nameColor + "" + ChatColor.BOLD + ability.getName());
            }

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + ability.getDescription());
            lore.add("");
            lore.add(ChatColor.BLUE + "Level: " + ChatColor.WHITE + level);
            lore.add("");
            if (isBound) {
                lore.add(ChatColor.GREEN + "Already bound to this slot");
            } else {
                lore.add(ChatColor.YELLOW + "Click to bind to slot " + (slot + 1));
            }

            meta.setLore(lore);
            meta.addEnchant(VersionUtil.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            icon.setItemMeta(meta);

            gui.setItem(abilitySlots[i], icon);
        }

        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Binding GUI");
        backButton.setItemMeta(backMeta);
        gui.setItem(49, backButton);

        player.openInventory(gui);
    }
}
