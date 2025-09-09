package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceCommand implements CommandExecutor {

    private final ForceUserManager userManager;
    private final HolocronManager holocronManager; // --- NEW ---

    public ForceCommand(ForceUserManager userManager, HolocronManager holocronManager) {
        this.userManager = userManager;
        this.holocronManager = holocronManager; // --- NEW ---
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

            try {
                ForceSide chosenSide = ForceSide.valueOf(args[1].toUpperCase());
                if (chosenSide == ForceSide.LIGHT || chosenSide == ForceSide.DARK) {
                    forceUser.setSide(chosenSide);
                    if (chosenSide == ForceSide.LIGHT) {
                        player.sendMessage(ChatColor.AQUA + "You have embraced the Light Side of the Force.");
                    } else {
                        player.sendMessage(ChatColor.RED + "You have succumbed to the Dark Side of the Force.");
                    }
                    // --- NEW: Give the Holocron ---
                    holocronManager.giveHolocron(player);
                    player.sendMessage(ChatColor.YELLOW + "A Holocron has been added to your inventory. Hold it to channel the Force!");
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