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
        Action action = event.getAction();

        // We only care about left-clicks for all abilities now.
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        // --- NEW, SIMPLIFIED TELEKINESIS LOGIC ---
        // If the player is already lifting something, the next left-click will launch it.
        if (telekinesisManager.isLifting(player)) {
            event.setCancelled(true); // Prevent hitting the entity.
            telekinesisManager.launch(player);
            return; // Stop processing here.
        }
        // --- END NEW LOGIC ---

        // If not using Telekinesis, proceed with normal ability activation.
        ForceUser forceUser = userManager.getForceUser(player);
        if (forceUser == null || !forceUser.arePowersActive()) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR) return;

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