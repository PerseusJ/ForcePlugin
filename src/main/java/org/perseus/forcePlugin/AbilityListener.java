package org.perseus.forcePlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.abilities.Ability;

public class AbilityListener implements Listener {

    private final TelekinesisManager telekinesisManager;
    private final ForceUserManager userManager;
    private final AbilityManager abilityManager;
    private final CooldownManager cooldownManager;
    private final ForceBarManager forceBarManager;
    private final LevelingManager levelingManager;

    public AbilityListener(ForceUserManager userManager, AbilityManager abilityManager, CooldownManager cooldownManager, ForceBarManager forceBarManager, TelekinesisManager telekinesisManager, LevelingManager levelingManager) {
        this.userManager = userManager;
        this.abilityManager = abilityManager;
        this.cooldownManager = cooldownManager;
        this.forceBarManager = forceBarManager;
        this.telekinesisManager = telekinesisManager;
        this.levelingManager = levelingManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (telekinesisManager.isLifting(player)) {
            event.setCancelled(true);
            telekinesisManager.launch(player);
            levelingManager.addXp(player, 2.0);
            return;
        }

        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || !forceUser.arePowersActive()) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR) return;

        int selectedSlot = player.getInventory().getHeldItemSlot();
        if (selectedSlot > 2) return;

        String abilityId = forceUser.getBoundAbility(selectedSlot + 1);
        if (abilityId == null) return;

        // --- NEW: Security check for unlocked abilities ---
        if (!forceUser.hasUnlockedAbility(abilityId)) {
            player.sendMessage(ChatColor.RED + "You have not unlocked this ability yet!");
            return;
        }
        // --- END NEW ---

        Ability ability = abilityManager.getAbility(abilityId);
        if (ability == null) return;

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            player.sendMessage(ChatColor.RED + "You can use " + ability.getName() + " again in " + cooldownManager.getRemainingCooldownFormatted(player, ability.getID()) + ".");
            return;
        }

        if (forceUser.getCurrentForceEnergy() < ability.getEnergyCost()) {
            player.sendMessage(ChatColor.AQUA + "You don't have enough Force Energy.");
            return;
        }

        forceUser.setCurrentForceEnergy(forceUser.getCurrentForceEnergy() - ability.getEnergyCost());
        cooldownManager.setCooldown(player, ability.getID(), ability.getCooldown());
        ability.execute(player);
        forceBarManager.updateBar(player);
        levelingManager.addXp(player, 1.0);
    }
}