package org.perseus.forcePlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.ForceUserManager;

import java.util.Map;

public class ForceCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public ForceCommand(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("forceplugin.use")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        ForceUserManager userManager = plugin.getForceUserManager();
        ForceUser forceUser = userManager.getForceUser(player);

        if (forceUser == null) {
            player.sendMessage(ChatColor.RED + "Error: Your data is still loading. Please wait a moment and try again.");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("choose")) {
            if (forceUser.getSide() != ForceSide.NONE) {
                player.sendMessage(ChatColor.RED + "You have already chosen your path. You cannot change it.");
                return true;
            }
            plugin.getGuiManager().openChooseSideGUI(player);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("abilities")) {
            if (forceUser.getSide() == ForceSide.NONE) {
                player.sendMessage(ChatColor.RED + "You must choose a path first (/force choose).");
                return true;
            }
            plugin.getGuiManager().openAbilityGUI(player);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("bind")) {
            handleBindCommand(player, forceUser, args);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Usage: /force choose | /force abilities | /force bind <slot> [ability] | /force bind list | /force bind gui");
        }
        return true;
    }

    private void handleBindCommand(Player player, ForceUser forceUser, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /force bind <slot> [ability] | /force bind list | /force bind gui");
            return;
        }

        if (args[1].equalsIgnoreCase("list")) {
            Map<Integer, String> binds = forceUser.getSlotBinds();
            if (binds.isEmpty()) {
                player.sendMessage(ChatColor.GRAY + "No abilities are bound to any slot.");
            } else {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Slot Binds:");
                for (int slot = 0; slot < 9; slot++) {
                    String abilityId = forceUser.getBoundAbilityId(slot);
                    if (abilityId != null) {
                        Ability ability = plugin.getAbilityManager().getAbility(abilityId);
                        String name = (ability != null) ? ability.getName() : abilityId;
                        player.sendMessage(ChatColor.GRAY + "  Slot " + (slot + 1) + ": " + ChatColor.WHITE + name);
                    }
                }
            }
            return;
        }

        if (args[1].equalsIgnoreCase("gui")) {
            plugin.getGuiManager().openBindGUI(player);
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid slot. Use a number 1-9.");
            return;
        }

        if (slot < 0 || slot > 8) {
            player.sendMessage(ChatColor.RED + "Slot must be between 1 and 9.");
            return;
        }

        if (args.length == 2) {
            forceUser.setSlotBind(slot, null);
            player.sendMessage(ChatColor.GRAY + "Unbound slot " + (slot + 1) + ".");
            plugin.getHudManager().updateScoreboard(player);
            return;
        }

        String abilityId = args[2].toUpperCase();
        if (!forceUser.hasUnlockedAbility(abilityId)) {
            player.sendMessage(ChatColor.RED + "You have not unlocked " + abilityId + ".");
            return;
        }

        forceUser.setSlotBind(slot, abilityId);
        plugin.getHudManager().updateScoreboard(player);
        Ability ability = plugin.getAbilityManager().getAbility(abilityId);
        String name = (ability != null) ? ability.getName() : abilityId;
        player.sendMessage(ChatColor.GREEN + "Bound " + name + " to slot " + (slot + 1) + ".");
    }
}