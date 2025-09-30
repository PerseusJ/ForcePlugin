package org.perseus.forcePlugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.managers.ForceBarManager;
import org.perseus.forcePlugin.managers.ForceUserManager;

public class PlayerConnectionListener implements Listener {

    private final ForcePlugin plugin;

    public PlayerConnectionListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ForceUserManager userManager = plugin.getForceUserManager();

        // Load data asynchronously. The callback will handle bar creation.
        userManager.loadPlayerData(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ForceUserManager userManager = plugin.getForceUserManager();
        ForceBarManager barManager = plugin.getForceBarManager();

        userManager.savePlayerData(player);
        barManager.removePlayer(player);
        userManager.removePlayerFromCache(player);
    }
}