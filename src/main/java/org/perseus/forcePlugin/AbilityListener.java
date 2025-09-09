package org.perseus.forcePlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.abilities.Ability;

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

        if (!action.isLeftClick() || !plugin.getHolocronManager().isHolocron(itemInHand)) {
            return;
        }

        event.setCancelled(true);

        if (player.isSneaking()) {
            return;
        }

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String abilityId = forceUser.getActiveAbilityId();
        if (abilityId == null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "No ability selected."));
            return;
        }

        Ability ability = plugin.getAbilityManager().getAbility(abilityId);
        if (ability == null) return;

        int abilityLevel = forceUser.getAbilityLevel(abilityId);
        CooldownManager cooldownManager = plugin.getCooldownManager();
        LevelingManager levelingManager = plugin.getLevelingManager();

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            String remaining = cooldownManager.getRemainingCooldownFormatted(player, ability.getID());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + ability.getName() + " is on cooldown: " + remaining));
            return;
        }

        if (forceUser.getCurrentForceEnergy() < ability.getEnergyCost(abilityLevel)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Not enough Force Energy!"));
            return;
        }

        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - ability.getEnergyCost(abilityLevel));
        cooldownManager.setCooldown(player, ability.getID(), ability.getCooldown(abilityLevel));
        ability.execute(player, forceUser);
        plugin.getForceBarManager().updateBar(player);

        double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-ability-use", 1.0);
        if (ability.getID().equals("TELEKINESIS")) {
            // XP for Telekinesis is now handled in its own manager/listener
        } else {
            levelingManager.addXp(player, xpToGive);
        }
    }
}