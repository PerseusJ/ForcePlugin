# Graph Report - .  (2026-06-11)

## Corpus Check
- Corpus is ~23,623 words - fits in a single context window. You may not need a graph.

## Summary
- 1116 nodes · 2527 edges · 64 communities (60 shown, 4 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 499 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Admin Commands & Dark Abilities|Admin Commands & Dark Abilities]]
- [[_COMMUNITY_Admin Command Handlers|Admin Command Handlers]]
- [[_COMMUNITY_Force Enchant Command & GUI|Force Enchant Command & GUI]]
- [[_COMMUNITY_Database Manager|Database Manager]]
- [[_COMMUNITY_Force Bar & Connection Listeners|Force Bar & Connection Listeners]]
- [[_COMMUNITY_Dark Corrupt Ability|Dark Corrupt Ability]]
- [[_COMMUNITY_Cooldown & Ability Listener|Cooldown & Ability Listener]]
- [[_COMMUNITY_Rank Data Model|Rank Data Model]]
- [[_COMMUNITY_Ability Manager|Ability Manager]]
- [[_COMMUNITY_Force Points & GUI Manager|Force Points & GUI Manager]]
- [[_COMMUNITY_Force Enchant GUI|Force Enchant GUI]]
- [[_COMMUNITY_Light Camouflage Ultimate|Light Camouflage Ultimate]]
- [[_COMMUNITY_Dark Force Drain|Dark Force Drain]]
- [[_COMMUNITY_Holocron Listener|Holocron Listener]]
- [[_COMMUNITY_Chain Lightning|Chain Lightning]]
- [[_COMMUNITY_Version Adapter 1.16|Version Adapter 1.16]]
- [[_COMMUNITY_Version Adapter 1.21|Version Adapter 1.21]]
- [[_COMMUNITY_Ultimate Ability Listener|Ultimate Ability Listener]]
- [[_COMMUNITY_Version Adapter Interface|Version Adapter Interface]]
- [[_COMMUNITY_Dark Aura|Dark Aura]]
- [[_COMMUNITY_Force Choke|Force Choke]]
- [[_COMMUNITY_Force Rage|Force Rage]]
- [[_COMMUNITY_Force Scream|Force Scream]]
- [[_COMMUNITY_Soul Rend|Soul Rend]]
- [[_COMMUNITY_Unstoppable Vengeance|Unstoppable Vengeance]]
- [[_COMMUNITY_Force Absorb Ultimate|Force Absorb Ultimate]]
- [[_COMMUNITY_Force Barrier|Force Barrier]]
- [[_COMMUNITY_Force Clarity|Force Clarity]]
- [[_COMMUNITY_Force Deflection|Force Deflection]]
- [[_COMMUNITY_Force Heal|Force Heal]]
- [[_COMMUNITY_Force Mend|Force Mend]]
- [[_COMMUNITY_Force Repulse|Force Repulse]]
- [[_COMMUNITY_Force Serenity Ultimate|Force Serenity Ultimate]]
- [[_COMMUNITY_Force Stasis|Force Stasis]]
- [[_COMMUNITY_Force Valor|Force Valor]]
- [[_COMMUNITY_Force Sense Ability|Force Sense Ability]]
- [[_COMMUNITY_Telekinesis Ability|Telekinesis Ability]]
- [[_COMMUNITY_Force Tab Completer|Force Tab Completer]]
- [[_COMMUNITY_Mark of the Hunt|Mark of the Hunt]]
- [[_COMMUNITY_Plugin Entry Point|Plugin Entry Point]]
- [[_COMMUNITY_Ability Base Class|Ability Base Class]]
- [[_COMMUNITY_Holocron Manager|Holocron Manager]]
- [[_COMMUNITY_Abstract Ability Framework|Abstract Ability Framework]]
- [[_COMMUNITY_Force Side Command|Force Side Command]]
- [[_COMMUNITY_Version Utility|Version Utility]]
- [[_COMMUNITY_Passive Data Model|Passive Data Model]]
- [[_COMMUNITY_Ambient Effects Manager|Ambient Effects Manager]]
- [[_COMMUNITY_Config YAML Documents|Config YAML Documents]]
- [[_COMMUNITY_GUI Manager Imports|GUI Manager Imports]]
- [[_COMMUNITY_Plugin Commands & Enchanting Config|Plugin Commands & Enchanting Config]]
- [[_COMMUNITY_Rank Specializations|Rank Specializations]]
- [[_COMMUNITY_VS Code Settings|VS Code Settings]]
- [[_COMMUNITY_Force Energy Config|Force Energy Config]]
- [[_COMMUNITY_Ultimate Abilities Config|Ultimate Abilities Config]]
- [[_COMMUNITY_Neutral Passives|Neutral Passives]]

