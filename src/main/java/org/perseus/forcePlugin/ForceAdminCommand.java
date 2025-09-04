package org.perseus.forcePlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.abilities.Ability;

public class ForceAdminCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public ForceAdminCommand(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 1. Permission Check
        if (!sender.hasPermission("forceplugin.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // 2. Check for sub-command
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                handleReload(sender);
                break;
            case "setside":
                handleSetSide(sender, args);
                break;
            case "reset":
                handleReset(sender, args);
                break;
            case "check":
                handleCheck(sender, args);
                break;
            default:
                sendHelpMessage(sender);
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadPluginConfig();
        sender.sendMessage(ChatColor.GREEN + "ForcePlugin configuration has been reloaded.");
    }

    private void handleSetSide(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa setside <player> <light|dark|none>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        try {
            ForceSide side = ForceSide.valueOf(args[2].toUpperCase());
            ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
            forceUser.setSide(side);
            plugin.getForceBarManager().updateBar(target); // Update their bar color
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s side to " + side.name() + ".");
            target.sendMessage(ChatColor.YELLOW + "An admin has set your Force alignment to " + side.name() + ".");
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid side. Use <light|dark|none>.");
        }
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa reset <player>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
        forceUser.setSide(ForceSide.NONE);
        for (int i = 1; i <= 3; i++) { // Unbind all abilities
            forceUser.setBoundAbility(i, null);
        }
        plugin.getForceBarManager().updateBar(target);
        sender.sendMessage(ChatColor.GREEN + "Reset " + target.getName() + "'s Force data.");
        target.sendMessage(ChatColor.YELLOW + "An admin has reset your Force data.");
    }

    private void handleCheck(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa check <player>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
        sender.sendMessage(ChatColor.GOLD + "--- Force Data for " + target.getName() + " ---");
        sender.sendMessage(ChatColor.YELLOW + "Side: " + ChatColor.WHITE + forceUser.getSide().name());
        for (int i = 1; i <= 3; i++) {
            String abilityId = forceUser.getBoundAbility(i);
            String abilityName = "Empty";
            if (abilityId != null) {
                Ability ability = plugin.getAbilityManager().getAbility(abilityId);
                if (ability != null) abilityName = ability.getName();
            }
            sender.sendMessage(ChatColor.YELLOW + "Slot " + i + ": " + ChatColor.WHITE + abilityName);
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- ForceAdmin Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/fa reload" + ChatColor.GRAY + " - Reloads the config.yml.");
        sender.sendMessage(ChatColor.YELLOW + "/fa setside <player> <side>" + ChatColor.GRAY + " - Sets a player's alignment.");
        sender.sendMessage(ChatColor.YELLOW + "/fa reset <player>" + ChatColor.GRAY + " - Resets a player's data.");
        sender.sendMessage(ChatColor.YELLOW + "/fa check <player>" + ChatColor.GRAY + " - Checks a player's data.");
    }
}