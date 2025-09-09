package org.perseus.forcePlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.abilities.Ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HolocronListener implements Listener {

    private final ForcePlugin plugin;
    private final Map<UUID, Integer> selectingPlayers = new HashMap<>();

    public HolocronListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    // --- Main Control Logic ---

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!plugin.getHolocronManager().isHolocron(itemInHand)) return;

        if (event.isSneaking()) {
            selectingPlayers.put(player.getUniqueId(), 0);
            showSelectedAbility(player);
        } else {
            selectingPlayers.remove(player.getUniqueId());
            plugin.getHolocronManager().updateHolocronName(player, itemInHand);
            sendActionBarMessage(player, "");
        }
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!selectingPlayers.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);

        int oldSlot = event.getPreviousSlot();
        int newSlot = event.getNewSlot();
        int direction = 0;
        if (oldSlot == 8 && newSlot == 0) direction = 1;
        else if (oldSlot == 0 && newSlot == 8) direction = -1;
        else if (newSlot > oldSlot) direction = 1;
        else if (newSlot < oldSlot) direction = -1;

        if (direction != 0) {
            cycleAbility(player, direction);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().isRightClick()) return;
        if (!plugin.getHolocronManager().isHolocron(event.getItem())) return;
        plugin.getGuiManager().openAbilityGUI(player);
    }

    // --- Holocron Protection Logic ---

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getHolocronManager().isHolocron(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop a Force Artifact.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        // Check if the player is trying to move the Holocron
        if (plugin.getHolocronManager().isHolocron(clickedItem) || plugin.getHolocronManager().isHolocron(cursorItem)) {
            // Allow them to move it within their own inventory (but not hotbar <-> main inv)
            if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
                // Prevent moving it out of the hotbar if it's there
                if (event.getSlot() < 9 && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                }
                // Allow normal clicks within the main inventory space
                return;
            }
            // If the click is in any other inventory (chest, ender chest, etc.), cancel it.
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatColor.RED + "You cannot store a Force Artifact here.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Find the Holocron in the list of items to be dropped
        ItemStack holocron = null;
        for (ItemStack item : event.getDrops()) {
            if (plugin.getHolocronManager().isHolocron(item)) {
                holocron = item;
                break;
            }
        }

        if (holocron != null) {
            // Remove it from the items that drop on the ground
            event.getDrops().remove(holocron);
            // Add it to the list of items the player will keep after respawning
            event.getItemsToKeep().add(holocron);
        }
    }


    // --- Helper Methods ---

    private void cycleAbility(Player player, int direction) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        List<String> unlocked = new ArrayList<>(forceUser.getUnlockedAbilities().keySet());
        if (unlocked.isEmpty()) return;

        int currentIndex = unlocked.indexOf(forceUser.getActiveAbilityId());
        if (currentIndex == -1) currentIndex = 0; // Default to first if not found

        currentIndex += direction;

        if (currentIndex >= unlocked.size()) currentIndex = 0;
        if (currentIndex < 0) currentIndex = unlocked.size() - 1;

        forceUser.setActiveAbilityId(unlocked.get(currentIndex));
        showSelectedAbility(player);
    }

    private void showSelectedAbility(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        Ability activeAbility = plugin.getAbilityManager().getAbility(forceUser.getActiveAbilityId());
        if (activeAbility != null) {
            ChatColor color = (forceUser.getSide() == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;
            sendActionBarMessage(player, color + "" + ChatColor.BOLD + "Selected: " + activeAbility.getName());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.5f);
        }
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}