## God Nodes (most connected - your core abstractions)
1. `ForceUser` - 34 edges
2. `AbstractAbility` - 22 edges
3. `ForcePlugin` - 20 edges
4. `GUIManager` - 15 edges
5. `String` - 14 edges
6. `UltimateAbilityListener` - 14 edges
7. `ForceAdminCommand` - 13 edges
8. `GUIListener` - 13 edges
9. `ForceEnchantManager` - 12 edges
10. `ChainLightning` - 11 edges

## Surprising Connections (you probably didn't know these)
- `Light Side Abilities` --semantically_similar_to--> `Light Side Passives`  [INFERRED] [semantically similar]
  src/main/resources/config.yml → src/main/resources/passives.yml
- `Dark Side Abilities` --semantically_similar_to--> `Dark Side Passives`  [INFERRED] [semantically similar]
  src/main/resources/config.yml → src/main/resources/passives.yml
- `DarkAura` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/DarkAura.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `ForceCorrupt` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/ForceCorrupt.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `SoulRend` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/SoulRend.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Light Side System** — resources_config_light_abilities, resources_ranks_light_ranks, resources_ranks_light_specializations, resources_passives_light_passives [INFERRED 0.95]
- **Dark Side System** — resources_config_dark_abilities, resources_ranks_dark_ranks, resources_ranks_dark_specializations, resources_passives_dark_passives [INFERRED 0.95]

## Communities (64 total, 4 thin omitted)

### Community 0 - "Admin Commands & Dark Abilities"
Cohesion: 0.06
Nodes (34): Ability, ForceCrush, ForceLightning, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+26 more)

### Community 1 - "Admin Command Handlers"
Cohesion: 0.09
Nodes (24): CommandExecutor, ForceAdminCommand, ForceStatsCommand, LevelingManager, ExperienceListener, LevelingManager, PlayerExpChangeEvent, PlayerLevelChangeEvent (+16 more)

### Community 2 - "Force Enchant Command & GUI"
Cohesion: 0.07
Nodes (26): ForceEnchantCommand, ForceEnchantment, ForceEnchantManager, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, Command, CommandSender (+18 more)

### Community 3 - "Database Manager"
Cohesion: 0.08
Nodes (16): DatabaseManager, ForceUser, PassiveListener, ForcePlugin, ForceUser, String, UUID, ForceSide (+8 more)

### Community 4 - "Force Bar & Connection Listeners"
Cohesion: 0.08
Nodes (20): ForceBarManager, Listener, PlayerConnectionListener, ProjectileDeflectionListener, ForceBarManager, ForceUserManager, PlayerQuitEvent, ProjectileHitEvent (+12 more)

### Community 5 - "Dark Corrupt Ability"
Cohesion: 0.09
Nodes (20): ForceCorrupt, ForceJudgment, ParticleUtil, Object, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser (+12 more)

### Community 6 - "Cooldown & Ability Listener"
Cohesion: 0.09
Nodes (18): CooldownManager, AbilityListener, ActionBarUtil, CooldownManager, TelekinesisManager, TelekinesisManager, EntityDamageByEntityEvent, EventHandler (+10 more)

### Community 7 - "Rank Data Model"
Cohesion: 0.09
Nodes (17): Rank, ForcePlaceholders, RankManager, NotNull, PlaceholderExpansion, Rank, List, String (+9 more)

