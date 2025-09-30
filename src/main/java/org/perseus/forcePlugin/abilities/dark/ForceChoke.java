package org.perseus.forcePlugin.abilities.dark;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceChoke implements Ability {
    private final ForcePlugin plugin;
    private final AbilityConfigManager configManager;
    public ForceChoke(ForcePlugin plugin, AbilityConfigManager configManager) { this.plugin = plugin; this.configManager = configManager; }
    @Override public String getID() { return "FORCE_CHOKE"; }
    @Override public String getName() { return "Force Choke"; }
    @Override public String getDescription() { return "Lifts and damages a single target."; }
    @Override public ForceSide getSide() { return ForceSide.DARK; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 12.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int range = 15;
        final int duration = configManager.getIntValue(getID(), level, "duration-seconds", 3) * 20;
        int levitationAmp = 0;
        double damagePerSecond = configManager.getDoubleValue(getID(), level, "damage-per-second-hearts", 1.0) * 2;
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, levitationAmp));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 0.8f);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || target.isDead() || !target.isValid()) { this.cancel(); return; }
                if (ticks % 20 == 0) { target.damage(damagePerSecond, player); }
                Location particleLoc = target.getLocation().add(0, target.getHeight() * 0.8, 0);
                double radius = 0.5 - ((double) ticks / duration) * 0.3;
                double angle = ticks * Math.PI / 4;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                // SMOKE_NORMAL is not in 1.16, use SMOKE_LARGE
                target.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLoc.clone().add(x, 0, z), 1, 0, 0, 0, 0);
                target.getWorld().spawnParticle(Particle.SQUID_INK, particleLoc.clone().add(-x, 0, -z), 1, 0, 0, 0, 0);
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}