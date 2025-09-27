package org.perseus.forcePlugin.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public class PassiveAbility {

    private final String id;
    private final String displayName;
    private final List<String> description;
    private final Material material; // This is the final field
    private final int tier;
    private final int maxRank;
    private final List<Double> values;

    public PassiveAbility(String id, String displayName, List<String> description, String materialName, int tier, int maxRank, List<Double> values) {
        this.id = id;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        this.description = description.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());

        // --- THE FIX: Use a temporary variable to determine the material first ---
        Material tempMaterial;
        try {
            tempMaterial = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            tempMaterial = Material.BARRIER; // Assign fallback to the temporary variable
        }
        this.material = tempMaterial; // Assign to the final field only once
        // --- END FIX ---

        this.tier = tier;
        this.maxRank = maxRank;
        this.values = values;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public List<String> getDescription() { return description; }
    public Material getMaterial() { return material; }
    public int getTier() { return tier; }
    public int getMaxRank() { return maxRank; }

    public double getValue(int rank) {
        if (rank > 0 && rank <= values.size()) {
            return values.get(rank - 1);
        }
        return 0.0;
    }
}