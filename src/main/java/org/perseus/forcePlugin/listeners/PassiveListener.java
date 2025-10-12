package org.perseus.forcePlugin.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceUser;

public class PassiveListener implements Listener {

    private final ForcePlugin plugin;

    public PassiveListener(ForcePlugin plugin) {
        this.plugin = plugin;
        startSoothingPresenceTask();
    }

    // --- Soothing Presence ---
    private void startSoothingPresenceTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
                    if (forceUser != null && forceUser.hasUnlockedPassive("SOOTHING_PRESENCE")) {
                        int level = forceUser.getPassiveLevel("SOOTHING_PRESENCE");
                        double healAmount = plugin.getPassiveManager().getPassiveDoubleValue("SOOTHING_PRESENCE", level, "heal-amount", 0.25) * 2;
                        if (player.getHealth() < org.perseus.forcePlugin.managers.HealthUtil.getMaxHealth(player)) {
                            player.setHealth(Math.min(org.perseus.forcePlugin.managers.HealthUtil.getMaxHealth(player), player.getHealth() + healAmount));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L); // Runs every 5 seconds (100 ticks)
    }

    // --- Deflecting Palm ---
    @EventHandler
    public void onMeleeDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);

        if (forceUser != null && forceUser.hasUnlockedPassive("DEFLECTING_PALM")) {
            int level = forceUser.getPassiveLevel("DEFLECTING_PALM");
            double chance = plugin.getPassiveManager().getPassiveDoubleValue("DEFLECTING_PALM", level, "negate-chance", 5.0);
            if (Math.random() * 100 < chance) {
                event.setCancelled(true);
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.5f);
            }
        }
    }

    // --- Force Ferocity ---
    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player player = event.getEntity().getKiller();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);

        if (forceUser != null && forceUser.hasUnlockedPassive("FORCE_FEROCITY")) {
            int level = forceUser.getPassiveLevel("FORCE_FEROCITY");
            int duration = plugin.getPassiveManager().getPassiveIntValue("FORCE_FEROCITY", level, "duration-seconds", 3) * 20;
            int amplifier = plugin.getPassiveManager().getPassiveIntValue("FORCE_FEROCITY", level, "speed-amplifier", 1) - 1;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier));
        }
    }
}