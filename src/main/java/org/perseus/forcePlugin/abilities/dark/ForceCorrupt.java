package org.perseus.forcePlugin.abilities.dark;

import org.perseus.forcePlugin.versioning.VersionUtil;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
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

public class ForceCorrupt extends AbstractAbility {

    public ForceCorrupt(AbilityConfigManager configManager, ForcePlugin plugin) {
        super(configManager, plugin);
    }

    @Override
    public String getID() {
        return "FORCE_CORRUPT";
    }

    @Override
    public String getName() {
        return "Force Corrupt";
    }

    @Override
    public String getDescription() {
        return "Infects a target with dark energy, causing them to wither and slow.";
    }

    @Override
    public ForceSide getSide() {
        return ForceSide.DARK;
    }

    @Override
    public double getEnergyCost(int level) {
        return cfg(level, "energy-cost", 35.0);
    }

    @Override
    public double getCooldown(int level) {
        return cfg(level, "cooldown", 20.0);
    }

    @Override
    public void execute(Player player, ForceUser forceUser) {
        int level = forceUser.getAbilityLevel(getID());
        int witherTicks = cfgInt(level, "wither-duration-seconds", 5) * 20;
        int poisonTicks = cfgInt(level, "poison-duration-seconds", 5) * 20;
        int slowTicks = cfgInt(level, "slow-duration-seconds", 5) * 20;

        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 20,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        
        if (rayTrace == null || rayTrace.getHitEntity() == null) return;
        
        LivingEntity target = (LivingEntity) rayTrace.getHitEntity();

        if (witherTicks > 0) target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, witherTicks, 0));
        if (poisonTicks > 0) target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, poisonTicks, 0));
        if (slowTicks > 0) target.addPotionEffect(new PotionEffect(VersionUtil.SLOWNESS, slowTicks, 1));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.8f, 0.5f);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 0.5f);

        Particle.DustOptions beamOptions = new Particle.DustOptions(Color.fromRGB(50, 0, 50), 1.0F);
        ParticleUtil.drawZigZagBeam(player.getEyeLocation(), target.getEyeLocation(), plugin.getVersionAdapter().getRedstoneParticle(), 4.0, 0.3, beamOptions);

        target.getWorld().spawnParticle(VersionUtil.WITCH, target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
    }
}

