package org.perseus.forcePlugin.abilities;

import org.bukkit.entity.Player;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

public interface Ability {
    String getID();
    String getName();
    String getDescription();
    ForceSide getSide();
    double getEnergyCost(int level);
    double getCooldown(int level);
    void execute(Player player, ForceUser forceUser);
}