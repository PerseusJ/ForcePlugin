$directory = "c:\Projects\ForcePlugin\src\main\java\org\perseus\forcePlugin\abilities"
$files = Get-ChildItem -Path $directory -Recurse -Filter *.java

$utf8NoBom = New-Object System.Text.UTF8Encoding($False)

foreach ($file in $files) {
    if ($file.Name -eq "Ability.java" -or $file.Name -eq "AbstractAbility.java") {
        continue
    }

    $content = Get-Content -Path $file.FullName -Raw

    # Remove BOM if present
    $content = $content -replace "^\xEF\xBB\xBF", ""
    $content = $content.TrimStart([char]0xFEFF)

    [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
    Write-Host "Fixed BOM in $($file.FullName)"
}
