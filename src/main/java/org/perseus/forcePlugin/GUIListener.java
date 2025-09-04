package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
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
            return; // Ignore clicks on filler items
        }

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        int clickedSlot = event.getRawSlot();

        // --- MODIFIED: Check for binding slots in their new positions ---
        if (clickedSlot >= 40 && clickedSlot <= 42) {
            int bindingSlot = clickedSlot - 39; // 40->1, 41->2, 42->3

            if (event.isLeftClick()) {
                String selectedId = selectedAbility.get(player.getUniqueId());
                if (selectedId == null) {
                    player.sendMessage(ChatColor.YELLOW + "Please select an ability from the top section first.");
                    return;
                }
                forceUser.setBoundAbility(bindingSlot, selectedId);
                Ability boundAbility = plugin.getAbilityManager().getAbility(selectedId);
                selectedAbility.remove(player.getUniqueId());
                player.sendMessage(ChatColor.AQUA + "Successfully bound " + boundAbility.getName() + " to slot " + bindingSlot + ".");
                plugin.getGuiManager().openAbilityGUI(player);
            } else if (event.isRightClick()) {
                String currentlyBoundId = forceUser.getBoundAbility(bindingSlot);
                if (currentlyBoundId == null) {
                    player.sendMessage(ChatColor.YELLOW + "This slot is already empty.");
                    return;
                }
                Ability unboundAbility = plugin.getAbilityManager().getAbility(currentlyBoundId);
                forceUser.setBoundAbility(bindingSlot, null);
                selectedAbility.remove(player.getUniqueId());
                player.sendMessage(ChatColor.YELLOW + "Successfully unbound " + unboundAbility.getName() + " from slot " + bindingSlot + ".");
                plugin.getGuiManager().openAbilityGUI(player);
            }
        }
        // --- MODIFIED: Any other valid item click is an ability selection ---
        else {
            String abilityName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            // Find the ability that corresponds to the clicked icon.
            for (Ability ability : plugin.getAbilityManager().getAbilitiesBySide(forceUser.getSide())) {
                if (ability.getName().equalsIgnoreCase(abilityName)) {
                    selectedAbility.put(player.getUniqueId(), ability.getID());
                    player.sendMessage(ChatColor.GREEN + "Selected " + ability.getName() + ". Now click a binding slot.");
                    return;
                }
            }
        }
    }
}