import os
import re

directory = "c:/Projects/ForcePlugin/src/main/java/org/perseus/forcePlugin/abilities"

for root, _, files in os.walk(directory):
    for file in files:
        if not file.endswith(".java"):
            continue
        if file in ["Ability.java", "AbstractAbility.java"]:
            continue
            
        path = os.path.join(root, file)
        with open(path, "r", encoding="utf-8") as f:
            content = f.read()
            
        # 1. imports
        content = content.replace("import org.perseus.forcePlugin.abilities.Ability;", "import org.perseus.forcePlugin.abilities.AbstractAbility;")
        
        # 2. implements -> extends
        content = content.replace("implements Ability", "extends AbstractAbility")
        
        # 3. fields
        content = re.sub(r"^\s*private final AbilityConfigManager configManager;.*?\n", "", content, flags=re.MULTILINE)
        content = re.sub(r"^\s*private final ForcePlugin plugin;.*?\n", "", content, flags=re.MULTILINE)
        
        # 4. constructor
        class_name = file.replace(".java", "")
        # The constructor usually looks like: public ClassName(AbilityConfigManager configManager, ForcePlugin plugin) { this.configManager = configManager; this.plugin = plugin; }
        # Let's replace it with properly formatted one.
        old_constructor_pattern = r"public\s+" + class_name + r"\s*\(\s*AbilityConfigManager\s+configManager\s*,\s*ForcePlugin\s+plugin\s*\)\s*\{[^}]+\}"
        new_constructor = f"public {class_name}(AbilityConfigManager configManager, ForcePlugin plugin) {{\n        super(configManager, plugin);\n    }}"
        content = re.sub(old_constructor_pattern, new_constructor, content)
        
        # 5. config calls
        content = content.replace('configManager.getDoubleValue(getID(), level, ', 'cfg(level, ')
        content = content.replace('configManager.getIntValue(getID(), level, ', 'cfgInt(level, ')
        
        # 6. fix static call in ForceDrain
        if class_name == "ForceDrain":
            content = content.replace("org.perseus.forcePlugin.ForcePlugin.getPlugin(org.perseus.forcePlugin.ForcePlugin.class)", "plugin")
            
        # 7. multiline formatting.
        # we have a lot of: @Override public String getID() { return "FORCE_DRAIN"; }
        # let's just do a basic formatting
        content = re.sub(r"@Override\s+public\s+([a-zA-Z<>]+)\s+([a-zA-Z0-9_]+)\s*\((.*?)\)\s*\{\s*", r"@Override\n    public \1 \2(\3) {\n        ", content)
        content = re.sub(r"\}\s+@Override", r"}\n\n    @Override", content)
        # fix the closing brace being on same line
        content = re.sub(r"return\s+([^;]+);\s*\}", r"return \1;\n    }", content)
        
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print("Refactored", path)
