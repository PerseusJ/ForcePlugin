package org.perseus.forcePlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ForceTabCompleter implements TabCompleter {

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		String name = command.getName().toLowerCase(Locale.ROOT);
		switch (name) {
			case "force":
				return completeForce(sender, args);
			case "forcestats":
				return completeForceStats(sender, args);
			case "forceadmin":
				return completeForceAdmin(sender, args);
			case "forceenchant":
				return Collections.emptyList();
			default:
				return Collections.emptyList();
		}
	}

	private List<String> completeForce(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return prefixFilter(args[0], listOf("choose"));
		}
		return Collections.emptyList();
	}

	private List<String> completeForceStats(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return completePlayersPreferSelf(sender, args[0]);
		}
		return Collections.emptyList();
	}

	private List<String> completeForceAdmin(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return prefixFilter(args[0], listOf("reload","setside","reset","check","setlevel","givexp","givepoints","giveholocron"));
		}
		if (args.length >= 2) {
			String sub = args[0].toLowerCase(Locale.ROOT);
			switch (sub) {
				case "setside":
				case "reset":
				case "check":
				case "setlevel":
				case "givexp":
				case "givepoints":
				case "giveholocron":
					if (args.length == 2) {
						return completePlayersPreferSelf(sender, args[1]);
					}
					break;
			}
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setside")) {
					return prefixFilter(args[2], listOf("light","dark","none"));
				}
				if (args[0].equalsIgnoreCase("setlevel")) {
					return prefixFilter(args[2], listOf("1","5","10","20","30","40","50"));
				}
				if (args[0].equalsIgnoreCase("givexp")) {
					return prefixFilter(args[2], listOf("10","25","50","100","250","500"));
				}
				if (args[0].equalsIgnoreCase("givepoints")) {
					return prefixFilter(args[2], listOf("1","2","3","5","10","20"));
				}
			}
		}
		return Collections.emptyList();
	}

	private List<String> completePlayersPreferSelf(CommandSender sender, String prefix) {
		List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
		if (sender instanceof Player) {
			String self = ((Player) sender).getName();
			// Prioritize self at the front
			names.remove(self);
			names.add(0, self);
		}
		return prefixFilter(prefix, names);
	}

	private List<String> listOf(String... vals) {
		List<String> list = new ArrayList<>(vals.length);
		Collections.addAll(list, vals);
		return list;
	}

	private List<String> prefixFilter(String prefix, List<String> options) {
		String p = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
		return options.stream()
				.filter(s -> s.toLowerCase(Locale.ROOT).startsWith(p))
				.collect(Collectors.toList());
	}
}


