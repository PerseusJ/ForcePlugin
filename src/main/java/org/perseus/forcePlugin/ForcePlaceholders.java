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
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) {
            return "N/A";
        }

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
                double needed = plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel());
                return (needed == Double.MAX_VALUE) ? "Max" : String.format("%.1f", needed);

            case "xp_bar":
                return createProgressBar(forceUser.getForceXp(), plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel()));

            case "rank":
                return plugin.getRankManager().getRank(forceUser);

            default:
                return null;
        }
    }

    private String createProgressBar(double current, double max) {
        if (max <= 0 || max == Double.MAX_VALUE) {
            return ChatColor.GOLD + "" + repeat("|", 10);
        }
        float percent = (float) (current / max);
        int progressBars = 10;
        int barsToShow = (int) (progressBars * percent);

        return ChatColor.GREEN + "" + repeat("|", barsToShow)
                + ChatColor.GRAY + "" + repeat("|", progressBars - barsToShow);
    }

    private String repeat(String str, int times) {
        if (times <= 0) return "";
        StringBuilder sb = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}