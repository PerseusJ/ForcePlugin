package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceDrain implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceDrain(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_DRAIN"; }
    @Override public String getName() { return "Force Drain"; }
    @Override public String getDescription() { return "Steal health from a target to heal yourself."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 40.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 25.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int range = 15;
        double drainAmount = configManager.getDoubleValue(getID(), level, "drain-amount-hearts", 2.0) * 2;
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        EntityType type = target.getType();
        if (type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.WITHER || type == EntityType.STRAY || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.HUSK || type == EntityType.WITHER_SKELETON || type == EntityType.PHANTOM) {
            player.sendMessage(ChatColor.RED + "You cannot drain life from the undead.");
            return;
        }

        target.damage(drainAmount, player);
        double newHealth = Math.min(player.getHealth() + drainAmount, org.perseus.forcePlugin.managers.HealthUtil.getMaxHealth(player));
        player.setHealth(newHealth);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5);

        new BukkitRunnable() {
            final Location start = target.getEyeLocation();
            final Location end = player.getEyeLocation();
            final Vector direction = end.toVector().subtract(start.toVector()).normalize();
            final double distance = start.distance(end);
            double traveled = 0;
            @Override
            public void run() {
                if (traveled >= distance) { this.cancel(); return; }
                Location point = start.clone().add(direction.clone().multiply(traveled));
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(200, 0, 0), 1.0F);
                player.getWorld().spawnParticle(org.perseus.forcePlugin.ForcePlugin.getPlugin(org.perseus.forcePlugin.ForcePlugin.class).getVersionAdapter().getRedstoneParticle(), point, 1, 0, 0, 0, 0, dustOptions);
                traveled += 0.5;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}