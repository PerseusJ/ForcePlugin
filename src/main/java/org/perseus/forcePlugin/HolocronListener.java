package org.perseus.forcePlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    // Tracks players who are in "Ability Selection Mode"
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
            // Enter Selection Mode
            selectingPlayers.put(player.getUniqueId(), 0); // Start at index 0
            showSelectedAbility(player);
        } else {
            // Exit Selection Mode and confirm selection
            selectingPlayers.remove(player.getUniqueId());
            plugin.getHolocronManager().updateHolocronName(player, itemInHand);
            sendActionBarMessage(player, ""); // Clear the action bar
        }
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        // Check if the player is in selection mode
        if (!selectingPlayers.containsKey(player.getUniqueId())) return;

        event.setCancelled(true); // Prevent the hotbar from actually changing

        // Determine scroll direction
        int oldSlot = event.getPreviousSlot();
        int newSlot = event.getNewSlot();
        int direction = 0;
        if (oldSlot == 8 && newSlot == 0) direction = 1; // Scroll forward
        else if (oldSlot == 0 && newSlot == 8) direction = -1; // Scroll backward
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

        // Right-clicking the Holocron opens the GUI
        plugin.getGuiManager().openAbilityGUI(player);
    }

    // --- Holocron Protection Logic ---

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getHolocronManager().isHolocron(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop a Holocron.");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (plugin.getHolocronManager().isHolocron(event.getCurrentItem())) {
            // Allow moving it within the player's inventory, but not into other inventories
            if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ChatColor.RED + "You cannot store a Holocron here.");
            }
        }
    }

    // --- Helper Methods ---

    private void cycleAbility(Player player, int direction) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        List<String> unlocked = new ArrayList<>(forceUser.getUnlockedAbilities().keySet());
        if (unlocked.isEmpty()) return;

        int currentIndex = selectingPlayers.getOrDefault(player.getUniqueId(), 0);
        currentIndex += direction;

        // Loop around the list
        if (currentIndex >= unlocked.size()) currentIndex = 0;
        if (currentIndex < 0) currentIndex = unlocked.size() - 1;

        selectingPlayers.put(player.getUniqueId(), currentIndex);
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