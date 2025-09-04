package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilitiesCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public AbilitiesCommand(ForcePlugin plugin) {
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
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);

        if (forceUser == null) {
            player.sendMessage(ChatColor.RED + "Error: Your data could not be found. Please try relogging.");
            return true;
        }

        if (forceUser.getSide() == ForceSide.NONE) {
            player.sendMessage(ChatColor.RED + "You must first choose a side with /force choose <side>.");
            return true;
        }

        plugin.getGuiManager().openAbilityGUI(player);

        return true;
    }
}