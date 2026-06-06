package org.perseus.forcePlugin.abilities.universal;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceSense extends AbstractAbility {

    public ForceSense(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "FORCE_SENSE";
    }

    @Override
    public String getName() {
        return "Force Sense";
    }

    @Override
    public String getDescription() {
        return "Sense nearby entities and reveal their life force.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.NONE;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 10.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 15.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int radius = cfgInt(level, "radius", 20);
        int durationTicks = cfgInt(level, "duration-seconds", 5) * 20;

        StringBuilder healthSummary = new StringBuilder();
        int entitiesFound = 0;

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && !entity.equals(player)) {
                LivingEntity target = (LivingEntity) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, durationTicks, 0, false, false));
                entitiesFound++;

                if (entity instanceof Player) {
                    Player targetPlayer = (Player) entity;
                    String healthStr = String.format("%.1f", targetPlayer.getHealth());
                    healthSummary.append(ChatColor.YELLOW).append(targetPlayer.getName())
                                 .append(ChatColor.GRAY).append(": ").append(ChatColor.RED).append(healthStr).append("â¤ ");
                }
            }
        }

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        Location loc = player.getLocation();
        for (int i = 0; i < 36; i++) {
            double angle = 2 * Math.PI * i / 36;
            double x = loc.getX() + 2 * Math.cos(angle);
            double z = loc.getZ() + 2 * Math.sin(angle);
            Location particleLoc = new Location(loc.getWorld(), x, loc.getY() + 0.5, z);
            player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0.05);
        }

        if (healthSummary.length() > 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(healthSummary.toString()));
        } else if (entitiesFound > 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "Sensed " + entitiesFound + " entities nearby."));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "No entities sensed."));
        }
    }
}
