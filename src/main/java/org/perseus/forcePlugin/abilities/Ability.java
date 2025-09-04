package org.perseus.forcePlugin.abilities;

import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForceSide;

public interface Ability {
    String getID();
    String getName();
    String getDescription();
    ForceSide getSide();
    double getEnergyCost();
    double getCooldown();
    void execute(Player player);
}