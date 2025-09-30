package org.perseus.forcePlugin.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public class Rank {

    private int levelRequired;
    private String id;
    private final String displayName;
    private List<String> description;
    private Material material;

    public Rank(int levelRequired, String displayName) {
        this.levelRequired = levelRequired;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public Rank(String id, String displayName, List<String> description, String materialName) {
        this.id = id;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        this.description = description.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        try {
            this.material = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.material = Material.BARRIER;
        }
    }

    public int getLevelRequired() { return levelRequired; }
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public List<String> getDescription() { return description; }
    public Material getMaterial() { return material; }
}