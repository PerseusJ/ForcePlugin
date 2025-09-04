package org.perseus.forcePlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final ForceUserManager userManager;
    private final ForceBarManager barManager;

    // The constructor no longer needs the HotbarManager.
    public PlayerConnectionListener(ForceUserManager userManager, ForceBarManager barManager) {
        this.userManager = userManager;
        this.barManager = barManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        userManager.loadPlayerData(event.getPlayer());
        barManager.addPlayer(event.getPlayer());
        // The call to hotbarManager.updateHotbar(...) has been removed.
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        userManager.savePlayerData(event.getPlayer());
        barManager.removePlayer(event.getPlayer());
        userManager.removePlayerFromCache(event.getPlayer());
    }
}