package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Manages all logic related to Force XP, levels, and progression.
 */
public class LevelingManager {

    private final ForcePlugin plugin;

    public LevelingManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Calculates the total XP required to reach the next level.
     * @param currentLevel The player's current level.
     * @return The total XP needed to level up.
     */
    public double getXpForNextLevel(int currentLevel) {
        // A simple formula: 100 XP for level 1, 200 for level 2, etc.
        // This can be made more complex later (e.g., exponential growth).
        return currentLevel * 100.0;
    }

    /**
     * Adds a specified amount of Force XP to a player and handles leveling up.
     * @param player The player to grant XP to.
     * @param amount The amount of XP to add.
     */
    public void addXp(Player player, double amount) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null || forceUser.getSide() == ForceSide.NONE) {
            return; // Don't grant XP if they haven't chosen a side.
        }

        double currentXp = forceUser.getForceXp();
        double xpNeeded = getXpForNextLevel(forceUser.getForceLevel());

        forceUser.setForceXp(currentXp + amount);

        // Check if the player has enough XP to level up.
        if (forceUser.getForceXp() >= xpNeeded) {
            levelUp(player, forceUser);
        }

        // Update the visual XP bar.
        updateXpBar(player);
    }

    /**
     * Handles the logic for when a player levels up.
     * @param player The player who is leveling up.
     * @param forceUser The data object for that player.
     */
    private void levelUp(Player player, ForceUser forceUser) {
        double xpNeeded = getXpForNextLevel(forceUser.getForceLevel());
        double excessXp = forceUser.getForceXp() - xpNeeded;

        // Increase level, reset XP (carrying over any excess), and grant a point.
        forceUser.setForceLevel(forceUser.getForceLevel() + 1);
        forceUser.setForceXp(excessXp);
        forceUser.addForcePoints(1);

        // Notify the player with a title message and sound.
        player.sendTitle(
                ChatColor.AQUA + "Force Level Up!",
                ChatColor.YELLOW + "You are now Level " + forceUser.getForceLevel(),
                10, 70, 20); // FadeIn, Stay, FadeOut ticks
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // Check if they can level up again with the excess XP.
        if (forceUser.getForceXp() >= getXpForNextLevel(forceUser.getForceLevel())) {
            levelUp(player, forceUser); // Recursive call for multi-level ups.
        }
    }

    /**
     * Updates the player's vanilla XP bar to visually represent their Force XP progress.
     * @param player The player whose bar should be updated.
     */
    public void updateXpBar(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        // The XP bar level shows the Force Level.
        player.setLevel(forceUser.getForceLevel());

        // The XP bar progress shows the percentage towards the next level.
        float progress = (float) (forceUser.getForceXp() / getXpForNextLevel(forceUser.getForceLevel()));
        player.setExp(Math.max(0.0f, Math.min(1.0f, progress)));
    }
}