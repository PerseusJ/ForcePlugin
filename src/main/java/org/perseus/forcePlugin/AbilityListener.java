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

    private final ForceUserManager userManager;
    private final AbilityManager abilityManager;
    private final CooldownManager cooldownManager;
    private final ForceBarManager forceBarManager;

    public AbilityListener(ForceUserManager userManager, AbilityManager abilityManager, CooldownManager cooldownManager, ForceBarManager forceBarManager) {
        this.userManager = userManager;
        this.abilityManager = abilityManager;
        this.cooldownManager = cooldownManager;
        this.forceBarManager = forceBarManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        // --- REVERTED TO ORIGINAL CHECK ---
        // The player's hand must be empty to use an ability.
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR) {
            return;
        }
        // --- END REVERT ---

        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || !forceUser.arePowersActive()) {
            return;
        }

        int selectedSlot = player.getInventory().getHeldItemSlot();
        if (selectedSlot > 2) {
            return;
        }

        String abilityId = forceUser.getBoundAbility(selectedSlot + 1);
        if (abilityId == null) {
            return;
        }

        Ability ability = abilityManager.getAbility(abilityId);
        if (ability == null) {
            return;
        }

        if (cooldownManager.isOnCooldown(player, ability.getID())) {
            String remaining = cooldownManager.getRemainingCooldownFormatted(player, ability.getID());
            // We can update this to use the action bar later if we want.
            player.sendMessage(ChatColor.RED + "You can use " + ability.getName() + " again in " + remaining + ".");
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
    }
}