package org.perseus.forcePlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.perseus.forcePlugin.abilities.Ability;

public class ActionBarListener implements Listener {

    private final ForceUserManager userManager;
    private final AbilityManager abilityManager;

    public ActionBarListener(ForceUserManager userManager, AbilityManager abilityManager) {
        this.userManager = userManager;
        this.abilityManager = abilityManager;
    }

    @EventHandler
    public void onHotbarScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || !forceUser.arePowersActive()) {
            sendActionBarMessage(player, "");
            return;
        }

        int newSlot = event.getNewSlot();

        if (newSlot <= 2) {
            String abilityId = forceUser.getBoundAbility(newSlot + 1);
            if (abilityId != null) {
                Ability ability = abilityManager.getAbility(abilityId);
                if (ability != null) {
                    // --- THE FIX: More robust logic for determining color ---
                    ChatColor nameColor;
                    ForceSide abilitySide = ability.getSide();
                    ForceSide playerSide = forceUser.getSide();

                    if (abilitySide == ForceSide.LIGHT) {
                        nameColor = ChatColor.AQUA;
                    } else if (abilitySide == ForceSide.DARK) {
                        nameColor = ChatColor.RED;
                    } else { // This handles the Universal (ForceSide.NONE) case
                        if (playerSide == ForceSide.LIGHT) {
                            nameColor = ChatColor.AQUA;
                        } else { // If player is Dark Side (or somehow NONE), default to red
                            nameColor = ChatColor.RED;
                        }
                    }
                    // --- END FIX ---

                    sendActionBarMessage(player, nameColor + "" + ChatColor.BOLD + ability.getName());
                }
            } else {
                sendActionBarMessage(player, "");
            }
        } else {
            sendActionBarMessage(player, "");
        }
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}