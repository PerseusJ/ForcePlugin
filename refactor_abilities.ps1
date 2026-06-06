$directory = "c:\Projects\ForcePlugin\src\main\java\org\perseus\forcePlugin\abilities"
$files = Get-ChildItem -Path $directory -Recurse -Filter *.java

foreach ($file in $files) {
    if ($file.Name -eq "Ability.java" -or $file.Name -eq "AbstractAbility.java") {
        continue
    }

    $content = Get-Content -Path $file.FullName -Raw

    # 1. imports
    $content = $content -replace "import org\.perseus\.forcePlugin\.abilities\.Ability;", "import org.perseus.forcePlugin.abilities.AbstractAbility;"

    # 2. implements -> extends
    $content = $content -replace "implements Ability", "extends AbstractAbility"

    # 3. fields
    $content = $content -replace "(?m)^\s*private final AbilityConfigManager configManager;.*?\r?\n", ""
    $content = $content -replace "(?m)^\s*private final ForcePlugin plugin;.*?\r?\n", ""

    # 4. constructor
    $className = $file.BaseName
    $oldConstructorPattern = "public\s+$className\s*\(\s*AbilityConfigManager\s+configManager\s*,\s*ForcePlugin\s+plugin\s*\)\s*\{[^}]+\}"
    $newConstructor = "public $className(AbilityConfigManager configManager, ForcePlugin plugin) {`r`n        super(configManager, plugin);`r`n    }"
    $content = [System.Text.RegularExpressions.Regex]::Replace($content, $oldConstructorPattern, $newConstructor)

    # 5. config calls
    $content = $content.Replace("configManager.getDoubleValue(getID(), level, ", "cfg(level, ")
    $content = $content.Replace("configManager.getIntValue(getID(), level, ", "cfgInt(level, ")

    # 6. fix static call in ForceDrain
    if ($className -eq "ForceDrain") {
        $content = $content.Replace("org.perseus.forcePlugin.ForcePlugin.getPlugin(org.perseus.forcePlugin.ForcePlugin.class)", "plugin")
    }

    # 7. multiline formatting
    $content = [System.Text.RegularExpressions.Regex]::Replace($content, "@Override\s+public\s+([a-zA-Z<>]+)\s+([a-zA-Z0-9_]+)\s*\((.*?)\)\s*\{\s*", "@Override`r`n    public `$1 `$2(`$3) {`r`n        ")
    $content = [System.Text.RegularExpressions.Regex]::Replace($content, "\}\s+@Override", "}`r`n`r`n    @Override")
    $content = [System.Text.RegularExpressions.Regex]::Replace($content, "return\s+([^;]+);\s*\}", "return `$1;`r`n    }")

    Set-Content -Path $file.FullName -Value $content -Encoding UTF8
    Write-Host "Refactored $($file.FullName)"
}
