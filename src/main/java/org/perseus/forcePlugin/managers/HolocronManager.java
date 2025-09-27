package org.perseus.forcePlugin.managers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

import java.util.Arrays; // Import Arrays
import java.util.List;
import java.util.UUID;

public class HolocronManager {

    private final ForcePlugin plugin;
    private static final String JEDI_HOLOCRON_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA2OWM0NDk4YjdlNGU5MDI3NmZlZTI4Nzg2YmY1ZTliM2ZmOGIzOWQ2NjdkMzZhNjkyM2Q4ODBhNjI3YWI3NyJ9fX0=";
    private static final String SITH_HOLOCRON_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E1NWIzYTllYjE4MzA4Yjk2YmVhNGExNzJmZDI4MTFmMmU2MGQwYTllOGE3MmZmODI4YzQ0OTkxMzNkZjc2NyJ9fX0=";
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
        updateHolocronName(player);
    }

    private ItemStack createHolocronItem(ForceSide side) {
        String textureValue = (side == ForceSide.LIGHT) ? JEDI_HOLOCRON_TEXTURE : SITH_HOLOCRON_TEXTURE;
        ItemStack head = plugin.getVersionAdapter().createCustomHead(textureValue, side);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList(HOLOCRON_IDENTIFIER));
            head.setItemMeta(meta);
        }
        return head;
    }

    public void updateHolocronName(Player player) {
        ItemStack holocron = null;
        for (ItemStack item : player.getInventory().getContents()) {
            if (isHolocron(item)) {
                holocron = item;
                break;
            }
        }
        if (holocron == null) return;

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
        if (item == null || item.getType() != Material.PLAYER_HEAD || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
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