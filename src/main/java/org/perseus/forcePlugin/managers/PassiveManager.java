package org.perseus.forcePlugin.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.PassiveAbility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassiveManager {

    private final ForcePlugin plugin;
    private final Map<String, List<PassiveAbility>> specializationPassives = new HashMap<>();

    public PassiveManager(ForcePlugin plugin) {
        this.plugin = plugin;
        loadPassives();
    }

    public void loadPassives() {
        specializationPassives.clear();
        File passivesFile = new File(plugin.getDataFolder(), "passives.yml");
        if (!passivesFile.exists()) {
            plugin.saveResource("passives.yml", false);
        }

        FileConfiguration passivesConfig = YamlConfiguration.loadConfiguration(passivesFile);
        ConfigurationSection specSection = passivesConfig.getConfigurationSection("specializations");
        if (specSection == null) return;

        for (String specKey : specSection.getKeys(false)) {
            List<PassiveAbility> passiveList = new ArrayList<>();
            List<Map<?, ?>> passiveMaps = specSection.getMapList(specKey);

            for (Map<?, ?> passiveMap : passiveMaps) {
                try {
                    String id = (String) passiveMap.get("id");
                    String name = (String) passiveMap.get("display-name");

                    // --- THE FIX: Robustly handle both String and List for description ---
                    List<String> desc;
                    Object descObj = passiveMap.get("description");
                    if (descObj instanceof List) {
                        desc = (List<String>) descObj;
                    } else {
                        desc = Collections.singletonList(String.valueOf(descObj));
                    }
                    // --- END FIX ---

                    String mat = (String) passiveMap.get("material");
                    int tier = (int) passiveMap.get("tier");
                    int maxRank = (int) passiveMap.get("max-rank");
                    List<Double> values = (List<Double>) passiveMap.get("values");
                    passiveList.add(new PassiveAbility(id, name, desc, mat, tier, maxRank, values));
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to load a passive for specialization: " + specKey);
                    e.printStackTrace();
                }
            }
            specializationPassives.put(specKey.toUpperCase(), passiveList);
        }
        plugin.getLogger().info("Loaded " + specializationPassives.values().stream().mapToInt(List::size).sum() + " passive abilities.");
    }

    public List<PassiveAbility> getPassivesForSpec(String specId) {
        if (specId == null) return new ArrayList<>();
        return specializationPassives.getOrDefault(specId.toUpperCase(), new ArrayList<>());
    }
}