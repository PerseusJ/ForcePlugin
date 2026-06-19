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

import java.util.Arrays;

public class BindGUI {

    public static final String TITLE = "Hotbar Binding - Click a slot to bind";

    private final ForcePlugin plugin;

    public BindGUI(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        Inventory gui = Bukkit.createInventory(null, 27, TITLE);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 9; i < 18; i++) {
            gui.setItem(i, filler);
        }

        for (int slot = 0; slot < 9; slot++) {
            String abilityId = forceUser.getBoundAbilityId(slot);
            ItemStack slotItem;

            if (abilityId != null) {
                Ability ability = plugin.getAbilityManager().getAbility(abilityId);
                if (ability != null) {
                    ForceSide side = ability.getSide() == ForceSide.NONE ? forceUser.getSide() : ability.getSide();
                    Material iconMaterial = (side == ForceSide.LIGHT) ? Material.NETHER_STAR : Material.REDSTONE;
                    ChatColor nameColor = (side == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;

                    slotItem = new ItemStack(iconMaterial);
                    ItemMeta meta = slotItem.getItemMeta();
                    meta.setDisplayName(nameColor + "" + ChatColor.BOLD + ability.getName());
                    int level = forceUser.getAbilityLevel(abilityId);
                    meta.setLore(Arrays.asList(
                            ChatColor.GRAY + "Slot " + (slot + 1),
                            ChatColor.GRAY + "Level: " + ChatColor.WHITE + level,
                            "",
                            ChatColor.YELLOW + "Click to change"
                    ));
                    meta.addEnchant(VersionUtil.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    slotItem.setItemMeta(meta);
                } else {
                    slotItem = createEmptySlot(slot);
                }
            } else {
                slotItem = createEmptySlot(slot);
            }

            gui.setItem(slot, slotItem);
        }

        ItemStack resetSlot = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta resetMeta = resetSlot.getItemMeta();
        resetMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Reset Current Slot");
        resetMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unbinds the ability from your current hotbar slot."));
        resetSlot.setItemMeta(resetMeta);
        gui.setItem(22, resetSlot);

        ItemStack unbindAll = new ItemStack(Material.BARRIER);
        ItemMeta unbindMeta = unbindAll.getItemMeta();
        unbindMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Unbind All");
        unbindMeta.setLore(Arrays.asList(ChatColor.GRAY + "Clears all hotbar bindings."));
        unbindAll.setItemMeta(unbindMeta);
        gui.setItem(23, unbindAll);

        ItemStack close = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close");
        close.setItemMeta(closeMeta);
        gui.setItem(26, close);

        player.openInventory(gui);
    }

    private ItemStack createEmptySlot(int slot) {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Empty Slot " + (slot + 1));
        meta.setLore(Arrays.asList(ChatColor.YELLOW + "Click to bind an ability"));
        pane.setItemMeta(meta);
        return pane;
    }
}
