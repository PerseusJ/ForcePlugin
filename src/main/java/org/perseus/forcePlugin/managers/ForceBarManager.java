package org.perseus.forcePlugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForceBarManager {

    private final ForcePlugin plugin;
    private final ForceUserManager userManager;
    private final Map<UUID, BossBar> playerBars = new HashMap<>();
    private double regenAmountPerSecond;

    public ForceBarManager(ForcePlugin plugin, ForceUserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
        reloadConfig();
        startEnergyRegenTask();
    }

    public void reloadConfig() {
        this.regenAmountPerSecond = plugin.getConfig().getDouble("force-energy.regeneration-per-second", 2.5);
    }

    public void addPlayer(Player player) {
        ForceUser user = userManager.getForceUser(player);
        if (user == null) return;
        BossBar bar = Bukkit.createBossBar("Force Energy", BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(player);
        playerBars.put(player.getUniqueId(), bar);
        updateBar(player);
    }

    public void removePlayer(Player player) {
        BossBar bar = playerBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
        }
    }

    public void updateBar(Player player) {
        BossBar bar = playerBars.get(player.getUniqueId());
        ForceUser user = userManager.getForceUser(player);
        if (bar == null || user == null) return;

        if (user.getSide() == ForceSide.LIGHT) {
            bar.setColor(BarColor.BLUE);
        } else if (user.getSide() == ForceSide.DARK) {
            bar.setColor(BarColor.RED);
        } else {
            bar.setColor(BarColor.WHITE);
        }

        double progress = user.getCurrentForceEnergy() / 100.0;
        bar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
    }

    private void startEnergyRegenTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : playerBars.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        ForceUser user = userManager.getForceUser(player);
                        if (user != null && user.getSide() != ForceSide.NONE) {

                            // --- NEW: Check for and handle expired debuffs ---
                            if (user.getRegenDebuffExpiry() > 0 && System.currentTimeMillis() > user.getRegenDebuffExpiry()) {
                                user.setForceRegenModifier(1.0);
                                user.setRegenDebuffExpiry(0L);
                            }

                            double currentEnergy = user.getCurrentForceEnergy();
                            if (currentEnergy < 100.0) {
                                double finalRegen = regenAmountPerSecond;
                                // --- NEW: Add Meditation bonus ---
                                if (user.hasUnlockedPassive("MEDITATION")) {
                                    int level = user.getPassiveLevel("MEDITATION");
                                    finalRegen += plugin.getPassiveManager().getPassiveDoubleValue("MEDITATION", level, "regen-bonus", 0.5);
                                }

                                // --- NEW: Apply the debuff modifier ---
                                finalRegen *= user.getForceRegenModifier();

                                user.setCurrentForceEnergy(currentEnergy + finalRegen);
                                updateBar(player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}