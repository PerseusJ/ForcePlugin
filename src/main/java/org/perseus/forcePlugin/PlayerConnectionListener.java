package org.perseus.forcePlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final ForceUserManager userManager;
    private final ForceBarManager barManager;
    private final ForcePlugin plugin; // We need the main plugin instance to schedule a task

    public PlayerConnectionListener(ForceUserManager userManager, ForceBarManager barManager, ForcePlugin plugin) {
        this.userManager = userManager;
        this.barManager = barManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load the player's data immediately as before.
        userManager.loadPlayerData(player);

        // --- THE FIX: Delay adding the bar by one server tick ---
        // This ensures that all data is fully loaded and available before we try to create the bar.
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            barManager.addPlayer(player);
        }, 1L); // 1L = 1 tick delay
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        userManager.savePlayerData(event.getPlayer());
        barManager.removePlayer(event.getPlayer());
        userManager.removePlayerFromCache(event.getPlayer());
    }
}