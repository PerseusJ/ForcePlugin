package org.perseus.forcePlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A data class that holds all plugin-specific information for a single player.
 */
public class ForceUser {

    // --- Core Player Identity ---
    private final UUID uuid;

    // --- Player Choices & State ---
    private ForceSide side;
    private boolean powersActive;
    private double currentForceEnergy;

    // --- Ability Management ---
    private final Map<Integer, String> boundAbilities;

    // --- NEW: RPG Progression Fields ---
    private int forceLevel;
    private double forceXp;
    private int forcePoints;
    private final List<String> unlockedAbilities;
    // --- END NEW ---

    /**
     * Constructor for a new ForceUser.
     * @param uuid The UUID of the player this object belongs to.
     */
    public ForceUser(UUID uuid) {
        this.uuid = uuid;
        this.side = ForceSide.NONE;
        this.powersActive = false;
        this.currentForceEnergy = 100.0;
        this.boundAbilities = new HashMap<>();

        // --- NEW: Initialize RPG fields with default values ---
        this.forceLevel = 1;
        this.forceXp = 0.0;
        this.forcePoints = 0; // Start with 0 points, maybe give 1 at level 1? For now, 0.
        this.unlockedAbilities = new ArrayList<>();
        // --- END NEW ---
    }

    // --- GETTERS (for retrieving information) ---

    public UUID getUuid() { return uuid; }
    public ForceSide getSide() { return side; }
    public boolean arePowersActive() { return powersActive; }
    public double getCurrentForceEnergy() { return currentForceEnergy; }
    public String getBoundAbility(int slot) { return boundAbilities.get(slot); }

    // --- NEW: Getters for RPG fields ---
    public int getForceLevel() { return forceLevel; }
    public double getForceXp() { return forceXp; }
    public int getForcePoints() { return forcePoints; }
    public List<String> getUnlockedAbilities() { return unlockedAbilities; }
    public boolean hasUnlockedAbility(String abilityId) { return unlockedAbilities.contains(abilityId); }
    // --- END NEW ---


    // --- SETTERS (for modifying information) ---

    public void setSide(ForceSide side) { this.side = side; }
    public void setPowersActive(boolean powersActive) { this.powersActive = powersActive; }
    public void setCurrentForceEnergy(double currentForceEnergy) { this.currentForceEnergy = Math.max(0, Math.min(100, currentForceEnergy)); }
    public void setBoundAbility(int slot, String abilityId) {
        if (abilityId == null) {
            boundAbilities.remove(slot);
        } else {
            boundAbilities.put(slot, abilityId);
        }
    }

    // --- NEW: Setters for RPG fields ---
    public void setForceLevel(int forceLevel) { this.forceLevel = forceLevel; }
    public void setForceXp(double forceXp) { this.forceXp = forceXp; }
    public void setForcePoints(int forcePoints) { this.forcePoints = forcePoints; }
    public void addForcePoints(int amount) { this.forcePoints += amount; }
    public void unlockAbility(String abilityId) {
        if (!unlockedAbilities.contains(abilityId)) {
            unlockedAbilities.add(abilityId);
        }
    }
    // --- END NEW ---
}