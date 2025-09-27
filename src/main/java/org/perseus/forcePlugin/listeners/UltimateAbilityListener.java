package org.perseus.forcePlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UltimateAbilityListener implements Listener {

    private final ForcePlugin plugin;
    public static final List<String> ULTIMATE_ABILITY_IDS = Arrays.asList(
            "FORCE_ABSORB", "FORCE_CAMOUFLAGE", "FORCE_SERENITY",
            "UNSTOPPABLE_VENGEANCE", "MARK_OF_THE_HUNT", "CHAIN_LIGHTNING"
    );

    private static final Map<UUID, Double> absorbingPlayers = new HashMap<>();
    private static final Set<UUID> camouflagedPlayers = new HashSet<>();
    private static final Map<UUID, Double> vengefulPlayers = new HashMap<>();
    private static final Set<UUID> markedEntities = new HashSet<>();

    public UltimateAbilityListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    // --- Force Absorb Logic ---
    public static void addAbsorbingPlayer(UUID uuid) { absorbingPlayers.put(uuid, 0.0); }
    public static double removeAbsorbingPlayer(UUID uuid) {
        return absorbingPlayers.remove(uuid) != null ? absorbingPlayers.getOrDefault(uuid, 0.0) : 0.0;
    }

    @EventHandler
    public void onPlayerAbsorbDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (absorbingPlayers.containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
            double currentAbsorbed = absorbingPlayers.get(event.getEntity().getUniqueId());
            absorbingPlayers.put(event.getEntity().getUniqueId(), currentAbsorbed + event.getDamage());
        }
    }

    // --- Force Camouflage Logic ---
    public static void addCamouflagedPlayer(UUID uuid) { camouflagedPlayers.add(uuid); }

    @EventHandler
    public void onAbilityFromCamouflage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        if (camouflagedPlayers.contains(damager.getUniqueId())) {
            camouflagedPlayers.remove(damager.getUniqueId());
            damager.removePotionEffect(PotionEffectType.INVISIBILITY);
            double multiplier = plugin.getAbilityConfigManager().getDoubleValue("FORCE_CAMOUFLAGE", 1, "bonus-damage-multiplier", 1.5);
            event.setDamage(event.getDamage() * multiplier);
        }
    }

    // --- Unstoppable Vengeance Logic ---
    public static void addVengefulPlayer(UUID uuid) { vengefulPlayers.put(uuid, 1.0); }
    public static void removeVengefulPlayer(UUID uuid) { vengefulPlayers.remove(uuid); }

    @EventHandler
    public void onVengeanceDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (vengefulPlayers.containsKey(event.getEntity().getUniqueId())) {
            double currentMultiplier = vengefulPlayers.get(event.getEntity().getUniqueId());
            double increase = plugin.getAbilityConfigManager().getDoubleValue("UNSTOPPABLE_VENGEANCE", 1, "damage-increase-per-heart", 0.1) * (event.getDamage() / 2);
            vengefulPlayers.put(event.getEntity().getUniqueId(), currentMultiplier + increase);
        }
    }

    @EventHandler
    public void onVengeanceDamageDealt(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (vengefulPlayers.containsKey(event.getDamager().getUniqueId())) {
            event.setDamage(event.getDamage() * vengefulPlayers.get(event.getDamager().getUniqueId()));
        }
    }

    // --- Mark of the Hunt Logic ---
    public static void addMarkedEntity(UUID uuid) { markedEntities.add(uuid); }

    // --- THE FIX: The missing helper method ---
    public static boolean isMarked(UUID uuid) {
        return markedEntities.contains(uuid);
    }
    // --- END FIX ---

    @EventHandler
    public void onMarkedDamage(EntityDamageByEntityEvent event) {
        if (markedEntities.contains(event.getEntity().getUniqueId())) {
            double multiplier = plugin.getAbilityConfigManager().getDoubleValue("MARK_OF_THE_HUNT", 1, "damage-multiplier", 1.2);
            event.setDamage(event.getDamage() * multiplier);
            if (event.getEntity().isDead() || !event.getEntity().isValid()) {
                markedEntities.remove(event.getEntity().getUniqueId());
            }
        }
    }
}