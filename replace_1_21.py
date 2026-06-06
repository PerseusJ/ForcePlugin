import os

replacements = {
    "PotionEffectType.SLOW,": "PotionEffectType.SLOWNESS,",
    "PotionEffectType.SLOW)": "PotionEffectType.SLOWNESS)",
    "PotionEffectType.DAMAGE_RESISTANCE": "PotionEffectType.RESISTANCE",
    "PotionEffectType.INCREASE_DAMAGE": "PotionEffectType.STRENGTH",
    "PotionEffectType.CONFUSION": "PotionEffectType.NAUSEA",
    "PotionEffectType.SLOW_DIGGING": "PotionEffectType.MINING_FATIGUE",
    "Particle.VILLAGER_HAPPY": "Particle.HAPPY_VILLAGER",
    "Particle.SNOWBALL": "Particle.SNOWFLAKE",
    "Particle.SNOW_SHOVEL": "Particle.SNOWFLAKE",
    "Particle.TOTEM": "Particle.TOTEM_OF_UNDYING",
    "Particle.CRIT_MAGIC": "Particle.ENCHANTED_HIT",
    "Particle.EXPLOSION_NORMAL": "Particle.POOF",
    "Particle.EXPLOSION_LARGE": "Particle.EXPLOSION",
    "Particle.EXPLOSION_HUGE": "Particle.EXPLOSION_EMITTER",
    "Particle.REDSTONE": "Particle.DUST"
}

directory = "c:/Projects/ForcePlugin/src/main/java"

for root, _, files in os.walk(directory):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            original = content
            for old, new in replacements.items():
                content = content.replace(old, new)
            if original != content:
                with open(path, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f"Updated {path}")
