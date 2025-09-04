package org.perseus.forcePlugin;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Manages ability cooldowns for all players.
 * This prevents players from spamming abilities.
 */
public class CooldownManager {

    // The main data structure.
    // Key: The player's UUID.
    // Value: Another map that stores the cooldowns for that specific player.
    //      Inner Key: The unique ID of the ability (e.g., "FORCE_PUSH").
    //      Inner Value: The system time (in milliseconds) when the cooldown expires.
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    /**
     * Puts an ability on cooldown for a specific player.
     * @param player The player to set the cooldown for.
     * @param abilityId The unique ID of the ability to put on cooldown.
     * @param seconds The duration of the cooldown in seconds.
     */
    public void setCooldown(Player player, String abilityId, double seconds) {
        if (seconds <= 0) {
            return; // No cooldown to set.
        }
        // Calculate the exact time in the future when the cooldown will expire.
        long expirationTime = System.currentTimeMillis() + (long) (seconds * 1000);

        // Get the player's personal map of cooldowns, or create a new one if it doesn't exist.
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(abilityId, expirationTime);
    }

    /**
     * Checks if a specific ability is currently on cooldown for a player.
     * @param player The player to check.
     * @param abilityId The unique ID of the ability to check.
     * @return True if the ability is on cooldown, false otherwise.
     */
    public boolean isOnCooldown(Player player, String abilityId) {
        return getRemainingCooldownMillis(player, abilityId) > 0;
    }

    /**
     * Gets the remaining cooldown time for an ability in a human-readable format (e.g., "3.2s").
     * @param player The player to check.
     * @param abilityId The unique ID of the ability to check.
     * @return A formatted string with the remaining time.
     */
    public String getRemainingCooldownFormatted(Player player, String abilityId) {
        long millis = getRemainingCooldownMillis(player, abilityId);
        if (millis <= 0) {
            return "Ready";
        }
        // Format the milliseconds into seconds with one decimal place.
        return String.format("%.1fs", millis / 1000.0);
    }

    /**
     * The core logic for getting the remaining cooldown time in milliseconds.
     * @param player The player to check.
     * @param abilityId The unique ID of the ability to check.
     * @return The remaining time in milliseconds, or 0 if the cooldown has expired.
     */
    private long getRemainingCooldownMillis(Player player, String abilityId) {
        // Get the player's personal cooldown map. If they have no cooldowns, return 0.
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }

        // Get the expiration time for the specific ability. If it's not on cooldown, return 0.
        Long expirationTime = playerCooldowns.get(abilityId);
        if (expirationTime == null) {
            return 0;
        }

        // Calculate the difference between the expiration time and the current time.
        long remainingTime = expirationTime - System.currentTimeMillis();

        // If the time has already passed, the result will be negative. In that case, return 0.
        return Math.max(0, remainingTime);
    }
}