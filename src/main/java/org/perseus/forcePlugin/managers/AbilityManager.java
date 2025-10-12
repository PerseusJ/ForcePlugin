package org.perseus.forcePlugin.managers;

import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.abilities.dark.*;
import org.perseus.forcePlugin.abilities.light.*;
import org.perseus.forcePlugin.abilities.universal.*;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.listeners.UltimateAbilityListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityManager {

    private final Map<String, Ability> abilities = new HashMap<>();

    public AbilityManager(ForcePlugin plugin, AbilityConfigManager configManager, TelekinesisManager telekinesisManager) {
        registerAbilities(plugin, configManager, telekinesisManager);
    }

    public void reload(ForcePlugin plugin, AbilityConfigManager configManager, TelekinesisManager telekinesisManager) {
        abilities.clear();
        registerAbilities(plugin, configManager, telekinesisManager);
    }

    private void registerAbilities(ForcePlugin plugin, AbilityConfigManager configManager, TelekinesisManager telekinesisManager) {
        // Universal
        registerAbility(new ForcePush(configManager));
        registerAbility(new ForcePull(configManager, plugin));
        registerAbility(new Telekinesis(configManager, telekinesisManager));

        // Light Side
        registerAbility(new ForceHeal(configManager, plugin));
        registerAbility(new ForceRepulse(configManager, plugin));
        registerAbility(new ForceStasis(configManager, plugin));
        registerAbility(new ForceBarrier(configManager, plugin));
        registerAbility(new ForceJudgment(configManager, plugin));
        registerAbility(new ForceDeflection(configManager, plugin));
        registerAbility(new ForceAbsorb(configManager, plugin));
        registerAbility(new ForceCamouflage(configManager));
        registerAbility(new ForceSerenity(configManager, plugin));

        // Dark Side
        registerAbility(new ForceChoke(plugin, configManager));
        registerAbility(new ForceLightning(configManager, plugin));
        registerAbility(new ForceScream(configManager, plugin));
        registerAbility(new ForceDrain(configManager, plugin));
        registerAbility(new ForceCrush(configManager, plugin));
        registerAbility(new ForceRage(configManager, plugin));
        registerAbility(new UnstoppableVengeance(configManager, plugin));
        registerAbility(new MarkOfTheHunt(configManager));
        registerAbility(new ChainLightning(configManager, plugin));
    }

    private void registerAbility(Ability ability) {
        abilities.put(ability.getID(), ability);
    }

    public Ability getAbility(String id) {
        return abilities.get(id);
    }

    public Collection<Ability> getAbilitiesBySide(ForceSide side) {
        return abilities.values().stream()
                .filter(ability -> !UltimateAbilityListener.ULTIMATE_ABILITY_IDS.contains(ability.getID()))
                .filter(ability -> ability.getSide() == side || ability.getSide() == ForceSide.NONE)
                .collect(Collectors.toList());
    }

    public Collection<Ability> getAllAbilitiesBySide(ForceSide side) {
        return abilities.values().stream()
                .filter(ability -> ability.getSide() == side || ability.getSide() == ForceSide.NONE)
                .collect(Collectors.toList());
    }
}