package org.perseus.forcePlugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

public class ForcePlaceholders extends PlaceholderExpansion {

    private final ForcePlugin plugin;

    public ForcePlaceholders(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        // This is the prefix for all our placeholders (e.g., %forceplugin_level%)
        return "forceplugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Perseus";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required on newer PAPI versions
    }

    // This is the core method that gets called when a placeholder is used.
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) {
            return "N/A"; // Return something if the player's data isn't loaded
        }

        // Use a switch statement to handle different placeholders
        switch (identifier) {
            case "side":
                return forceUser.getSide().name();

            case "side_formatted":
                if (forceUser.getSide() == ForceSide.LIGHT) return ChatColor.AQUA + "Light";
                if (forceUser.getSide() == ForceSide.DARK) return ChatColor.RED + "Dark";
                return ChatColor.GRAY + "None";

            case "level":
                return String.valueOf(forceUser.getForceLevel());

            case "points":
                return String.valueOf(forceUser.getForcePoints());

            case "xp":
                return String.format("%.1f", forceUser.getForceXp());

            case "xp_needed":
                return String.format("%.1f", plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel()));

            case "xp_bar":
                // This is a more advanced placeholder that creates a visual progress bar
                return createProgressBar(forceUser.getForceXp(), plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel()));

            default:
                return null; // Let PAPI know we don't recognize this placeholder
        }
    }

    private String createProgressBar(double current, double max) {
        if (max <= 0) return "";
        float percent = (float) (current / max);
        int progressBars = 10; // The total number of bars in the progress bar
        int barsToShow = (int) (progressBars * percent);

        return ChatColor.GREEN + "" + "|".repeat(barsToShow)
                + ChatColor.GRAY + "" + "|".repeat(progressBars - barsToShow);
    }
}