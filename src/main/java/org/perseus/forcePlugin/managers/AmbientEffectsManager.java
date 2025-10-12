package org.perseus.forcePlugin.managers;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;

public class AmbientEffectsManager {

    private final ForcePlugin plugin;

    public AmbientEffectsManager(ForcePlugin plugin) {
        this.plugin = plugin;
        startEffectTask();
    }

    private void startEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ForceUser forceUser = plugin.getForceUserManager().getForceUser(player);
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();

                    if (forceUser != null && plugin.getHolocronManager().isHolocron(itemInHand) && forceUser.getSide() != ForceSide.NONE) {
                        spawnAmbientParticle(player, forceUser.getSide());
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 10L);
    }

    private void spawnAmbientParticle(Player player, ForceSide side) {
        Location location = player.getLocation().add(
                (Math.random() - 0.5) * 1.5,
                0.2,
                (Math.random() - 0.5) * 1.5
        );

        if (side == ForceSide.LIGHT) {
            player.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0, 0, 0, 0);
        } else {
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(150, 0, 0), 0.7F);
            player.getWorld().spawnParticle(plugin.getVersionAdapter().getRedstoneParticle(), location, 1, 0, 0, 0, 0, dustOptions);
        }
    }
}