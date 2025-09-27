package org.perseus.forcePlugin.managers;

import org.bukkit.entity.Player;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.PassiveAbility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private final ForcePlugin plugin; // We need the plugin instance to access other managers

    public CooldownManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void setCooldown(Player player, String abilityId, double seconds) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        double finalSeconds = seconds;

        // --- NEW: Check for "Flowing Serenity" passive ---
        if (forceUser != null && forceUser.getSpecialization() != null && forceUser.getSpecialization().equals("CONSULAR")) {
            int rank = forceUser.getPassiveRank("FLOWING_SERENITY");
            if (rank > 0) {
                PassiveAbility passive = findPassive(forceUser.getSpecialization(), "FLOWING_SERENITY");
                if (passive != null) {
                    double multiplier = passive.getValue(rank);
                    finalSeconds *= multiplier; // Apply the reduction
                }
            }
        }
        // --- END NEW ---

        if (finalSeconds <= 0) {
            return;
        }
        long expirationTime = System.currentTimeMillis() + (long) (finalSeconds * 1000);
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(abilityId, expirationTime);
    }

    public boolean isOnCooldown(Player player, String abilityId) {
        return getRemainingCooldownMillis(player, abilityId) > 0;
    }

    public String getRemainingCooldownFormatted(Player player, String abilityId) {
        long millis = getRemainingCooldownMillis(player, abilityId);
        if (millis <= 0) {
            return "Ready";
        }
        return String.format("%.1fs", millis / 1000.0);
    }

    private long getRemainingCooldownMillis(Player player, String abilityId) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }
        Long expirationTime = playerCooldowns.get(abilityId);
        if (expirationTime == null) {
            return 0;
        }
        long remainingTime = expirationTime - System.currentTimeMillis();
        return Math.max(0, remainingTime);
    }

    private PassiveAbility findPassive(String specId, String passiveId) {
        return plugin.getPassiveManager().getPassivesForSpec(specId).stream()
                .filter(p -> p.getId().equals(passiveId))
                .findFirst().orElse(null);
    }
}