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
    private String specialization;
    private boolean needsToChoosePath;

    // --- NEW: Map to store unlocked passive abilities and their levels ---
    private final Map<String, Integer> unlockedPassives;

    // --- NEW: Fields for Force Enchanting debuff ---
    private double forceRegenModifier;
    private long regenDebuffExpiry;

    public ForceUser(UUID uuid) {
        this.uuid = uuid;
        this.side = ForceSide.NONE;
        this.currentForceEnergy = 100.0;
        this.forceLevel = 1;
        this.forceXp = 0.0;
        this.forcePoints = 0;
        this.unlockedAbilities = new HashMap<>();
        this.activeAbilityId = null;
        this.specialization = null;
        this.needsToChoosePath = false;
        this.unlockedPassives = new HashMap<>(); // Initialize new map
        this.forceRegenModifier = 1.0;
        this.regenDebuffExpiry = 0L;
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
    public String getSpecialization() { return specialization; }
    public boolean needsToChoosePath() { return needsToChoosePath; }

    // --- NEW: Getters for passives ---
    public Map<String, Integer> getUnlockedPassives() { return unlockedPassives; }
    public boolean hasUnlockedPassive(String passiveId) { return unlockedPassives.containsKey(passiveId); }
    public int getPassiveLevel(String passiveId) { return unlockedPassives.getOrDefault(passiveId, 0); }
    public double getForceRegenModifier() { return forceRegenModifier; }
    public long getRegenDebuffExpiry() { return regenDebuffExpiry; }


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
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setNeedsToChoosePath(boolean needsToChoosePath) { this.needsToChoosePath = needsToChoosePath; }
    public void setForceRegenModifier(double forceRegenModifier) { this.forceRegenModifier = forceRegenModifier; }
    public void setRegenDebuffExpiry(long regenDebuffExpiry) { this.regenDebuffExpiry = regenDebuffExpiry; }

    // --- NEW: Setters for passives ---
    public void unlockPassive(String passiveId) {
        if (!unlockedPassives.containsKey(passiveId)) {
            unlockedPassives.put(passiveId, 1);
        }
    }
    public void upgradePassive(String passiveId) {
        if (hasUnlockedPassive(passiveId)) {
            int currentLevel = getPassiveLevel(passiveId);
            unlockedPassives.put(passiveId, currentLevel + 1);
        }
    }
}