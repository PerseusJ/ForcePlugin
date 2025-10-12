package org.perseus.forcePlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.perseus.forcePlugin.ForcePlugin;

public class ExperienceListener implements Listener {

    private final ForcePlugin plugin;

    public ExperienceListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-mob-kill", 5.0);
            plugin.getLevelingManager().addXp(killer, xpToGive);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getLevelingManager().updateXpBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
        plugin.getLevelingManager().updateXpBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(PlayerLevelChangeEvent event) {
        plugin.getLevelingManager().updateXpBar(event.getPlayer());
    }
}