package org.perseus.forcePlugin.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.Rank;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

    private final ForcePlugin plugin;
    private final Map<ForceSide, List<Rank>> ranks = new HashMap<>();

    public RankManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    public void loadRanks() {
        ranks.clear();
        File ranksFile = new File(plugin.getDataFolder(), "ranks.yml");
        if (!ranksFile.exists()) {
            plugin.saveResource("ranks.yml", false);
        }

        FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
        ConfigurationSection ranksSection = ranksConfig.getConfigurationSection("ranks");
        if (ranksSection == null) return;

        for (String sideKey : ranksSection.getKeys(false)) {
            try {
                ForceSide side = ForceSide.valueOf(sideKey.toUpperCase());
                List<Rank> sideRanks = new ArrayList<>();
                List<Map<?, ?>> rankMaps = ranksSection.getMapList(sideKey);

                for (Map<?, ?> rankMap : rankMaps) {
                    int level = (int) rankMap.get("level-required");
                    String name = (String) rankMap.get("display-name");
                    sideRanks.add(new Rank(level, name));
                }

                // Sort ranks by level required, highest to lowest.
                sideRanks.sort(Comparator.comparingInt(Rank::getLevelRequired).reversed());
                ranks.put(side, sideRanks);

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid Force Side '" + sideKey + "' found in ranks.yml.");
            }
        }
        plugin.getLogger().info("Loaded " + ranks.values().stream().mapToInt(List::size).sum() + " ranks from ranks.yml.");
    }

    /**
     * Gets the correct rank title for a player based on their side and level.
     * @param side The player's ForceSide.
     * @param level The player's Force Level.
     * @return The display name of the rank, or an empty string if none is found.
     */
    public String getRank(ForceSide side, int level) {
        List<Rank> sideRanks = ranks.get(side);
        if (sideRanks == null || sideRanks.isEmpty()) {
            return "";
        }

        // Because the list is sorted from highest to lowest, the first one we find is the correct one.
        for (Rank rank : sideRanks) {
            if (level >= rank.getLevelRequired()) {
                return rank.getDisplayName();
            }
        }

        return ""; // Should not happen if ranks are configured from level 1.
    }
}