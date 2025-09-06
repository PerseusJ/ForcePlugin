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
import org.perseus.forcePlugin.AbilityConfigManager;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.ForceSide;

public class ForceCrush implements Ability {

    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;

    public ForceCrush(AbilityConfigManager configManager, ForcePlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public String getID() { return "FORCE_CRUSH"; }

    @Override
    public String getName() { return "Force Crush"; }

    @Override
    public String getDescription() { return "Deals high damage and slows a single target."; }

    @Override
    public ForceSide getSide() { return ForceSide.DARK; }

    @Override
    public double getEnergyCost() { return configManager.getDoubleValue(getID(), "energy-cost", 35.0); }

    @Override
    public double getCooldown() { return configManager.getDoubleValue(getID(), "cooldown", 15.0); }

    @Override
    public void execute(Player player) {
        int range = configManager.getIntValue(getID(), "range", 20);
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));

        if (rayTrace == null || rayTrace.getHitEntity() == null) return;

        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();
        double damage = configManager.getDoubleValue(getID(), "damage-hearts", 4.0) * 2;
        int duration = configManager.getIntValue(getID(), "slowness-duration-seconds", 3) * 20;
        int slownessAmp = configManager.getIntValue(getID(), "slowness-amplifier", 3) - 1;

        target.damage(damage, player);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slownessAmp));
        player.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.8f, 0.8f);

        // --- NEW: Converging Crush Particles ---
        new BukkitRunnable() {
            double radius = 2.0;
            @Override
            public void run() {
                if (radius < 0.5) {
                    this.cancel();
                    return;
                }
                Location center = target.getLocation().add(0, 1, 0);
                for (int i = 0; i < 15; i++) {
                    Vector direction = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
                    Location particleLoc = center.clone().add(direction.multiply(radius));
                    target.getWorld().spawnParticle(Particle.SQUID_INK, particleLoc, 1, 0, 0, 0, 0);
                }
                radius -= 0.2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        // --- END NEW ---
    }
}