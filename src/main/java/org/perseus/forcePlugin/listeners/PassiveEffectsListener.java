package org.perseus.forcePlugin.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent; // --- THE FIX: This line was missing ---
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.PassiveAbility;
import org.perseus.forcePlugin.managers.PassiveManager;

import java.util.UUID;

public class PassiveEffectsListener implements Listener {

    private final ForcePlugin plugin;

    public PassiveEffectsListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            applyPermanentPassives(player);
        }, 20L);
    }

    @EventHandler
    public void onDamageDealt(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player attacker = (Player) event.getDamager();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(attacker);
        if (forceUser == null || forceUser.getSpecialization() == null) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            int focusedStrikesRank = forceUser.getPassiveRank("FOCUSED_STRIKES");
            if (focusedStrikesRank > 0) {
                PassiveAbility passive = findPassive(forceUser.getSpecialization(), "FOCUSED_STRIKES");
                if (passive != null) {
                    double chance = passive.getValue(focusedStrikesRank);
                    if (Math.random() < chance) {
                        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() + 5.0);
                        plugin.getForceBarManager().updateBar(attacker);
                    }
                }
            }
        }

        handleLifesteal(attacker, forceUser, event.getFinalDamage());
        handleConditionalDamage(forceUser, (LivingEntity) event.getEntity(), event);
        handleSwiftPursuit(attacker, forceUser, (LivingEntity) event.getEntity());
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player killer = event.getEntity().getKiller();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(killer);
        if (forceUser == null || forceUser.getSpecialization() == null) return;

        int thirstRank = forceUser.getPassiveRank("THIRST_FOR_BATTLE");
        if (thirstRank > 0) {
            PassiveAbility passive = findPassive(forceUser.getSpecialization(), "THIRST_FOR_BATTLE");
            if (passive != null) {
                int duration = (int) (passive.getValue(thirstRank) * 20);
                killer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, 0));
            }
        }
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getNewEffect() == null) return;

        Player player = (Player) event.getEntity();
        PotionEffectType type = event.getNewEffect().getType();

        if (type.equals(PotionEffectType.SLOW) || type.equals(PotionEffectType.WEAKNESS)) {
            ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
            if (forceUser == null || forceUser.getSpecialization() == null || !forceUser.getSpecialization().equals("GUARDIAN")) return;

            int rank = forceUser.getPassiveRank("GUARDIANS_RESOLVE");
            if (rank > 0) {
                PassiveAbility passive = findPassive(forceUser.getSpecialization(), "GUARDIANS_RESOLVE");
                if (passive != null) {
                    double resistance = passive.getValue(rank);
                    int originalDuration = event.getNewEffect().getDuration();
                    int newDuration = (int) (originalDuration * (1.0 - resistance));
                    event.setCancelled(true);
                    player.addPotionEffect(new PotionEffect(type, newDuration, event.getNewEffect().getAmplifier(), event.getNewEffect().isAmbient(), event.getNewEffect().hasParticles()));
                }
            }
        }
    }

    public void applyPermanentPassives(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        handleAttributePassive(player, forceUser, "FORCE_VIGOR", Attribute.GENERIC_MAX_HEALTH, "ForceVigor", AttributeModifier.Operation.ADD_NUMBER, 2.0);
        handleAttributePassive(player, forceUser, "RESILIENCE", Attribute.GENERIC_ARMOR, "ForceResilience", AttributeModifier.Operation.ADD_NUMBER, 1.0);
        handleAttributePassive(player, forceUser, "SWIFTNESS", Attribute.GENERIC_MOVEMENT_SPEED, "ForceSwiftness", AttributeModifier.Operation.ADD_SCALAR, 1.0);
    }

    private void handleAttributePassive(Player player, ForceUser user, String passiveId, Attribute attribute, String modifierName, AttributeModifier.Operation operation, double valueMultiplier) {
        for (AttributeModifier modifier : player.getAttribute(attribute).getModifiers()) {
            if (modifier.getName().equals(modifierName)) {
                player.getAttribute(attribute).removeModifier(modifier);
            }
        }
        int rank = user.getPassiveRank(passiveId);
        if (rank > 0) {
            PassiveAbility passive = findPassive(user.getSpecialization(), passiveId);
            if (passive != null) {
                double value = passive.getValue(rank);
                double finalValue = (attribute == Attribute.GENERIC_MOVEMENT_SPEED) ? value : value * valueMultiplier;
                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), modifierName, finalValue, operation);
                player.getAttribute(attribute).addModifier(mod);
            }
        }
    }

    private void handleLifesteal(Player attacker, ForceUser user, double damageDealt) {
        int rank = user.getPassiveRank("DARK_SUSTENANCE");
        if (rank > 0) {
            PassiveAbility passive = findPassive(user.getSpecialization(), "DARK_SUSTENANCE");
            if (passive != null) {
                double lifestealPercent = passive.getValue(rank);
                double healthToRestore = damageDealt * lifestealPercent;
                double maxHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                double newHealth = Math.min(attacker.getHealth() + healthToRestore, maxHealth);
                attacker.setHealth(newHealth);
            }
        }
    }

    private void handleConditionalDamage(ForceUser user, LivingEntity target, EntityDamageByEntityEvent event) {
        String passiveId = null;
        if (user.getSpecialization() != null) {
            if (user.getSpecialization().equals("SENTINEL")) passiveId = "OPPORTUNIST";
            if (user.getSpecialization().equals("WARRIOR")) passiveId = "SHATTERING_STRIKES";
        }
        if (passiveId == null) return;

        int rank = user.getPassiveRank(passiveId);
        if (rank > 0) {
            if (target.hasPotionEffect(PotionEffectType.SLOW) || target.hasPotionEffect(PotionEffectType.LEVITATION)) {
                PassiveAbility passive = findPassive(user.getSpecialization(), passiveId);
                if (passive != null) {
                    double multiplier = passive.getValue(rank);
                    event.setDamage(event.getDamage() * multiplier);
                }
            }
        }
    }

    private void handleSwiftPursuit(Player attacker, ForceUser user, LivingEntity target) {
        int rank = user.getPassiveRank("SWIFT_PURSUIT");
        if (rank > 0) {
            if (UltimateAbilityListener.isMarked(target.getUniqueId())) {
                PassiveAbility passive = findPassive(user.getSpecialization(), "SWIFT_PURSUIT");
                if (passive != null) {
                    int duration = (int) (passive.getValue(rank) * 20);
                    attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0));
                }
            }
        }
    }

    private PassiveAbility findPassive(String specId, String passiveId) {
        PassiveManager passiveManager = plugin.getPassiveManager();
        return passiveManager.getPassivesForSpec(specId).stream()
                .filter(p -> p.getId().equals(passiveId))
                .findFirst().orElse(null);
    }
}