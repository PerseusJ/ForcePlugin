$replacements = @(
    @("PotionEffectType.SLOW_DIGGING", "VersionUtil.MINING_FATIGUE"),
    @("PotionEffectType.SLOW,", "VersionUtil.SLOWNESS,"),
    @("PotionEffectType.SLOW)", "VersionUtil.SLOWNESS)"),
    @("PotionEffectType.SLOW;", "VersionUtil.SLOWNESS;"),
    @("PotionEffectType.DAMAGE_RESISTANCE", "VersionUtil.RESISTANCE"),
    @("PotionEffectType.INCREASE_DAMAGE", "VersionUtil.STRENGTH"),
    @("PotionEffectType.CONFUSION", "VersionUtil.NAUSEA"),
    @("PotionEffectType.FAST_DIGGING", "VersionUtil.HASTE"),
    @("Particle.VILLAGER_HAPPY", "VersionUtil.HAPPY_VILLAGER"),
    @("Particle.SNOWBALL", "VersionUtil.SNOWFLAKE"),
    @("Particle.SNOW_SHOVEL", "VersionUtil.SNOWFLAKE"),
    @("Particle.TOTEM", "VersionUtil.TOTEM_OF_UNDYING"),
    @("Particle.CRIT_MAGIC", "VersionUtil.ENCHANTED_HIT"),
    @("Particle.EXPLOSION_NORMAL", "VersionUtil.POOF"),
    @("Particle.EXPLOSION_LARGE", "VersionUtil.EXPLOSION"),
    @("Particle.EXPLOSION_HUGE", "VersionUtil.EXPLOSION_EMITTER"),
    @("Particle.REDSTONE", "VersionUtil.DUST"),
    @("Particle.SMOKE_LARGE", "VersionUtil.LARGE_SMOKE"),
    @("Particle.SPELL_WITCH", "VersionUtil.WITCH"),
    @("Particle.SPELL_MOB", "VersionUtil.ENTITY_EFFECT"),
    @("Enchantment.DURABILITY", "VersionUtil.UNBREAKING"),
    @("PotionEffectType.MINING_FATIGUE", "VersionUtil.MINING_FATIGUE"),
    @("PotionEffectType.SLOWNESS,", "VersionUtil.SLOWNESS,"),
    @("PotionEffectType.SLOWNESS\)", "VersionUtil.SLOWNESS)"),
    @("PotionEffectType.SLOWNESS;", "VersionUtil.SLOWNESS;"),
    @("PotionEffectType.RESISTANCE", "VersionUtil.RESISTANCE"),
    @("PotionEffectType.STRENGTH", "VersionUtil.STRENGTH"),
    @("PotionEffectType.NAUSEA", "VersionUtil.NAUSEA"),
    @("PotionEffectType.HASTE", "VersionUtil.HASTE"),
    @("Particle.HAPPY_VILLAGER", "VersionUtil.HAPPY_VILLAGER"),
    @("Particle.SNOWFLAKE", "VersionUtil.SNOWFLAKE"),
    @("Particle.TOTEM_OF_UNDYING", "VersionUtil.TOTEM_OF_UNDYING"),
    @("Particle.ENCHANTED_HIT", "VersionUtil.ENCHANTED_HIT"),
    @("Particle.POOF", "VersionUtil.POOF"),
    @("Particle.EXPLOSION", "VersionUtil.EXPLOSION"),
    @("Particle.EXPLOSION_EMITTER", "VersionUtil.EXPLOSION_EMITTER"),
    @("Particle.DUST", "VersionUtil.DUST"),
    @("Particle.LARGE_SMOKE", "VersionUtil.LARGE_SMOKE"),
    @("Particle.WITCH", "VersionUtil.WITCH"),
    @("Particle.ENTITY_EFFECT", "VersionUtil.ENTITY_EFFECT"),
    @("Enchantment.UNBREAKING", "VersionUtil.UNBREAKING")
)

$importStr = "import org.perseus.forcePlugin.versioning.VersionUtil;"
$files = Get-ChildItem -Path "c:\Projects\ForcePlugin\src\main\java" -Recurse -Filter "*.java"

foreach ($file in $files) {
    if ($file.Name -eq "VersionUtil.java") { continue }
    
    $content = Get-Content -Path $file.FullName -Raw
    $original = $content
    foreach ($pair in $replacements) {
        $content = $content -replace [regex]::Escape($pair[0]), $pair[1]
    }
    
    if ($content -ne $original) {
        if ($content -notmatch "import org\.perseus\.forcePlugin\.versioning\.VersionUtil;") {
            $content = $content -replace "(package\s+[^;]+;)", "`$1`n`n$importStr"
        }
        
        [IO.File]::WriteAllText($file.FullName, $content, (New-Object System.Text.UTF8Encoding($false)))
        Write-Host "Updated $($file.FullName)"
    }
}
