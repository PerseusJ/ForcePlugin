package org.perseus.forcePlugin.managers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

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
        int newLevel = forceUser.getForceLevel() + 1;

        forceUser.setForceLevel(newLevel);
        forceUser.setForceXp(excessXp);
        forceUser.addForcePoints(pointsPerLevel);

        player.sendTitle(
                ChatColor.AQUA + "Force Level Up!",
                ChatColor.YELLOW + "You are now Level " + forceUser.getForceLevel(),
                10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // Check for special ability unlocks
        String ultimateToUnlock = null;
        if (newLevel == 33) {
            ultimateToUnlock = forceUser.getSide() == ForceSide.LIGHT ? "FORCE_ABSORB" : "UNSTOPPABLE_VENGEANCE";
        } else if (newLevel == 66) {
            ultimateToUnlock = forceUser.getSide() == ForceSide.LIGHT ? "FORCE_CAMOUFLAGE" : "MARK_OF_THE_HUNT";
        } else if (newLevel == 100) {
            ultimateToUnlock = forceUser.getSide() == ForceSide.LIGHT ? "FORCE_SERENITY" : "CHAIN_LIGHTNING";
        }

        if (ultimateToUnlock != null && !forceUser.hasUnlockedAbility(ultimateToUnlock)) {
            forceUser.unlockAbility(ultimateToUnlock);
            org.perseus.forcePlugin.abilities.Ability ultimate = plugin.getAbilityManager().getAbility(ultimateToUnlock);
            if (ultimate != null) {
                player.sendMessage("");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Ultimate Ability Unlocked!");
                player.sendMessage(ChatColor.YELLOW + "You have unlocked: " + ChatColor.AQUA + ultimate.getName());
                player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GOLD + "/force abilities" + ChatColor.GRAY + " to view it.");
                player.sendMessage("");
            }
        }

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