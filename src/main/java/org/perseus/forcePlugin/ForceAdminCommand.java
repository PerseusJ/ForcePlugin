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
        if (!sender.hasPermission("forceplugin.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "reload": handleReload(sender); break;
            case "setside": handleSetSide(sender, args); break;
            case "reset": handleReset(sender, args); break;
            case "check": handleCheck(sender, args); break;
            case "setlevel": handleSetLevel(sender, args); break;
            case "givexp": handleGiveXp(sender, args); break;
            case "givepoints": handleGivePoints(sender, args); break;
            case "giveholocron": handleGiveHolocron(sender, args); break;
            default: sendHelpMessage(sender); break;
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
            plugin.getForceBarManager().updateBar(target);
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

        // --- THE FIX: Correctly reset all data and add back defaults ---
        forceUser.setSide(ForceSide.NONE);
        forceUser.getUnlockedAbilities().clear(); // Clear all unlocked abilities first
        forceUser.unlockAbility("FORCE_PUSH");   // Add back the defaults
        forceUser.unlockAbility("FORCE_PULL");   // Add back the defaults
        forceUser.setActiveAbilityId("FORCE_PUSH"); // Set a default active ability
        forceUser.setForceLevel(1);
        forceUser.setForceXp(0);
        forceUser.setForcePoints(0);
        // --- END FIX ---

        plugin.getHolocronManager().removeHolocron(target);
        plugin.getForceBarManager().updateBar(target);
        plugin.getLevelingManager().updateXpBar(target);
        sender.sendMessage(ChatColor.GREEN + "Completely reset " + target.getName() + "'s Force data.");
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
        sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.AQUA + forceUser.getForceLevel());
        double neededXp = plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel());
        sender.sendMessage(String.format(ChatColor.YELLOW + "XP: " + ChatColor.WHITE + "%.1f / %.1f", forceUser.getForceXp(), neededXp));
        sender.sendMessage(ChatColor.YELLOW + "Points: " + ChatColor.GREEN + forceUser.getForcePoints());
        String activeAbilityName = "None";
        if (forceUser.getActiveAbilityId() != null) {
            Ability ability = plugin.getAbilityManager().getAbility(forceUser.getActiveAbilityId());
            if (ability != null) activeAbilityName = ability.getName();
        }
        sender.sendMessage(ChatColor.YELLOW + "Active Ability: " + ChatColor.WHITE + activeAbilityName);
    }

    private void handleSetLevel(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa setlevel <player> <level>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        try {
            int level = Integer.parseInt(args[2]);
            ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
            forceUser.setForceLevel(level);
            forceUser.setForceXp(0);
            plugin.getLevelingManager().updateXpBar(target);
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s Force Level to " + level + ".");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid level number.");
        }
    }

    private void handleGiveXp(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa givexp <player> <amount>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        try {
            double amount = Double.parseDouble(args[2]);
            plugin.getLevelingManager().addXp(target, amount);
            sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " Force XP to " + target.getName() + ".");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid XP amount.");
        }
    }

    private void handleGivePoints(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa givepoints <player> <amount>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        try {
            int amount = Integer.parseInt(args[2]);
            ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
            forceUser.addForcePoints(amount);
            sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " Force Point(s) to " + target.getName() + ".");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid point amount.");
        }
    }

    private void handleGiveHolocron(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /fa giveholocron <player>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
        if (forceUser.getSide() == ForceSide.NONE) {
            sender.sendMessage(ChatColor.RED + "That player has not chosen a side yet.");
            return;
        }
        plugin.getHolocronManager().giveHolocron(target);
        sender.sendMessage(ChatColor.GREEN + "Gave a new Holocron to " + target.getName() + ".");
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- ForceAdmin Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/fa reload" + ChatColor.GRAY + " - Reloads the config.yml.");
        sender.sendMessage(ChatColor.YELLOW + "/fa setside <player> <side>" + ChatColor.GRAY + " - Sets a player's alignment.");
        sender.sendMessage(ChatColor.YELLOW + "/fa reset <player>" + ChatColor.GRAY + " - Resets a player's data.");
        sender.sendMessage(ChatColor.YELLOW + "/fa check <player>" + ChatColor.GRAY + " - Checks a player's data.");
        sender.sendMessage(ChatColor.YELLOW + "/fa setlevel <player> <level>" + ChatColor.GRAY + " - Sets a player's Force Level.");
        sender.sendMessage(ChatColor.YELLOW + "/fa givexp <player> <amount>" + ChatColor.GRAY + " - Gives a player Force XP.");
        sender.sendMessage(ChatColor.YELLOW + "/fa givepoints <player> <amount>" + ChatColor.GRAY + " - Gives a player Force Points.");
        sender.sendMessage(ChatColor.YELLOW + "/fa giveholocron <player>" + ChatColor.GRAY + " - Gives a player a new Holocron.");
    }
}