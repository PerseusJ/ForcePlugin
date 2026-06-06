package org.perseus.forcePlugin.versioning;

import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

public class VersionUtil {
    public static final PotionEffectType SLOWNESS = getPotion("SLOWNESS", "SLOW");
    public static final PotionEffectType RESISTANCE = getPotion("RESISTANCE", "DAMAGE_RESISTANCE");
    public static final PotionEffectType STRENGTH = getPotion("STRENGTH", "INCREASE_DAMAGE");
    public static final PotionEffectType NAUSEA = getPotion("NAUSEA", "CONFUSION");
    public static final PotionEffectType MINING_FATIGUE = getPotion("MINING_FATIGUE", "SLOW_DIGGING");
    public static final PotionEffectType HASTE = getPotion("HASTE", "FAST_DIGGING");

    public static final Particle HAPPY_VILLAGER = getParticle("HAPPY_VILLAGER", "VILLAGER_HAPPY");
    public static final Particle SNOWFLAKE = getParticle("SNOWFLAKE", "SNOWBALL", "SNOW_SHOVEL");
    public static final Particle TOTEM_OF_UNDYING = getParticle("TOTEM_OF_UNDYING", "TOTEM");
    public static final Particle ENCHANTED_HIT = getParticle("ENCHANTED_HIT", "CRIT_MAGIC");
    public static final Particle POOF = getParticle("POOF", "EXPLOSION_NORMAL");
    public static final Particle EXPLOSION = getParticle("EXPLOSION", "EXPLOSION_LARGE");
    public static final Particle EXPLOSION_EMITTER = getParticle("EXPLOSION_EMITTER", "EXPLOSION_HUGE");
    public static final Particle DUST = getParticle("DUST", "REDSTONE");
    public static final Particle LARGE_SMOKE = getParticle("LARGE_SMOKE", "SMOKE_LARGE");
    public static final Particle WITCH = getParticle("WITCH", "SPELL_WITCH");
    public static final Particle ENTITY_EFFECT = getParticle("ENTITY_EFFECT", "SPELL_MOB");

    public static final Enchantment UNBREAKING = getEnchantment("UNBREAKING", "DURABILITY");

    private static PotionEffectType getPotion(String... names) {
        for (String name : names) {
            try {
                PotionEffectType type = PotionEffectType.getByName(name);
                if (type != null) return type;
            } catch (Exception ignored) {}
        }
        return PotionEffectType.SPEED; // Fallback
    }

    private static Particle getParticle(String... names) {
        for (String name : names) {
            try {
                return Particle.valueOf(name);
            } catch (Exception ignored) {}
        }
        return Particle.FLAME; // Fallback
    }

    @SuppressWarnings("deprecation")
    private static Enchantment getEnchantment(String... names) {
        for (String name : names) {
            try {
                Enchantment type = Enchantment.getByName(name);
                if (type != null) return type;
            } catch (Exception ignored) {}
        }
        return null;
    }
}
