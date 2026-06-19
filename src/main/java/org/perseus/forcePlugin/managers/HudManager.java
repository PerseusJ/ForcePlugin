package org.perseus.forcePlugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HudManager implements Listener {

    private static final String[] ENTRIES = {
        "§0§0§0", "§0§0§1", "§0§0§2", "§0§0§3", "§0§0§4",
        "§0§0§5", "§0§0§6", "§0§0§7", "§0§0§8"
    };
    private static final String OBJECTIVE_NAME = "forceBinds";

    private final ForcePlugin plugin;
    private final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();
    private boolean enabled;
    private String title;

    public HudManager(ForcePlugin plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        this.enabled = plugin.getConfig().getBoolean("hud.enabled", true);
        this.title = ChatColor.translateAlternateColorCodes('&',
            plugin.getConfig().getString("hud.title", "&6Force Binds"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addPlayer(Player player) {
        if (!enabled) return;
        updateScoreboard(player);
    }

    public void removePlayer(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void removeAll() {
        for (UUID uuid : new ArrayList<>(playerScoreboards.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                removePlayer(player);
            }
        }
        playerScoreboards.clear();
    }

    public void updateScoreboard(Player player) {
        updateScoreboard(player, player.getInventory().getHeldItemSlot());
    }

    public void updateScoreboard(Player player, int currentSlot) {
        if (!enabled) return;
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = playerScoreboards.computeIfAbsent(
            player.getUniqueId(), k -> manager.getNewScoreboard());

        Objective obj = board.getObjective(OBJECTIVE_NAME);
        if (obj == null) {
            obj = board.registerNewObjective(OBJECTIVE_NAME, "dummy", title);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        for (int slot = 0; slot < 9; slot++) {
            String entry = ENTRIES[slot];

            String abilityId = forceUser.getBoundAbilityId(slot);
            String abilityName;
            if (abilityId != null) {
                Ability ability = plugin.getAbilityManager().getAbility(abilityId);
                abilityName = (ability != null) ? ability.getName() : abilityId;
            } else {
                abilityName = "Empty";
            }

            Team team = board.getTeam("s" + slot);
            if (team == null) {
                team = board.registerNewTeam("s" + slot);
                team.addEntry(entry);
            }

            if (slot == currentSlot) {
                team.setPrefix(ChatColor.GREEN + ">" + ChatColor.WHITE + (slot + 1)
                    + ": " + ChatColor.GOLD + abilityName);
            } else {
                String color = abilityId != null ? ChatColor.WHITE.toString()
                    : ChatColor.DARK_GRAY.toString();
                team.setPrefix(ChatColor.GRAY + " " + (slot + 1) + ": " + color + abilityName);
            }

            obj.getScore(entry).setScore(9 - slot);
        }

        player.setScoreboard(board);
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent event) {
        if (!enabled) return;
        updateScoreboard(event.getPlayer(), event.getNewSlot());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }
}
