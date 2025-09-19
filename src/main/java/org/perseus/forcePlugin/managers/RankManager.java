package org.perseus.forcePlugin.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.Rank;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

    private final ForcePlugin plugin;
    private final Map<ForceSide, List<Rank>> linearRanks = new HashMap<>();
    private final Map<ForceSide, List<Rank>> specializations = new HashMap<>();

    public RankManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    public void loadRanks() {
        linearRanks.clear();
        specializations.clear();
        File ranksFile = new File(plugin.getDataFolder(), "ranks.yml");
        if (!ranksFile.exists()) {
            plugin.saveResource("ranks.yml", false);
        }

        FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);

        // Load Linear Ranks
        ConfigurationSection ranksSection = ranksConfig.getConfigurationSection("ranks");
        if (ranksSection != null) {
            for (String sideKey : ranksSection.getKeys(false)) {
                try {
                    ForceSide side = ForceSide.valueOf(sideKey.toUpperCase());
                    List<Rank> sideRanks = new ArrayList<>();
                    for (Map<?, ?> rankMap : ranksSection.getMapList(sideKey)) {
                        sideRanks.add(new Rank((int) rankMap.get("level-required"), (String) rankMap.get("display-name")));
                    }
                    sideRanks.sort(Comparator.comparingInt(Rank::getLevelRequired).reversed());
                    linearRanks.put(side, sideRanks);
                } catch (Exception e) {
                    plugin.getLogger().warning("Invalid rank configuration for side: " + sideKey);
                }
            }
        }

        // Load Specializations
        ConfigurationSection specSection = ranksConfig.getConfigurationSection("specializations");
        if (specSection != null) {
            for (String sideKey : specSection.getKeys(false)) {
                try {
                    ForceSide side = ForceSide.valueOf(sideKey.toUpperCase());
                    List<Rank> sideSpecs = new ArrayList<>();
                    for (Map<?, ?> specMap : specSection.getMapList(sideKey)) {
                        sideSpecs.add(new Rank((String) specMap.get("id"), (String) specMap.get("display-name"), (List<String>) specMap.get("description"), (String) specMap.get("material")));
                    }
                    specializations.put(side, sideSpecs);
                } catch (Exception e) {
                    plugin.getLogger().warning("Invalid specialization configuration for side: " + sideKey);
                }
            }
        }
        plugin.getLogger().info("Loaded ranks and specializations from ranks.yml.");
    }

    public String getRank(ForceUser forceUser) {
        if (forceUser.getSide() == ForceSide.NONE) return "";

        // If player has a specialization, that is their rank.
        if (forceUser.getSpecialization() != null) {
            return specializations.get(forceUser.getSide()).stream()
                    .filter(spec -> spec.getId().equals(forceUser.getSpecialization()))
                    .findFirst()
                    .map(Rank::getDisplayName)
                    .orElse("");
        }

        // Otherwise, find their highest linear rank.
        List<Rank> sideRanks = linearRanks.get(forceUser.getSide());
        if (sideRanks == null) return "";
        for (Rank rank : sideRanks) {
            if (forceUser.getForceLevel() >= rank.getLevelRequired()) {
                return rank.getDisplayName();
            }
        }
        return "";
    }

    public List<Rank> getSpecializations(ForceSide side) {
        return specializations.getOrDefault(side, new ArrayList<>());
    }
}