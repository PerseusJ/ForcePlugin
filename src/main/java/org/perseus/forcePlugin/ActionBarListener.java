package org.perseus.forcePlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.perseus.forcePlugin.abilities.Ability;

/**
 * Displays the name of the selected ability in the player's action bar.
 */
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
            // If powers are off, clear the action bar just in case.
            sendActionBarMessage(player, "");
            return;
        }

        int newSlot = event.getNewSlot();

        // Check if the newly selected slot is an ability slot (0, 1, or 2).
        if (newSlot <= 2) {
            String abilityId = forceUser.getBoundAbility(newSlot + 1);
            if (abilityId != null) {
                Ability ability = abilityManager.getAbility(abilityId);
                if (ability != null) {
                    // Display the ability name in the action bar.
                    ChatColor nameColor = (ability.getSide() == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;
                    sendActionBarMessage(player, nameColor + "" + ChatColor.BOLD + ability.getName());
                }
            } else {
                // If the slot is empty, clear the action bar.
                sendActionBarMessage(player, "");
            }
        } else {
            // If they scroll to a non-ability slot, clear the action bar.
            sendActionBarMessage(player, "");
        }
    }

    /**
     * A helper method to send a message to the player's action bar.
     * @param player The player to send the message to.
     * @param message The text to display.
     */
    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}