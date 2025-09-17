package org.perseus.forcePlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceUser;

public class ForceStatsCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public ForceStatsCommand(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Please specify a player to check.");
                return true;
            }
            target = (Player) sender;
        } else {
            if (!sender.hasPermission("forceplugin.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to check other players' stats.");
                return true;
            }
            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found.");
                return true;
            }
        }

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
        if (forceUser == null) {
            sender.sendMessage(ChatColor.RED + "Could not retrieve Force data for that player.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "--- Force Stats for " + target.getName() + " ---");
        sender.sendMessage(ChatColor.YELLOW + "Side: " + ChatColor.WHITE + forceUser.getSide().name());

        // --- NEW: Display the player's rank ---
        String rank = plugin.getRankManager().getRank(forceUser.getSide(), forceUser.getForceLevel());
        if (!rank.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Rank: " + rank);
        }
        // --- END NEW ---

        sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.AQUA + forceUser.getForceLevel());

        double currentXp = forceUser.getForceXp();
        double neededXp = plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel());
        if (neededXp == Double.MAX_VALUE) {
            sender.sendMessage(ChatColor.YELLOW + "XP: " + ChatColor.GOLD + "Max Level");
        } else {
            sender.sendMessage(String.format(ChatColor.YELLOW + "XP: " + ChatColor.WHITE + "%.1f / %.1f", currentXp, neededXp));
        }

        sender.sendMessage(ChatColor.YELLOW + "Available Points: " + ChatColor.GREEN + forceUser.getForcePoints());
        sender.sendMessage(ChatColor.GOLD + "--------------------------");

        return true;
    }
}