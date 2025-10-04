package org.perseus.forcePlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.ForcePlugin;

public class ForceEnchantCommand implements CommandExecutor {

    private final ForcePlugin plugin;

    public ForceEnchantCommand(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getForceEnchantManager().canUse(player)) {
            player.sendMessage(ChatColor.RED + "You do not have the required rank to use this ability.");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You must be holding an item to enchant.");
            return true;
        }

        plugin.getGuiManager().openForceEnchantGUI(player);

        return true;
    }
}
