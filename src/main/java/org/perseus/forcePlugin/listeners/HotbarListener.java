package org.perseus.forcePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceUser;

public class HotbarListener implements Listener {

    private final ForcePlugin plugin;

    public HotbarListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        int newSlot = event.getNewSlot();
        String abilityId = forceUser.getBoundAbilityId(newSlot);
        if (abilityId != null) {
            Ability ability = plugin.getAbilityManager().getAbility(abilityId);
            if (ability != null) {
                org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.GRAY + "[" + ChatColor.GOLD + ability.getName() + ChatColor.GRAY + "]");
            }
        }
    }
}
