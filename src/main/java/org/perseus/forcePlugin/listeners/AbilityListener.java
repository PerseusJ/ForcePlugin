package org.perseus.forcePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.*;

public class AbilityListener implements Listener {

    private final ForcePlugin plugin;

    public AbilityListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemInHand = event.getItem();

        TelekinesisManager telekinesisManager = plugin.getTelekinesisManager();
        ForceUserManager userManager = plugin.getForceUserManager();

        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (!plugin.getHolocronManager().isHolocron(itemInHand)) {
            return;
        }

        event.setCancelled(true);

        if (player.isSneaking()) {
            return;
        }

        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null) return;

        if (forceUser.needsToChoosePath()) {
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.RED + "You must choose your final path! Right-click your Holocron.");
            return;
        }

        if (telekinesisManager.isLifting(player)) {
            telekinesisManager.launch(player);
            double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-telekinesis-launch", 2.0);
            plugin.getLevelingManager().addXp(player, xpToGive);
            return;
        }

        String abilityId = forceUser.getActiveAbilityId();
        if (abilityId == null) {
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.YELLOW + "No ability selected.");
            return;
        }

        AbilityManager abilityManager = plugin.getAbilityManager();
        Ability ability = abilityManager.getAbility(abilityId);
        if (ability == null) return;

        if (!forceUser.hasUnlockedAbility(abilityId)) {
            player.sendMessage(ChatColor.RED + "A bug has occurred: Tried to use a locked ability.");
            return;
        }

        int abilityLevel = forceUser.getAbilityLevel(abilityId);
        CooldownManager cooldownManager = plugin.getCooldownManager();
        LevelingManager levelingManager = plugin.getLevelingManager();
        ForceBarManager forceBarManager = plugin.getForceBarManager();

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            String remaining = cooldownManager.getRemainingCooldownFormatted(player, ability.getID());
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.RED + ability.getName() + " is on cooldown: " + remaining);
            return;
        }

        if (forceUser.getCurrentForceEnergy() < ability.getEnergyCost(abilityLevel)) {
            org.perseus.forcePlugin.managers.ActionBarUtil.send(player, ChatColor.AQUA + "Not enough Force Energy!");
            return;
        }

        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - ability.getEnergyCost(abilityLevel));
        cooldownManager.setCooldown(player, ability.getID(), ability.getCooldown(abilityLevel));
        ability.execute(player, forceUser);
        forceBarManager.updateBar(player);

        double xpToGive = plugin.getConfig().getDouble("progression.xp-gain.per-ability-use", 1.0);
        levelingManager.addXp(player, xpToGive);
    }

    @EventHandler
    public void onAbilityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        // --- Siphoning Strikes Logic ---
        if (forceUser.hasUnlockedPassive("SIPHONING_STRIKES")) {
            int level = forceUser.getPassiveLevel("SIPHONING_STRIKES");
            double chance = plugin.getPassiveManager().getPassiveDoubleValue("SIPHONING_STRIKES", level, "lifesteal-chance", 10.0);
            if (Math.random() * 100 < chance) {
                double percent = plugin.getPassiveManager().getPassiveDoubleValue("SIPHONING_STRIKES", level, "lifesteal-percent", 25.0);
                double healAmount = event.getDamage() * (percent / 100.0);
                player.setHealth(Math.min(org.perseus.forcePlugin.managers.HealthUtil.getMaxHealth(player), player.getHealth() + healAmount));
            }
        }

        // --- Corrupted Energy Logic ---
        if (forceUser.hasUnlockedPassive("CORRUPTED_ENERGY")) {
            int level = forceUser.getPassiveLevel("CORRUPTED_ENERGY");
            double chance = plugin.getPassiveManager().getPassiveDoubleValue("CORRUPTED_ENERGY", level, "wither-chance", 15.0);
            if (Math.random() * 100 < chance) {
                int duration = plugin.getPassiveManager().getPassiveIntValue("CORRUPTED_ENERGY", level, "wither-duration", 3) * 20;
                int amplifier = plugin.getPassiveManager().getPassiveIntValue("CORRUPTED_ENERGY", level, "wither-amplifier", 1) - 1;
                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, amplifier));
            }
        }
    }

    
}