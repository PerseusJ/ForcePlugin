package org.perseus.forcePlugin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.perseus.forcePlugin.abilities.Ability;
import java.util.List;
import java.util.UUID;
public class HolocronManager {

    private final ForcePlugin plugin;
    private static final String JEDI_HOLOCRON_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y5YjY3YjVjMzY3Y2QxZDRiYjc0M2Y5ODQ3YmI1Mjc5OTY1MWU5N2FiZmYxYjE2YjM3YjQ0YmY0Zjc0YmY0In19fQ==";
    private static final String SITH_HOLOCRON_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3Y2M3Y2YxZTRhYmFkMWQyYTRlNmEwY2I0OThhZmMwODMyYmE1MGUxYmZlYjZmYjRjYjFkYjlmOTQ0YmU5NCJ9fX0=";
    public static final String HOLOCRON_IDENTIFIER = ChatColor.DARK_PURPLE + "Force Artifact";

    public HolocronManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public void giveHolocron(Player player) {
        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null || forceUser.getSide() == ForceSide.NONE) return;

        removeHolocron(player);

        ItemStack holocron = createHolocronItem(forceUser.getSide());
        player.getInventory().addItem(holocron);
        updateHolocronName(player, holocron);
    }

    private ItemStack createHolocronItem(ForceSide side) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        // --- THE FIX: Explicitly use the Paper PlayerProfile and ProfileProperty ---
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID()); // Use createProfile for Paper
        ProfileProperty property = new ProfileProperty("textures",
                (side == ForceSide.LIGHT) ? JEDI_HOLOCRON_TEXTURE : SITH_HOLOCRON_TEXTURE);
        profile.setProperty(property);
        meta.setPlayerProfile(profile);
        // --- END FIX ---

        meta.setLore(List.of(HOLOCRON_IDENTIFIER));
        head.setItemMeta(meta);
        return head;
    }

    public void updateHolocronName(Player player, ItemStack holocron) {
        if (holocron == null || !isHolocron(holocron)) return;

        ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
        if (forceUser == null) return;

        Ability activeAbility = plugin.getAbilityManager().getAbility(forceUser.getActiveAbilityId());
        SkullMeta meta = (SkullMeta) holocron.getItemMeta();

        ChatColor sideColor = (forceUser.getSide() == ForceSide.LIGHT) ? ChatColor.AQUA : ChatColor.RED;
        String baseName = (forceUser.getSide() == ForceSide.LIGHT) ? "Jedi Holocron" : "Sith Holocron";

        if (activeAbility != null) {
            meta.setDisplayName(sideColor + "" + ChatColor.BOLD + baseName + ChatColor.WHITE + " [" + activeAbility.getName() + "]");
        } else {
            meta.setDisplayName(sideColor + "" + ChatColor.BOLD + baseName);
        }
        holocron.setItemMeta(meta);
    }

    public boolean isHolocron(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return false;
        }
        return item.getItemMeta().getLore().contains(HOLOCRON_IDENTIFIER);
    }

    public void removeHolocron(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isHolocron(item)) {
                player.getInventory().remove(item);
            }
        }
    }
}