package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LevelingManager {

    private final ForcePlugin plugin;
    private int maxLevel;
    private int pointsPerLevel;
    private double baseXp;
    private double xpMultiplier;

    public LevelingManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadConfigValues();
    }

    public void loadConfigValues() {
        FileConfiguration config = plugin.getConfig();
        this.maxLevel = config.getInt("progression.max-force-level", 50);
        this.pointsPerLevel = config.getInt("progression.points-per-level", 1);
        this.baseXp = config.getDouble("progression.base-xp", 100.0);
        this.xpMultiplier = config.getDouble("progression.xp-per-level-multiplier", 50.0);
    }

    public double getXpForNextLevel(int currentLevel) {
        if (currentLevel >= maxLevel) {
            return Double.MAX_VALUE;
        }
        // New Formula: Base + ( (Level - 1) * Multiplier)
        return baseXp + ((currentLevel - 1) * xpMultiplier);
    }

    public void addXp(Player player, double amount) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null || forceUser.getSide() == ForceSide.NONE || forceUser.getForceLevel() >= maxLevel) {
            return;
        }

        double currentXp = forceUser.getForceXp();
        double xpNeeded = getXpForNextLevel(forceUser.getForceLevel());

        forceUser.setForceXp(currentXp + amount);

        if (forceUser.getForceXp() >= xpNeeded) {
            levelUp(player, forceUser);
        }
        updateXpBar(player);
    }

    private void levelUp(Player player, ForceUser forceUser) {
        double xpNeeded = getXpForNextLevel(forceUser.getForceLevel());
        double excessXp = forceUser.getForceXp() - xpNeeded;

        forceUser.setForceLevel(forceUser.getForceLevel() + 1);
        forceUser.setForceXp(excessXp);
        forceUser.addForcePoints(pointsPerLevel);

        player.sendTitle(
                ChatColor.AQUA + "Force Level Up!",
                ChatColor.YELLOW + "You are now Level " + forceUser.getForceLevel(),
                10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        if (forceUser.getForceLevel() < maxLevel && forceUser.getForceXp() >= getXpForNextLevel(forceUser.getForceLevel())) {
            levelUp(player, forceUser);
        }
    }

    public void updateXpBar(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        player.setLevel(forceUser.getForceLevel());

        if (forceUser.getForceLevel() >= maxLevel) {
            player.setExp(1.0f);
        } else {
            float progress = (float) (forceUser.getForceXp() / getXpForNextLevel(forceUser.getForceLevel()));
            player.setExp(Math.max(0.0f, Math.min(1.0f, progress)));
        }
    }
}