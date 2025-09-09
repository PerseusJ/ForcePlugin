package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.abilities.Ability;

public class GUIListener implements Listener {

    private final ForcePlugin plugin;

    public GUIListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String viewTitle = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        if (viewTitle.equals(GUIManager.ABILITY_GUI_TITLE)) {
            handleAbilitySelection(event, player);
        } else if (viewTitle.startsWith(GUIManager.UPGRADE_GUI_TITLE_PREFIX)) {
            handleUpgradeMenu(event, player);
        }
    }

    private void handleAbilitySelection(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta().getDisplayName().equals(" ")) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).split(" - ")[0];
        Ability clickedAbility = findAbilityByName(forceUser, itemName);

        if (clickedAbility != null) {
            if (forceUser.hasUnlockedAbility(clickedAbility.getID())) {
                plugin.getGuiManager().openUpgradeGUI(player, clickedAbility);
            } else {
                int unlockCost = plugin.getConfig().getInt("abilities." + clickedAbility.getID() + ".unlock-cost", 1);
                if (forceUser.getForcePoints() >= unlockCost) {
                    forceUser.addForcePoints(-unlockCost);
                    forceUser.unlockAbility(clickedAbility.getID());
                    player.sendMessage(ChatColor.AQUA + "You have unlocked " + clickedAbility.getName() + "!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
                    plugin.getGuiManager().openAbilityGUI(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough Force Points! (Requires " + unlockCost + ")");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                }
            }
        }
    }

    private void handleUpgradeMenu(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String viewTitle = event.getView().getTitle();
        String abilityName = viewTitle.replace(GUIManager.UPGRADE_GUI_TITLE_PREFIX, "");
        Ability ability = findAbilityByName(forceUser, abilityName);
        if (ability == null) return;

        Material clickedType = clickedItem.getType();

        if (clickedType == Material.EMERALD_BLOCK) {
            if (forceUser.getForcePoints() > 0) {
                forceUser.addForcePoints(-1);
                forceUser.upgradeAbility(ability.getID());
                player.sendMessage(ChatColor.AQUA + "Upgraded " + ability.getName() + " to Level " + forceUser.getAbilityLevel(ability.getID()) + "!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.5f);
                plugin.getGuiManager().openUpgradeGUI(player, ability);
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough Force Points to upgrade this.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        } else if (clickedType == Material.BARRIER) {
            plugin.getGuiManager().openAbilityGUI(player);
        } else if (clickedType == Material.BLUE_WOOL || clickedType == Material.YELLOW_WOOL || clickedType == Material.RED_WOOL) {
            // This logic is now obsolete with the Holocron system, but we can leave it for now.
            // A better implementation would be to remove these buttons from the upgrade GUI.
            player.sendMessage(ChatColor.YELLOW + "Select your active ability by holding your Holocron and using Shift + Scroll.");
        }
    }

    private Ability findAbilityByName(ForceUser user, String name) {
        for (Ability ability : plugin.getAbilityManager().getAbilitiesBySide(user.getSide())) {
            if (ability.getName().equalsIgnoreCase(name.trim())) {
                return ability;
            }
        }
        return null;
    }
}