### Community 8 - "Ability Manager"
Cohesion: 0.14
Nodes (13): Collection, AbilityManager, PassiveManager, Ability, AbilityConfigManager, ForcePlugin, ForceSide, TelekinesisManager (+5 more)

### Community 9 - "Force Points & GUI Manager"
Cohesion: 0.20
Nodes (10): GUIListener, AbilityConfigManager, Ability, EventHandler, ForcePlugin, ForceUser, InventoryClickEvent, Passive (+2 more)

### Community 10 - "Force Enchant GUI"
Cohesion: 0.16
Nodes (10): ForceEnchantGUI, GUIManager, Material, PassiveManager, Ability, ForceUser, ItemStack, Passive (+2 more)

### Community 11 - "Light Camouflage Ultimate"
Cohesion: 0.15
Nodes (10): ForceCamouflage, AbilityConfigManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String (+2 more)

### Community 12 - "Dark Force Drain"
Cohesion: 0.15
Nodes (11): ForceDrain, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

### Community 13 - "Holocron Listener"
Cohesion: 0.19
Nodes (10): HolocronListener, PlayerDeathEvent, PlayerDropItemEvent, PlayerItemHeldEvent, PlayerToggleSneakEvent, EventHandler, ForcePlugin, InventoryClickEvent (+2 more)

### Community 14 - "Chain Lightning"
Cohesion: 0.19
Nodes (11): ChainLightning, Entity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, List, LivingEntity (+3 more)

### Community 15 - "Version Adapter 1.16"
Cohesion: 0.19
Nodes (12): FallingBlock, ForceSide, ItemStack, Location, Override, Particle, Player, PotionEffectType (+4 more)

### Community 16 - "Version Adapter 1.21"
Cohesion: 0.17
Nodes (11): FallingBlock, ForceSide, ItemStack, Location, Override, Particle, Player, PotionEffectType (+3 more)

### Community 17 - "Ultimate Ability Listener"
Cohesion: 0.20
Nodes (6): EntityDamageEvent, UltimateAbilityListener, EntityDamageByEntityEvent, EventHandler, ForcePlugin, UUID

### Community 18 - "Version Adapter Interface"
Cohesion: 0.18
Nodes (10): FallingBlock, ForceSide, ItemStack, Location, Particle, Player, PotionEffectType, String (+2 more)

### Community 19 - "Dark Aura"
Cohesion: 0.22
Nodes (8): DarkAura, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 20 - "Force Choke"
Cohesion: 0.24
Nodes (8): ForceChoke, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 21 - "Force Rage"
Cohesion: 0.24
Nodes (8): ForceRage, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 22 - "Force Scream"
Cohesion: 0.24
Nodes (8): ForceScream, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 23 - "Soul Rend"
Cohesion: 0.22
Nodes (8): SoulRend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 24 - "Unstoppable Vengeance"
Cohesion: 0.24
Nodes (8): UnstoppableVengeance, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 25 - "Force Absorb Ultimate"
Cohesion: 0.24
Nodes (8): ForceAbsorb, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 26 - "Force Barrier"
Cohesion: 0.24
Nodes (8): ForceBarrier, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 27 - "Force Clarity"
Cohesion: 0.22
Nodes (8): ForceClarity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 28 - "Force Deflection"
Cohesion: 0.24
Nodes (8): ForceDeflection, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 29 - "Force Heal"
Cohesion: 0.24
Nodes (8): ForceHeal, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 30 - "Force Mend"
Cohesion: 0.22
Nodes (8): ForceMend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 31 - "Force Repulse"
Cohesion: 0.24
Nodes (8): ForceRepulse, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 32 - "Force Serenity Ultimate"
Cohesion: 0.24
Nodes (8): ForceSerenity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 33 - "Force Stasis"
Cohesion: 0.24
Nodes (8): ForceStasis, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 34 - "Force Valor"
Cohesion: 0.22
Nodes (8): ForceValor, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 35 - "Force Sense Ability"
Cohesion: 0.23
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForceSense

