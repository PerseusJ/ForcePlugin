package org.perseus.forcePlugin.abilities.light;

import org.perseus.forcePlugin.versioning.VersionUtil;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.perseus.forcePlugin.ForcePlugin;
import org.perseus.forcePlugin.abilities.AbstractAbility;
import org.perseus.forcePlugin.data.ForceSide;
import org.perseus.forcePlugin.data.ForceUser;
import org.perseus.forcePlugin.managers.AbilityConfigManager;
import org.perseus.forcePlugin.managers.ParticleUtil;
import org.perseus.forcePlugin.managers.HealthUtil;

public class ForceMend extends AbstractAbility {

    public ForceMend(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "FORCE_MEND";
    }

    @Override
    public String getName() {
        return "Force Mend";
    }

    @Override
    public String getDescription() {
        return "Heal an ally from a distance.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.LIGHT;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 30.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 10.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        double healHearts = cfg(level, "heal-amount-hearts", 3.0);
        int regenDuration = cfgInt(level, "regen-duration-seconds", 5) * 20;

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 20,
                entity -> entity instanceof Player && !entity.equals(player));
        
        if (rayTrace == null || rayTrace.getHitEntity() == null) {
            player.sendMessage(org.bukkit.ChatColor.RED + "No ally in sight to mend.");
            return;
        }

        Player target = (Player) rayTrace.getHitEntity();

        double currentHealth = target.getHealth();
        double maxHealth = HealthUtil.getMaxHealth(target);
        target.setHealth(Math.min(currentHealth + (healHearts * 2), maxHealth));
        
        if (regenDuration > 0) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenDuration, 0));
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 1.5f);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 2.0f);

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 215, 0), 1.0F);
        ParticleUtil.drawZigZagBeam(player.getEyeLocation(), target.getEyeLocation(), plugin.getVersionAdapter().getRedstoneParticle(), 4.0, 0.3, dustOptions);
    }
}
