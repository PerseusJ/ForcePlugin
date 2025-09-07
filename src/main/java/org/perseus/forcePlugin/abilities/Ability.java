package org.perseus.forcePlugin.abilities;

import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForceSide;
import org.perseus.forcePlugin.ForceUser;

public interface Ability {
    String getID();
    String getName();
    String getDescription();
    ForceSide getSide();

    // Methods to get stats for a specific level (used for display and checks)
    double getEnergyCost(int level);
    double getCooldown(int level);

    // The execute method now takes the ForceUser to know the ability's current level
    void execute(Player player, ForceUser forceUser);
}