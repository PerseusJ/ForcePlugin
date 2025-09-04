package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceCommand implements CommandExecutor {

    private final ForceUserManager userManager;

    public ForceCommand(ForceUserManager userManager) {
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

        if (args.length == 2 && args[0].equalsIgnoreCase("choose")) {
            if (forceUser.getSide() != ForceSide.NONE) {
                player.sendMessage(ChatColor.RED + "You have already chosen your path. You cannot change it.");
                return true;
            }

            String chosenSideArg = args[1].toUpperCase();
            try {
                ForceSide chosenSide = ForceSide.valueOf(chosenSideArg);

                if (chosenSide == ForceSide.LIGHT) {
                    forceUser.setSide(ForceSide.LIGHT);
                    player.sendMessage(ChatColor.AQUA + "You have embraced the Light Side of the Force.");
                    player.sendMessage(ChatColor.GRAY + "Your path is set.");
                } else if (chosenSide == ForceSide.DARK) {
                    forceUser.setSide(ForceSide.DARK);
                    player.sendMessage(ChatColor.RED + "You have succumbed to the Dark Side of the Force.");
                    player.sendMessage(ChatColor.GRAY + "Your path is set.");
                } else {
                    player.sendMessage(ChatColor.GRAY + "That is not a valid path to choose.");
                }

            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.YELLOW + "Invalid side. Usage: /force choose <light|dark>");
            }

        } else {
            player.sendMessage(ChatColor.YELLOW + "Usage: /force choose <light|dark>");
        }

        return true;
    }
}