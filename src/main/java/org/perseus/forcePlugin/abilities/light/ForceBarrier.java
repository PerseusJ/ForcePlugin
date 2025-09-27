package org.perseus.forcePlugin.abilities.light;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.Ability;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.data.PassiveAbility;
import org.perseus.forcePlugin.managers.AbilityConfigManager;

public class ForceBarrier implements Ability {
    private final AbilityConfigManager configManager;
    private final ForcePlugin plugin;
    public ForceBarrier(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
    @Override public String getID() { return "FORCE_BARRIER"; }
    @Override public String getName() { return "Force Barrier"; }
    @Override public String getDescription() { return "Grants temporary Absorption hearts."; }
    @Override public ForceSide getSide() { return ForceSide.LIGHT; }
    @Override public double getEnergyCost(int level) { return configManager.getDoubleValue(getID(), level, "energy-cost", 30.0); }
    @Override public double getCooldown(int level) { return configManager.getDoubleValue(getID(), level, "cooldown", 25.0); }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int duration = configManager.getIntValue(getID(), level, "duration-seconds", 10) * 20;
        int amplifier = configManager.getIntValue(getID(), level, "shield-hearts", 2) - 1;

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier));
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.2f);

        // --- NEW: Protective Spirit Passive (Jedi Guardian) ---
        if (forceUser.getSpecialization() != null && forceUser.getSpecialization().equals("GUARDIAN")) {
            int rank = forceUser.getPassiveRank("PROTECTIVE_SPIRIT");
            if (rank > 0) {
                PassiveAbility passive = findPassive(forceUser.getSpecialization(), "PROTECTIVE_SPIRIT");
                if (passive != null) {
                    double allyShieldMultiplier = passive.getValue(rank);
                    int allyAmplifier = (int) Math.floor(amplifier * allyShieldMultiplier);
                    if (allyAmplifier >= 0) {
                        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                            // Check for other players who are also Light Side
                            if (entity instanceof Player && !entity.equals(player)) {
                                Player ally = (Player) entity;
                                ForceUser allyUser = plugin.getForceUserManager().getForceUser(ally);
                                if (allyUser != null && allyUser.getSide() == ForceSide.LIGHT) {
                                    ally.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, allyAmplifier));
                                    ally.getWorld().spawnParticle(Particle.END_ROD, ally.getLocation().add(0, 1, 0), 15, 0.4, 0.4, 0.4, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        // --- END NEW ---

        new BukkitRunnable() {
            int ticks = 0;
            double radius = 1.0;
            double angle = 0;
            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) { this.cancel(); return; }
                Location playerLoc = player.getLocation();
                double x = playerLoc.getX() + radius * Math.cos(angle);
                double z = playerLoc.getZ() + radius * Math.sin(angle);
                Location particleLoc = new Location(player.getWorld(), x, playerLoc.getY() + 1, z);
                player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
                angle += Math.PI / 8;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private PassiveAbility findPassive(String specId, String passiveId) {
        return plugin.getPassiveManager().getPassivesForSpec(specId).stream()
                .filter(p -> p.getId().equals(passiveId))
                .findFirst().orElse(null);
    }
}