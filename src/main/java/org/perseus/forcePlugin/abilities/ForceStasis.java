package org.perseus.forcePlugin.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceStasis implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceStasis(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_STASIS"; }
    @Override public String getName() { return "Force Stasis"; }
    @Override public String getDescription() { return "Freezes a single target in place."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 20.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 15.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int range = 15;
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 3) * 20;
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 200, false, false));
        player.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.8f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || target.isDead() || !target.isValid()) { this.cancel(); return; }
                Location center = target.getLocation().add(0, 1, 0);
                for (int i = 0; i < 10; i++) {
                    Vector direction = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
                    Location particleLoc = center.clone().add(direction.multiply(1.2));
                    target.getWorld().spawnParticle(Particle.SNOWFLAKE, particleLoc, 1, 0, 0, 0, 0);
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}