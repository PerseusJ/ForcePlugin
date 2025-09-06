package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /force stats command for displaying player progression.
 */
public class ForceStatsCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public ForceStatsCommand(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // This command can be used by players to check their own stats,
        // or by admins to check others' stats.

        Player target;

        if (args.length == 0) {
            // If no player is specified, the sender must be a player checking their own stats.
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Please specify a player to check.");
                return true;
            }
            target = (Player) sender;
        } else {
            // If a player is specified, the sender needs admin perms to check others.
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

        // At this point, we have a valid target player.
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(target);
        if (forceUser == null) {
            sender.sendMessage(ChatColor.RED + "Could not retrieve Force data for that player.");
            return true;
        }

        // Display the stats in a clean format.
        sender.sendMessage(ChatColor.GOLD + "--- Force Stats for " + target.getName() + " ---");
        sender.sendMessage(ChatColor.YELLOW + "Side: " + ChatColor.WHITE + forceUser.getSide().name());
        sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.AQUA + forceUser.getForceLevel());

        double currentXp = forceUser.getForceXp();
        double neededXp = plugin.getLevelingManager().getXpForNextLevel(forceUser.getForceLevel());
        sender.sendMessage(String.format(ChatColor.YELLOW + "XP: " + ChatColor.WHITE + "%.1f / %.1f", currentXp, neededXp));

        sender.sendMessage(ChatColor.YELLOW + "Available Points: " + ChatColor.GREEN + forceUser.getForcePoints());
        sender.sendMessage(ChatColor.GOLD + "--------------------------");

        return true;
    }
}