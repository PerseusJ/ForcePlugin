package org.perseus.forcePlugin.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.*;

public class AbilityListener implements Listener {

    private final ForcePlugin plugin;

    public AbilityListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemInHand = event.getItem();

        TelekinesisManager telekinesisManager = plugin.getTelekinesisManager();
        ForceUserManager userManager = plugin.getForceUserManager();

        // Use older Action check for 1.16 compatibility
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (!plugin.getHolocronManager().isHolocron(itemInHand)) {
            return;
        }

        event.setCancelled(true);

        if (player.isSneaking()) {
            return;
        }

        if (telekinesisManager.isLifting(player)) {
            telekinesisManager.launch(player);
            double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-telekinesis-launch", 2.0);
            plugin.getLevelingManager().addXp(player, xpToGive);
            return;
        }

        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        String abilityId = forceUser.getActiveAbilityId();
        if (abilityId == null) {
            sendActionBarMessage(player, ChatColor.YELLOW + "No ability selected.");
            return;
        }

        AbilityManager abilityManager = plugin.getAbilityManager();
        Ability ability = abilityManager.getAbility(abilityId);
        if (ability == null) return;

        if (!forceUser.hasUnlockedAbility(abilityId)) {
            player.sendMessage(ChatColor.RED + "A bug has occurred: Tried to use a locked ability.");
            return;
        }

        int abilityLevel = forceUser.getAbilityLevel(abilityId);
        CooldownManager cooldownManager = plugin.getCooldownManager();
        LevelingManager levelingManager = plugin.getLevelingManager();
        ForceBarManager forceBarManager = plugin.getForceBarManager();

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            String remaining = cooldownManager.getRemainingCooldownFormatted(player, ability.getID());
            sendActionBarMessage(player, ChatColor.RED + ability.getName() + " is on cooldown: " + remaining);
            return;
        }

        if (forceUser.getCurrentForceEnergy() < ability.getEnergyCost(abilityLevel)) {
            sendActionBarMessage(player, ChatColor.AQUA + "Not enough Force Energy!");
            return;
        }

        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - ability.getEnergyCost(abilityLevel));
        cooldownManager.setCooldown(player, ability.getID(), ability.getCooldown(abilityLevel));
        ability.execute(player, forceUser);
        forceBarManager.updateBar(player);

        double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-ability-use", 1.0);
        levelingManager.addXp(player, xpToGive);
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}