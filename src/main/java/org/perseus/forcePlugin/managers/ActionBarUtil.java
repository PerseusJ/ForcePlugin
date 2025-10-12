package org.perseus.forcePlugin.managers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ActionBarUtil {

	private ActionBarUtil() {}

	@SuppressWarnings("deprecation")
	public static void send(Player player, String message) {
		if (player == null) return;
		if (message == null) message = "";
		try {
			// Paper/Adventure (if available in the runtime): player.sendActionBar(Component.text(message))
			// We avoid hard compile deps; fallback to Spigot's API
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
		} catch (Throwable ignored) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
}