### Community 36 - "Telekinesis Ability"
Cohesion: 0.24
Nodes (8): AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, TelekinesisManager, Telekinesis

### Community 37 - "Force Tab Completer"
Cohesion: 0.38
Nodes (8): ForceTabCompleter, Nullable, Command, CommandSender, List, Override, String, TabCompleter

### Community 38 - "Mark of the Hunt"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 39 - "Plugin Entry Point"
Cohesion: 0.17
Nodes (8): ForcePlugin, GUIManager, JavaPlugin, AbilityManager, DatabaseManager, Override, RankManager, VersionAdapter

### Community 40 - "Ability Base Class"
Cohesion: 0.22
Nodes (5): Ability, ForceSide, ForceUser, Player, String

### Community 41 - "Holocron Manager"
Cohesion: 0.27
Nodes (6): HolocronManager, String, ForcePlugin, ForceSide, ItemStack, Player

### Community 42 - "Abstract Ability Framework"
Cohesion: 0.27
Nodes (5): AbstractAbility, AbilityConfigManager, ForcePlugin, Override, String

### Community 43 - "Force Side Command"
Cohesion: 0.27
Nodes (7): ForceCommand, HolocronManager, Command, CommandSender, ForcePlugin, Override, String

### Community 44 - "Version Utility"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 45 - "Passive Data Model"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 46 - "Ambient Effects Manager"
Cohesion: 0.39
Nodes (4): AmbientEffectsManager, ForcePlugin, ForceSide, Player

### Community 47 - "Config YAML Documents"
Cohesion: 0.36
Nodes (8): Abilities Configuration, Dark Side Abilities, Light Side Abilities, Progression System Configuration, Dark Side Passives, Light Side Passives, Dark Side Ranks, Light Side Ranks

### Community 48 - "GUI Manager Imports"
Cohesion: 0.29
Nodes (6): AbilityConfigManager, AbilityManager, ForcePlugin, ForceUserManager, PassiveManager, RankManager

### Community 49 - "Plugin Commands & Enchanting Config"
Cohesion: 0.33
Nodes (6): Force Enchanting Configuration, Force Command, ForceAdmin Command, ForceEnchant Command, ForcePlugin, ForceStats Command

### Community 50 - "Rank Specializations"
Cohesion: 0.67
Nodes (3): Dark Side Specializations, Light Side Specializations, Specialization Paths

## Knowledge Gaps
- **51 isolated node(s):** `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `ForcePlugin`, `ForceUserManager`, `AbilityManager` (+46 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstract Ability Framework` to `Admin Commands & Dark Abilities`, `Force Valor`, `Force Sense Ability`, `Dark Corrupt Ability`, `Dark Aura`, `Soul Rend`, `Force Clarity`, `Force Mend`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **Why does `Material` connect `Force Enchant GUI` to `Force Enchant Command & GUI`, `Rank Data Model`, `Force Points & GUI Manager`, `Holocron Manager`, `Version Adapter 1.16`, `Version Adapter 1.21`?**
  _High betweenness centrality (0.087) - this node is a cross-community bridge._
- **Why does `ForcePlugin` connect `Plugin Entry Point` to `Admin Command Handlers`, `Force Enchant Command & GUI`, `Cooldown & Ability Listener`, `Ability Manager`, `Force Points & GUI Manager`, `Force Enchant GUI`, `Holocron Listener`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **What connects `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `ForcePlugin` to the rest of the system?**
  _51 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Commands & Dark Abilities` be split into smaller, more focused modules?**
  _Cohesion score 0.05669710806697108 - nodes in this community are weakly interconnected._
- **Should `Admin Command Handlers` be split into smaller, more focused modules?**
  _Cohesion score 0.08711433756805807 - nodes in this community are weakly interconnected._
- **Should `Force Enchant Command & GUI` be split into smaller, more focused modules?**
  _Cohesion score 0.06734006734006734 - nodes in this community are weakly interconnected._