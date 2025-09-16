package org.perseus.forcePlugin.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForceUser {

    private final UUID uuid;
    private ForceSide side;
    private double currentForceEnergy;
    private int forceLevel;
    private double forceXp;
    private int forcePoints;
    private final Map<String, Integer> unlockedAbilities;
    private String activeAbilityId;

    public ForceUser(UUID uuid) {
        this.uuid = uuid;
        this.side = ForceSide.NONE;
        this.currentForceEnergy = 100.0;
        this.forceLevel = 1;
        this.forceXp = 0.0;
        this.forcePoints = 0;
        this.unlockedAbilities = new HashMap<>();
        this.activeAbilityId = null;
    }

    // --- GETTERS ---
    public UUID getUuid() { return uuid; }
    public ForceSide getSide() { return side; }
    public double getCurrentForceEnergy() { return currentForceEnergy; }
    public int getForceLevel() { return forceLevel; }
    public double getForceXp() { return forceXp; }
    public int getForcePoints() { return forcePoints; }
    public Map<String, Integer> getUnlockedAbilities() { return unlockedAbilities; }
    public boolean hasUnlockedAbility(String abilityId) { return unlockedAbilities.containsKey(abilityId); }
    public int getAbilityLevel(String abilityId) { return unlockedAbilities.getOrDefault(abilityId, 0); }
    public String getActiveAbilityId() { return activeAbilityId; }

    // --- SETTERS ---
    public void setSide(ForceSide side) { this.side = side; }
    public void setCurrentForceEnergy(double currentForceEnergy) { this.currentForceEnergy = Math.max(0, Math.min(100, currentForceEnergy)); }
    public void setForceLevel(int forceLevel) { this.forceLevel = forceLevel; }
    public void setForceXp(double forceXp) { this.forceXp = forceXp; }
    public void setForcePoints(int forcePoints) { this.forcePoints = forcePoints; }
    public void addForcePoints(int amount) { this.forcePoints += amount; }
    public void unlockAbility(String abilityId) {
        if (!unlockedAbilities.containsKey(abilityId)) {
            unlockedAbilities.put(abilityId, 1);
        }
    }
    public void upgradeAbility(String abilityId) {
        if (hasUnlockedAbility(abilityId)) {
            int currentLevel = getAbilityLevel(abilityId);
            unlockedAbilities.put(abilityId, currentLevel + 1);
        }
    }
    public void setActiveAbilityId(String activeAbilityId) { this.activeAbilityId = activeAbilityId; }
}