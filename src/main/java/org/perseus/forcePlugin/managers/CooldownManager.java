package org.perseus.forcePlugin.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public void setCooldown(Player player, String abilityId, double seconds) {
        if (seconds <= 0) {
            return;
        }
        long expirationTime = System.currentTimeMillis() + (long) (seconds * 1000);
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
}