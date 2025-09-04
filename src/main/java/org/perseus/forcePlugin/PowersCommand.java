package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PowersCommand implements CommandExecutor {

    private final ForceUserManager userManager;

    public PowersCommand(ForceUserManager userManager) {
        this.userManager = userManager;
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
        ForceUser forceUser = userManager.getForceUser(player);

        if (forceUser == null) {
            player.sendMessage(ChatColor.RED + "Error: Your data could not be found. Please try relogging.");
            return true;
        }

        if (forceUser.getSide() == ForceSide.NONE) {
            player.sendMessage(ChatColor.RED + "You must first choose a side with /force choose <side>.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                if (forceUser.arePowersActive()) {
                    player.sendMessage(ChatColor.YELLOW + "Your powers are already active.");
                } else {
                    forceUser.setPowersActive(true);
                    player.sendMessage(ChatColor.GREEN + "Your Force powers are now active.");
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!forceUser.arePowersActive()) {
                    player.sendMessage(ChatColor.YELLOW + "Your powers are already dormant.");
                } else {
                    forceUser.setPowersActive(false);
                    player.sendMessage(ChatColor.YELLOW + "Your Force powers are now dormant.");
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "Usage: /powers <on|off>");
            }
        } else {
            player.sendMessage(ChatColor.GRAY + "Usage: /powers <on|off>");
        }

        return true;
    }
}