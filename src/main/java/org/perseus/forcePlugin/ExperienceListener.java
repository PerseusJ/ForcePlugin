package org.perseus.forcePlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

/**
 * Manages Force XP gain and controls the vanilla XP bar.
 */
public class ExperienceListener implements Listener {

    private final LevelingManager levelingManager;

    public ExperienceListener(LevelingManager levelingManager) {
        this.levelingManager = levelingManager;
    }

    // Grant Force XP when a player kills a mob.
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            // Grant a configurable amount of XP. For now, a flat 5 XP.
            levelingManager.addXp(killer, 5.0);
        }
    }

    // Update the XP bar when the player joins.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        levelingManager.updateXpBar(event.getPlayer());
    }

    // Prevent the vanilla XP bar from changing due to other sources (e.g., bottles o' enchanting).
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExpChange(PlayerExpChangeEvent event) {
        // We cancel the event, then manually update our bar to ensure it's always correct.
        event.setAmount(0);
        levelingManager.updateXpBar(event.getPlayer());
    }

    // Prevent the level from changing from other sources (e.g., enchanting tables).
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(PlayerLevelChangeEvent event) {
        // This is a bit more complex, but for now, we'll just re-sync the bar.
        levelingManager.updateXpBar(event.getPlayer());
    }
}