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
import org.perseus.forcePlugin.data.PassiveAbility;
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

        // --- THE FIX: Use the 1.16 compatible Action check ---
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        // --- END FIX ---

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

        if (forceUser.needsToChoosePath()) {
            sendActionBarMessage(player, ChatColor.RED + "You must choose your final path! Right-click your Holocron.");
            return;
        }

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

        double energyCost = ability.getEnergyCost(abilityLevel);

        if (forceUser.getSpecialization() != null) {
            if (forceUser.getSpecialization().equals("SENTINEL")) {
                int rank = forceUser.getPassiveRank("DUAL_FOCUS");
                if (rank > 0) {
                    PassiveAbility passive = findPassive(forceUser.getSpecialization(), "DUAL_FOCUS");
                    if (passive != null) energyCost *= passive.getValue(rank);
                }
            } else if (forceUser.getSpecialization().equals("SORCERER")) {
                int rank = forceUser.getPassiveRank("EFFICIENT_CORRUPTION");
                if (rank > 0 && ability.getSide() == forceUser.getSide()) {
                    PassiveAbility passive = findPassive(forceUser.getSpecialization(), "EFFICIENT_CORRUPTION");
                    if (passive != null) energyCost *= passive.getValue(rank);
                }
            }
        }

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            String remaining = cooldownManager.getRemainingCooldownFormatted(player, ability.getID());
            sendActionBarMessage(player, ChatColor.RED + ability.getName() + " is on cooldown: " + remaining);
            return;
        }

        if (forceUser.getCurrentForceEnergy() < energyCost) {
            sendActionBarMessage(player, ChatColor.AQUA + "Not enough Force Energy!");
            return;
        }

        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - energyCost);
        cooldownManager.setCooldown(player, ability.getID(), ability.getCooldown(abilityLevel));
        ability.execute(player, forceUser);
        forceBarManager.updateBar(player);

        double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-ability-use", 1.0);
        levelingManager.addXp(player, xpToGive);
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    private PassiveAbility findPassive(String specId, String passiveId) {
        return plugin.getPassiveManager().getPassivesForSpec(specId).stream()
                .filter(p -> p.getId().equals(passiveId))
                .findFirst().orElse(null);
    }
}