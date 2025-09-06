package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.abilities.Ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {

    private final ForcePlugin plugin;
    private final Map<UUID, String> selectedAbility = new HashMap<>();

    public GUIListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUIManager.ABILITY_GUI_TITLE)) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta().getDisplayName().equals(" ")) {
            return;
        }

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        Ability clickedAbility = findAbilityByName(forceUser, itemName);

        // Handle clicks on ability icons
        if (clickedAbility != null) {
            if (forceUser.hasUnlockedAbility(clickedAbility.getID())) {
                // Action: Select an unlocked ability
                selectedAbility.put(player.getUniqueId(), clickedAbility.getID());
                player.sendMessage(ChatColor.GREEN + "Selected " + clickedAbility.getName() + ". Now click a binding slot.");
                // --- NEW: Sound Effect ---
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.5f);
            } else {
                // Action: Try to unlock a locked ability
                if (forceUser.getForcePoints() > 0) {
                    forceUser.addForcePoints(-1);
                    forceUser.unlockAbility(clickedAbility.getID());
                    player.sendMessage(ChatColor.AQUA + "You have unlocked " + clickedAbility.getName() + "!");
                    // --- NEW: Sound Effect ---
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
                    plugin.getGuiManager().openAbilityGUI(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough Force Points to unlock this.");
                    // --- NEW: Sound Effect ---
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                }
            }
            return;
        }

        // Handle clicks on binding slots
        int clickedSlot = event.getRawSlot();
        if (clickedSlot >= 48 && clickedSlot <= 50) {
            int bindingSlot = clickedSlot - 47;

            if (event.isLeftClick()) {
                // Action: Bind a selected ability
                String selectedId = selectedAbility.get(player.getUniqueId());
                if (selectedId == null) {
                    player.sendMessage(ChatColor.YELLOW + "Please select an unlocked ability first.");
                    return;
                }
                forceUser.setBoundAbility(bindingSlot, selectedId);
                player.sendMessage(ChatColor.AQUA + "Ability bound to slot " + bindingSlot + ".");
                // --- NEW: Sound Effect ---
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);
                selectedAbility.remove(player.getUniqueId());
                plugin.getGuiManager().openAbilityGUI(player);
            } else if (event.isRightClick()) {
                // Action: Unbind an ability
                if (forceUser.getBoundAbility(bindingSlot) != null) {
                    forceUser.setBoundAbility(bindingSlot, null);
                    player.sendMessage(ChatColor.YELLOW + "Ability unbound from slot " + bindingSlot + ".");
                    // --- NEW: Sound Effect ---
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.8f);
                    plugin.getGuiManager().openAbilityGUI(player);
                }
            }
        }
    }

    private Ability findAbilityByName(ForceUser user, String name) {
        for (Ability ability : plugin.getAbilityManager().getAbilitiesBySide(user.getSide())) {
            if (ability.getName().equalsIgnoreCase(name)) {
                return ability;
            }
        }
        return null;
    }
}