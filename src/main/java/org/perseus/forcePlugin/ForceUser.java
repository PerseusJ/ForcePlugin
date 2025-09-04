package org.perseus.forcePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A data class that holds all plugin-specific information for a single player.
 * An instance of this class will be created for every player on the server.
 */
public class ForceUser {

    // --- Core Player Identity ---
    private final UUID uuid; // The player's unique Minecraft ID. Cannot be changed.

    // --- Player Choices & State ---
    private ForceSide side; // The player's chosen alignment (LIGHT, DARK, or NONE).
    private boolean powersActive; // Whether the player has their powers toggled on or off.
    private double currentForceEnergy; // The player's current energy, from 0 to 100.

    // --- Ability Management ---
    // Stores the abilities bound to the hotbar slots.
    // Key: The hotbar slot (e.g., 1, 2, 3).
    // Value: The unique ID of the ability (e.g., "FORCE_PUSH").
    private final Map<Integer, String> boundAbilities;

    /**
     * Constructor for a new ForceUser.
     * Sets the default values for a player when they first join or are created.
     * @param uuid The UUID of the player this object belongs to.
     */
    public ForceUser(UUID uuid) {
        this.uuid = uuid;
        this.side = ForceSide.NONE; // Players start with no alignment.
        this.powersActive = false; // Powers are off by default.
        this.currentForceEnergy = 100.0; // Players start with full energy.
        this.boundAbilities = new HashMap<>();
    }

    // --- GETTERS (for retrieving information) ---

    public UUID getUuid() {
        return uuid;
    }

    public ForceSide getSide() {
        return side;
    }

    public boolean arePowersActive() {
        return powersActive;
    }

    public double getCurrentForceEnergy() {
        return currentForceEnergy;
    }

    /**
     * Gets the unique ID of the ability bound to a specific slot.
     * @param slot The slot number (1, 2, or 3).
     * @return The ability ID as a String, or null if no ability is bound to that slot.
     */
    public String getBoundAbility(int slot) {
        return boundAbilities.get(slot);
    }

    // --- SETTERS (for modifying information) ---

    public void setSide(ForceSide side) {
        this.side = side;
    }

    public void setPowersActive(boolean powersActive) {
        this.powersActive = powersActive;
    }

    public void setCurrentForceEnergy(double currentForceEnergy) {
        // Clamps the value between 0 and 100 to prevent errors.
        this.currentForceEnergy = Math.max(0, Math.min(100, currentForceEnergy));
    }

    /**
     * Binds an ability's ID to a specific slot.
     * @param slot The slot number (1, 2, or 3).
     * @param abilityId The unique ID of the ability to bind.
     */
    public void setBoundAbility(int slot, String abilityId) {
        if (abilityId == null) {
            boundAbilities.remove(slot); // If the ID is null, unbind the ability.
        } else {
            boundAbilities.put(slot, abilityId);
        }
    }
}