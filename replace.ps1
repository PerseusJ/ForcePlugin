$replacements = @(
    @("PotionEffectType.SLOW_DIGGING", "PotionEffectType.MINING_FATIGUE"),
    @("PotionEffectType.SLOW,", "PotionEffectType.SLOWNESS,"),
    @("PotionEffectType.SLOW)", "PotionEffectType.SLOWNESS)"),
    @("PotionEffectType.SLOW;", "PotionEffectType.SLOWNESS;"),
    @("PotionEffectType.DAMAGE_RESISTANCE", "PotionEffectType.RESISTANCE"),
    @("PotionEffectType.INCREASE_DAMAGE", "PotionEffectType.STRENGTH"),
    @("PotionEffectType.CONFUSION", "PotionEffectType.NAUSEA"),
    @("PotionEffectType.FAST_DIGGING", "PotionEffectType.HASTE"),
    @("Particle.VILLAGER_HAPPY", "Particle.HAPPY_VILLAGER"),
    @("Particle.SNOWBALL", "Particle.SNOWFLAKE"),
    @("Particle.SNOW_SHOVEL", "Particle.SNOWFLAKE"),
    @("Particle.TOTEM", "Particle.TOTEM_OF_UNDYING"),
    @("Particle.CRIT_MAGIC", "Particle.ENCHANTED_HIT"),
    @("Particle.EXPLOSION_NORMAL", "Particle.POOF"),
    @("Particle.EXPLOSION_LARGE", "Particle.EXPLOSION"),
    @("Particle.EXPLOSION_HUGE", "Particle.EXPLOSION_EMITTER"),
    @("Particle.REDSTONE", "Particle.DUST"),
    @("Particle.SMOKE_LARGE", "Particle.LARGE_SMOKE"),
    @("Particle.SPELL_WITCH", "Particle.WITCH"),
    @("Particle.SPELL_MOB", "Particle.ENTITY_EFFECT")
)

$files = Get-ChildItem -Path "c:\Projects\ForcePlugin\src\main\java" -Recurse -Filter "*.java"
foreach ($file in $files) {
    $content = Get-Content -Path $file.FullName -Raw
    $original = $content
    foreach ($pair in $replacements) {
        $content = $content -replace [regex]::Escape($pair[0]), $pair[1]
    }
    if ($content -ne $original) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8
        Write-Host "Updated $($file.FullName)"
    }
}
