package org.perseus.forcePlugin.data;

import org.bukkit.ChatColor;

public class Rank {

    private final int levelRequired;
    private final String displayName;

    public Rank(int levelRequired, String displayName) {
        this.levelRequired = levelRequired;
        // Translate color codes (&c, &b, etc.) when the rank is created.
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public String getDisplayName() {
        return displayName;
    }
}