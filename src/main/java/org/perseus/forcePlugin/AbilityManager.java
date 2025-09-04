package org.perseus.forcePlugin;

import org.perseus.forcePlugin.abilities.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityManager {

    private final Map<String, Ability> abilities = new HashMap<>();

    public AbilityManager(ForcePlugin plugin, AbilityConfigManager configManager) {
        // The constructor now just calls the registration method.
        registerAbilities(plugin, configManager);
    }

    // --- NEW METHOD ---
    /**
     * Clears and re-registers all abilities with a new configuration.
     * This is the core of the reload logic.
     * @param plugin The main plugin instance.
     * @param configManager The new, reloaded config manager.
     */
    public void reload(ForcePlugin plugin, AbilityConfigManager configManager) {
        abilities.clear(); // Remove all old ability instances
        registerAbilities(plugin, configManager); // Register them again with the new config
    }
    // --- END NEW ---

    /**
     * A helper method to register all abilities. Can be called from constructor or reload.
     */
    private void registerAbilities(ForcePlugin plugin, AbilityConfigManager configManager) {
        // Universal Abilities
        registerAbility(new ForcePush(configManager));
        registerAbility(new ForcePull(configManager));

        // Light Side Exclusive
        registerAbility(new ForceHeal(configManager));
        registerAbility(new ForceSpeed(configManager));
        registerAbility(new ForceValor(configManager));
        registerAbility(new ForceRepulse(configManager));
        registerAbility(new ForceStasis(configManager));
        registerAbility(new ForceBarrier(configManager));

        // Dark Side Exclusive
        registerAbility(new ForceChoke(plugin, configManager));
        registerAbility(new ForceLightning(configManager));
        registerAbility(new ForceScream(configManager));
        registerAbility(new ForceDrain(configManager));
        registerAbility(new ForceCrush(configManager));
        registerAbility(new ForceRage(configManager));
    }

    private void registerAbility(Ability ability) {
        abilities.put(ability.getID(), ability);
    }

    public Ability getAbility(String id) {
        return abilities.get(id);
    }

    public Collection<Ability> getAbilitiesBySide(ForceSide side) {
        return abilities.values().stream()
                .filter(ability -> ability.getSide() == side || ability.getSide() == ForceSide.NONE)
                .collect(Collectors.toList());
    }
}