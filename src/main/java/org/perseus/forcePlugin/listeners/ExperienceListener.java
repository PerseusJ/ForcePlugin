package org.perseus.forcePlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.perseus.forcePlugin.managers.LevelingManager;

public class ExperienceListener implements Listener {

    private final LevelingManager levelingManager;

    public ExperienceListener(LevelingManager levelingManager) {
        this.levelingManager = levelingManager;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            double xpToGive = killer.getServer().getPluginManager().getPlugin("ForcePlugin").getConfig().getDouble("progression.xp-gain.per-mob-kill", 5.0);
            levelingManager.addXp(killer, xpToGive);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        levelingManager.updateXpBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
        levelingManager.updateXpBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(PlayerLevelChangeEvent event) {
        levelingManager.updateXpBar(event.getPlayer());
    }
}