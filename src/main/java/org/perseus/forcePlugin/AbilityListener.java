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

    public AbilityListener(ForceUserManager userManager, AbilityManager abilityManager, CooldownManager cooldownManager, ForceBarManager forceBarManager, TelekinesisManager telekinesisManager) {
        this.userManager = userManager;
        this.abilityManager = abilityManager;
        this.cooldownManager = cooldownManager;
        this.forceBarManager = forceBarManager;
        this.telekinesisManager = telekinesisManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ForceUser forceUser = userManager.getForceUser(player);

        if (forceUser == null || !forceUser.arePowersActive()) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR) return;

        Action action = event.getAction();

        if (telekinesisManager.isLifting(player)) {
            event.setCancelled(true);
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                telekinesisManager.stopLifting(player, false);
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                telekinesisManager.launch(player);
            }
            return;
        }

        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) return;

        int selectedSlot = player.getInventory().getHeldItemSlot();
        if (selectedSlot > 2) return;

        String abilityId = forceUser.getBoundAbility(selectedSlot + 1);
        if (abilityId == null) return;

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
    }
}