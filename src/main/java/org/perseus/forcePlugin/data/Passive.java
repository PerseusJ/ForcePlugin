package org.perseus.forcePlugin.data;


import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Passive {

    private final String id;
    private final String displayName;
    private final List<String> description;

    public Passive(String id, String displayName, List<String> description) {
        this.id = id;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        this.description = description.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDescription() {
        return description;
    }
}
