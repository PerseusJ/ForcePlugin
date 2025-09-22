package org.perseus.forcePlugin.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.Rank;

public class GUIListener implements Listener {

    private final ForcePlugin plugin;

    public GUIListener(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String viewTitle = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        if (viewTitle.equals(GUIManager.ABILITY_GUI_TITLE)) {
            handleAbilitySelection(event, player);
        } else if (viewTitle.startsWith(GUIManager.UPGRADE_GUI_TITLE_PREFIX)) {
            handleUpgradeMenu(event, player);
        } else if (viewTitle.equals(GUIManager.SPECIALIZATION_GUI_TITLE)) {
            handleSpecializationChoice(event, player);
        }
    }

    private void handleAbilitySelection(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta().getDisplayName().equals(" ")) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).split(" - ")[0];
        Ability clickedAbility = findAbilityByName(forceUser, itemName);

        if (clickedAbility != null) {
            if (forceUser.hasUnlockedAbility(clickedAbility.getID())) {
                plugin.getGuiManager().openUpgradeGUI(player, clickedAbility);
            } else {
                int unlockCost = plugin.getAbilityConfigManager().getUnlockCost(clickedAbility.getID());
                if (forceUser.getForcePoints() >= unlockCost) {
                    forceUser.addForcePoints(-unlockCost);
                    forceUser.unlockAbility(clickedAbility.getID());
                    player.sendMessage(ChatColor.AQUA + "You have unlocked " + clickedAbility.getName() + "!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
                    plugin.getGuiManager().openAbilityGUI(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough Force Points! (Requires " + unlockCost + ")");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                }
            }
        }
    }

    private void handleUpgradeMenu(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String viewTitle = event.getView().getTitle();
        String abilityName = viewTitle.replace(GUIManager.UPGRADE_GUI_TITLE_PREFIX, "");
        Ability ability = findAbilityByName(forceUser, abilityName);
        if (ability == null) return;

        Material clickedType = clickedItem.getType();

        if (clickedType == Material.EMERALD_BLOCK) {
            int currentLevel = forceUser.getAbilityLevel(ability.getID());
            int upgradeCost = plugin.getAbilityConfigManager().getIntValue(ability.getID(), currentLevel, "upgrade-cost", 1);

            if (forceUser.getForcePoints() >= upgradeCost) {
                forceUser.addForcePoints(-upgradeCost);
                forceUser.upgradeAbility(ability.getID());
                player.sendMessage(ChatColor.AQUA + "Upgraded " + ability.getName() + " to Level " + forceUser.getAbilityLevel(ability.getID()) + "!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.5f);
                plugin.getGuiManager().openUpgradeGUI(player, ability);
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough Force Points to upgrade this! (Requires " + upgradeCost + ")");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        } else if (clickedType == Material.BARRIER) {
            plugin.getGuiManager().openAbilityGUI(player);
        }
    }

    private void handleSpecializationChoice(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        String displayName = clickedItem.getItemMeta().getDisplayName();
        for (Rank spec : plugin.getRankManager().getSpecializations(forceUser.getSide())) {
            if (spec.getDisplayName().equals(displayName)) {
                forceUser.setSpecialization(spec.getId());
                forceUser.setNeedsToChoosePath(false);

                String ultimateId = getUltimateForSpec(spec.getId());
                if (ultimateId != null) {
                    forceUser.unlockAbility(ultimateId);
                    Ability ultimateAbility = plugin.getAbilityManager().getAbility(ultimateId);
                    if (ultimateAbility != null) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Ultimate Ability Unlocked: " + ultimateAbility.getName());
                    }
                }

                player.closeInventory();
                player.sendMessage(ChatColor.GOLD + "You have chosen the path of the " + spec.getDisplayName() + ChatColor.GOLD + "!");
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                return;
            }
        }
    }

    // --- MODIFIED: This helper is now also in GUIManager, but we keep it here for the choice logic ---
    private String getUltimateForSpec(String specId) {
        if (specId == null) return null;
        switch (specId) {
            case "GUARDIAN": return "FORCE_ABSORB";
            case "SENTINEL": return "FORCE_CAMOUFLAGE";
            case "CONSULAR": return "FORCE_SERENITY";
            case "WARRIOR": return "UNSTOPPABLE_VENGEANCE";
            case "INQUISITOR": return "MARK_OF_THE_HUNT";
            case "SORCERER": return "CHAIN_LIGHTNING";
            default: return null;
        }
    }

    private Ability findAbilityByName(ForceUser user, String name) {
        // --- MODIFIED: Use the new getAllAbilitiesBySide method to find any ability by name ---
        for (Ability ability : plugin.getAbilityManager().getAllAbilitiesBySide(user.getSide())) {
            if (ability.getName().equalsIgnoreCase(name.trim())) {
                return ability;
            }
        }
        return null;
    }
}