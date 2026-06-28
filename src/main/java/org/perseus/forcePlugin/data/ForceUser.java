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
    private final Map<Integer, String> slotBinds;

    // Map to store unlocked passive abilities and their levels
    private final Map<String, Integer> unlockedPassives;

    // Fields for Force Enchanting debuff
    private double forceRegenModifier;
    private long regenDebuffExpiry;

    // Data loading flag to prevent ability use during async load
    private volatile boolean dataLoaded;

    public ForceUser(UUID uuid) {
        this.uuid = uuid;
        this.side = ForceSide.NONE;
        this.currentForceEnergy = 100.0;
        this.forceLevel = 1;
        this.forceXp = 0.0;
        this.forcePoints = 0;
        this.unlockedAbilities = new HashMap<>();
        this.slotBinds = new HashMap<>();
        this.unlockedPassives = new HashMap<>();
        this.forceRegenModifier = 1.0;
        this.regenDebuffExpiry = 0L;
        this.dataLoaded = false;
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
    public String getBoundAbilityId(int slot) { return slotBinds.get(slot); }
    public Map<Integer, String> getSlotBinds() { return slotBinds; }
    public void setSlotBind(int slot, String abilityId) {
        if (abilityId == null) {
            slotBinds.remove(slot);
        } else {
            slotBinds.put(slot, abilityId);
        }
    }
    public void clearSlotBinds() { slotBinds.clear(); }

    // Getters for passives
    public Map<String, Integer> getUnlockedPassives() { return unlockedPassives; }
    public boolean hasUnlockedPassive(String passiveId) { return unlockedPassives.containsKey(passiveId); }
    public int getPassiveLevel(String passiveId) { return unlockedPassives.getOrDefault(passiveId, 0); }
    public double getForceRegenModifier() { return forceRegenModifier; }
    public long getRegenDebuffExpiry() { return regenDebuffExpiry; }
    public boolean isDataLoaded() { return dataLoaded; }


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
    public void setForceRegenModifier(double forceRegenModifier) { this.forceRegenModifier = forceRegenModifier; }
    public void setRegenDebuffExpiry(long regenDebuffExpiry) { this.regenDebuffExpiry = regenDebuffExpiry; }
    public void setDataLoaded(boolean dataLoaded) { this.dataLoaded = dataLoaded; }

    // Setters for passives
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

    /**
     * Performs a full side switch for the player.
     * Clears all unlocked abilities, passives, slot binds, and Force Points,
     * then sets the new side. Force Level and XP are intentionally preserved.
     *
     * @param newSide The new ForceSide to assign.
     * @return The player's old ForceSide, for use in announcement messages.
     */
    public ForceSide performSideSwitch(ForceSide newSide) {
        ForceSide oldSide = this.side;
        this.side = newSide;
        unlockedAbilities.clear();
        unlockedPassives.clear();
        slotBinds.clear();
        this.forcePoints = 0;
        return oldSide;
    }
}