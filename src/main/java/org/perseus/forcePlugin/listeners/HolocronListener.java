package org.perseus.forcePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.ArrayList;
import java.util.List;

public class HolocronListener implements Listener {

    private final ForcePlugin plugin;

    public HolocronListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.isSneaking()) {
            showBoundAbility(player);
        } else {
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, "");
        }
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        int oldSlot = event.getPreviousSlot();
        int newSlot = event.getNewSlot();
        int direction = 0;
        if (oldSlot == 8 && newSlot == 0) direction = 1;
        else if (oldSlot == 0 && newSlot == 8) direction = -1;
        else if (newSlot > oldSlot) direction = 1;
        else if (newSlot < oldSlot) direction = -1;

        if (direction != 0) {
            cycleBind(player, newSlot, direction);
        }
    }



    private void cycleBind(Player player, int slot, int direction) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        int currentSlot = slot;
        String currentBind = forceUser.getBoundAbilityId(currentSlot);

        List<String> unlocked = new ArrayList<>(forceUser.getUnlockedAbilities().keySet());
        if (unlocked.isEmpty()) return;

        int currentIndex = unlocked.indexOf(currentBind);
        if (currentIndex == -1) currentIndex = 0;

        currentIndex += direction;

        if (currentIndex >= unlocked.size()) currentIndex = 0;
        if (currentIndex < 0) currentIndex = unlocked.size() - 1;

        forceUser.setSlotBind(currentSlot, unlocked.get(currentIndex));
        plugin.getHudManager().updateScoreboard(player);
        showBoundAbility(player);
    }

    private void showBoundAbility(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        int currentSlot = player.getInventory().getHeldItemSlot();
        String abilityId = forceUser.getBoundAbilityId(currentSlot);
        Ability ability = plugin.getAbilityManager().getAbility(abilityId);
        if (ability != null) {
            ChatColor color = (forceUser.getSide() == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, color + "" + ChatColor.BOLD + "Slot " + (currentSlot + 1) + ": " + ability.getName());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.5f);
        } else {
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.GRAY + "Slot " + (currentSlot + 1) + ": Empty");
        }
    }
}