package org.perseus.forcePlugin.abilities.light;

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
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.PassiveAbility;
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

        // --- NEW: Lingering Effects Passive (Jedi Sentinel) ---
        if (forceUser.getSpecialization() != null && forceUser.getSpecialization().equals("SENTINEL")) {
            int rank = forceUser.getPassiveRank("LINGERING_EFFECTS");
            if (rank > 0) {
                PassiveAbility passive = findPassive(forceUser.getSpecialization(), "LINGERING_EFFECTS");
                if (passive != null) {
                    duration += (int) (passive.getValue(rank) * 20);
                }
            }
        }
        // --- END NEW ---

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), range,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 200, false, false));
        player.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.8f);

        final int finalDuration = duration; // Make duration effectively final for the runnable
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= finalDuration || target.isDead() || !target.isValid()) { this.cancel(); return; }
                Location center = target.getLocation().add(0, 1, 0);
                for (int i = 0; i < 10; i++) {
                    Vector direction = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
                    Location particleLoc = center.clone().add(direction.multiply(1.2));
                    target.getWorld().spawnParticle(Particle.SNOW_SHOVEL, particleLoc, 1, 0, 0, 0, 0);
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private PassiveAbility findPassive(String specId, String passiveId) {
        return plugin.getPassiveManager().getPassivesForSpec(specId).stream()
                .filter(p -> p.getId().equals(passiveId))
                .findFirst().orElse(null);
    }
}