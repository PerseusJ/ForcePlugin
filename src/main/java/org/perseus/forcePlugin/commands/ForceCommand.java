package org.perseus.forcePlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.HolocronManager;
import org.perseus.forcePlugin.managers.ForceUserManager;

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
        HolocronManager holocronManager = plugin.getHolocronManager();
        ForceUser forceUser = userManager.getForceUser(player);

        if (forceUser == null) {
            player.sendMessage(ChatColor.RED + "Error: Your data is still loading. Please wait a moment and try again.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("choose")) {
            if (forceUser.getSide() != ForceSide.NONE) {
                player.sendMessage(ChatColor.RED + "You have already chosen your path. You cannot change it.");
                return true;
            }
            plugin.getGuiManager().openChooseSideGUI(player);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Usage: /force choose");
        }
        return true;
    }
}