# Graph Report - ForcePlugin  (2026-06-19)

## Corpus Check
- 92 files · ~25,357 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1152 nodes · 2655 edges · 82 communities (73 shown, 9 thin omitted)
- Extraction: 79% EXTRACTED · 21% INFERRED · 0% AMBIGUOUS · INFERRED: 562 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6024ba69`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

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
- [[_COMMUNITY_Community 38|Community 38]]
- [[_COMMUNITY_Plugin Entry Point|Plugin Entry Point]]
- [[_COMMUNITY_Ability Base Class|Ability Base Class]]
- [[_COMMUNITY_Holocron Manager|Holocron Manager]]
- [[_COMMUNITY_Abstract Ability Framework|Abstract Ability Framework]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Version Utility|Version Utility]]
- [[_COMMUNITY_Passive Data Model|Passive Data Model]]
- [[_COMMUNITY_Community 46|Community 46]]
- [[_COMMUNITY_Config YAML Documents|Config YAML Documents]]
- [[_COMMUNITY_Community 48|Community 48]]
- [[_COMMUNITY_Community 49|Community 49]]
- [[_COMMUNITY_Rank Specializations|Rank Specializations]]
- [[_COMMUNITY_VS Code Settings|VS Code Settings]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Ultimate Abilities Config|Ultimate Abilities Config]]
- [[_COMMUNITY_Neutral Passives|Neutral Passives]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 67|Community 67]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]
- [[_COMMUNITY_Community 70|Community 70]]
- [[_COMMUNITY_Community 71|Community 71]]
- [[_COMMUNITY_Community 72|Community 72]]
- [[_COMMUNITY_Community 73|Community 73]]
- [[_COMMUNITY_Community 74|Community 74]]
- [[_COMMUNITY_Community 78|Community 78]]
- [[_COMMUNITY_Community 79|Community 79]]
- [[_COMMUNITY_Community 80|Community 80]]
- [[_COMMUNITY_Community 82|Community 82]]
- [[_COMMUNITY_Community 83|Community 83]]

## God Nodes (most connected - your core abstractions)
1. `ForceUser` - 36 edges
2. `AbstractAbility` - 22 edges
3. `ForcePlugin` - 20 edges
4. `GUIManager` - 16 edges
5. `String` - 15 edges
6. `UltimateAbilityListener` - 14 edges
7. `GUIListener` - 13 edges
8. `ForceAdminCommand` - 12 edges
9. `ForceEnchantManager` - 12 edges
10. `ChainLightning` - 11 edges

## Surprising Connections (you probably didn't know these)
- `DarkAura` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/DarkAura.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `ForceCorrupt` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/ForceCorrupt.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `SoulRend` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/dark/SoulRend.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `ForceClarity` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/light/ForceClarity.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java
- `ForceMend` --inherits--> `AbstractAbility`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/abilities/light/ForceMend.java → src/main/java/org/perseus/forcePlugin/abilities/AbstractAbility.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Light Side System** — resources_config_light_abilities, resources_ranks_light_ranks, resources_ranks_light_specializations, resources_passives_light_passives [INFERRED 0.95]
- **Dark Side System** — resources_config_dark_abilities, resources_ranks_dark_ranks, resources_ranks_dark_specializations, resources_passives_dark_passives [INFERRED 0.95]

## Communities (82 total, 9 thin omitted)

### Community 0 - "Admin Commands & Dark Abilities"
Cohesion: 0.15
Nodes (12): CooldownManager, AbilityListener, PlayerInteractEvent, Ability, EntityDamageByEntityEvent, EventHandler, ForcePlugin, ForceUser (+4 more)

### Community 1 - "Admin Command Handlers"
Cohesion: 0.27
Nodes (7): ForceAdminCommand, LevelingManager, Command, CommandSender, ForcePlugin, Override, String

### Community 2 - "Force Enchant Command & GUI"
Cohesion: 0.09
Nodes (19): ForceEnchantment, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, Enchantment, Enchantment, ForcePlugin, Integer (+11 more)

### Community 3 - "Database Manager"
Cohesion: 0.14
Nodes (7): ForceUser, ForceUser, ForceSide, Integer, Map, String, UUID

### Community 4 - "Force Bar & Connection Listeners"
Cohesion: 0.06
Nodes (29): Ability, ForceBarManager, Listener, HolocronListener, HotbarListener, PlayerConnectionListener, ProjectileDeflectionListener, ForceBarManager (+21 more)

### Community 5 - "Dark Corrupt Ability"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 6 - "Cooldown & Ability Listener"
Cohesion: 0.35
Nodes (4): LevelingManager, ForcePlugin, ForceUser, Player

### Community 7 - "Rank Data Model"
Cohesion: 0.19
Nodes (8): ForceStatsCommand, Command, CommandSender, ForcePlugin, Override, String, RankManager, String

### Community 8 - "Ability Manager"
Cohesion: 0.21
Nodes (8): ForceEnchantGUI, GUIManager, Ability, ForceUser, ItemStack, Passive, Player, String

### Community 9 - "Force Points & GUI Manager"
Cohesion: 0.20
Nodes (11): ForceEnchantManager, GUIListener, HudManager, InventoryClickEvent, Ability, EventHandler, ForcePlugin, ForceUser (+3 more)

### Community 10 - "Force Enchant GUI"
Cohesion: 0.24
Nodes (8): ForceHeal, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 11 - "Light Camouflage Ultimate"
Cohesion: 0.22
Nodes (8): ForceValor, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 12 - "Dark Force Drain"
Cohesion: 0.24
Nodes (8): UnstoppableVengeance, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 13 - "Holocron Listener"
Cohesion: 0.23
Nodes (8): ForceLightning, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 14 - "Chain Lightning"
Cohesion: 0.19
Nodes (11): ChainLightning, Entity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, List, LivingEntity (+3 more)

### Community 15 - "Version Adapter 1.16"
Cohesion: 0.20
Nodes (11): FallingBlock, ForceSide, ItemStack, Location, Override, Particle, Player, PotionEffectType (+3 more)

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
Cohesion: 0.25
Nodes (7): ForcePlaceholders, NotNull, PlaceholderExpansion, ForcePlugin, Override, Player, String

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
Cohesion: 0.31
Nodes (6): RankManager, Rank, ForcePlugin, ForceSide, ForceUser, List

### Community 25 - "Force Absorb Ultimate"
Cohesion: 0.27
Nodes (7): ExperienceListener, PlayerExpChangeEvent, PlayerLevelChangeEvent, EntityDeathEvent, EventHandler, ForcePlugin, PlayerJoinEvent

### Community 26 - "Force Barrier"
Cohesion: 0.24
Nodes (8): ForceBarrier, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 27 - "Force Clarity"
Cohesion: 0.22
Nodes (8): ForceClarity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 28 - "Force Deflection"
Cohesion: 0.24
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForcePull

### Community 29 - "Force Heal"
Cohesion: 0.15
Nodes (11): ForceDrain, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

### Community 30 - "Force Mend"
Cohesion: 0.22
Nodes (8): ForceMend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 31 - "Force Repulse"
Cohesion: 0.15
Nodes (9): ForcePlugin, GUIManager, JavaPlugin, AbilityConfigManager, DatabaseManager, Override, TelekinesisManager, VersionAdapter (+1 more)

### Community 32 - "Force Serenity Ultimate"
Cohesion: 0.24
Nodes (8): ForceSerenity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 33 - "Force Stasis"
Cohesion: 0.24
Nodes (8): ForceStasis, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 34 - "Force Valor"
Cohesion: 0.26
Nodes (5): AbilityManager, ForceUserManager, Player, String, Player

### Community 35 - "Force Sense Ability"
Cohesion: 0.23
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForceSense

### Community 36 - "Telekinesis Ability"
Cohesion: 0.14
Nodes (12): TelekinesisManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, TelekinesisManager (+4 more)

### Community 37 - "Force Tab Completer"
Cohesion: 0.32
Nodes (9): ForceTabCompleter, Nullable, Command, CommandSender, ForcePlugin, List, Override, String (+1 more)

### Community 38 - "Community 38"
Cohesion: 0.33
Nodes (4): ActionBarUtil, Player, String, SuppressWarnings

### Community 39 - "Plugin Entry Point"
Cohesion: 0.15
Nodes (12): ForceCorrupt, ParticleUtil, Object, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+4 more)

### Community 40 - "Ability Base Class"
Cohesion: 0.22
Nodes (5): Ability, ForceSide, ForceUser, Player, String

### Community 41 - "Holocron Manager"
Cohesion: 0.57
Nodes (3): CooldownManager, Player, String

### Community 42 - "Abstract Ability Framework"
Cohesion: 0.27
Nodes (5): AbstractAbility, AbilityConfigManager, ForcePlugin, Override, String

### Community 43 - "Community 43"
Cohesion: 0.23
Nodes (6): PassiveListener, PassiveManager, EntityDamageByEntityEvent, EntityDeathEvent, EventHandler, ForcePlugin

### Community 44 - "Version Utility"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 45 - "Passive Data Model"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 46 - "Community 46"
Cohesion: 0.25
Nodes (7): AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, ForcePush

### Community 48 - "Community 48"
Cohesion: 0.24
Nodes (8): ForceAbsorb, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 49 - "Community 49"
Cohesion: 0.24
Nodes (8): ForceDeflection, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 50 - "Rank Specializations"
Cohesion: 0.67
Nodes (3): Dark Side Specializations, Light Side Specializations, Specialization Paths

### Community 60 - "Community 60"
Cohesion: 0.25
Nodes (7): ForceCamouflage, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 64 - "Community 64"
Cohesion: 0.09
Nodes (17): ForceEnchantCommand, Rank, AbilityPickerGUI, BindGUI, Material, Command, CommandSender, ForcePlugin (+9 more)

### Community 65 - "Community 65"
Cohesion: 0.26
Nodes (6): PassiveManager, ForcePlugin, ForceSide, List, Passive, String

### Community 66 - "Community 66"
Cohesion: 0.08
Nodes (24): ForceChoke, ForceCrush, ForceRepulse, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+16 more)

### Community 67 - "Community 67"
Cohesion: 0.23
Nodes (4): ActionTrigger, AbilityConfigManager, ForcePlugin, String

### Community 68 - "Community 68"
Cohesion: 0.24
Nodes (8): ForceJudgment, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 69 - "Community 69"
Cohesion: 0.26
Nodes (9): CommandExecutor, ForceCommand, Command, CommandSender, ForcePlugin, ForceUser, Override, Player (+1 more)

### Community 70 - "Community 70"
Cohesion: 0.31
Nodes (4): DatabaseManager, ForcePlugin, String, UUID

### Community 71 - "Community 71"
Cohesion: 0.33
Nodes (7): Collection, AbilityManager, Ability, AbilityConfigManager, ForcePlugin, ForceSide, TelekinesisManager

### Community 72 - "Community 72"
Cohesion: 0.33
Nodes (5): ForceUserManager, DatabaseManager, ForcePlugin, ForceUser, Player

### Community 73 - "Community 73"
Cohesion: 0.39
Nodes (4): AmbientEffectsManager, ForcePlugin, ForceSide, Player

### Community 74 - "Community 74"
Cohesion: 0.29
Nodes (6): AbilityConfigManager, AbilityManager, ForcePlugin, ForceUserManager, PassiveManager, RankManager

## Knowledge Gaps
- **57 isolated node(s):** `$schema`, `plugin`, `@opencode-ai/plugin`, `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration` (+52 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstract Ability Framework` to `Force Sense Ability`, `Force Bar & Connection Listeners`, `Plugin Entry Point`, `Light Camouflage Ultimate`, `Dark Aura`, `Soul Rend`, `Force Clarity`, `Force Mend`?**
  _High betweenness centrality (0.109) - this node is a cross-community bridge._
- **Why does `Material` connect `Community 64` to `Force Enchant Command & GUI`, `Ability Manager`, `Force Points & GUI Manager`, `Version Adapter 1.16`, `Version Adapter 1.21`?**
  _High betweenness centrality (0.071) - this node is a cross-community bridge._
- **Why does `ForcePlugin` connect `Force Repulse` to `Admin Commands & Dark Abilities`, `Admin Command Handlers`, `Force Valor`, `Force Enchant Command & GUI`, `Rank Data Model`, `Force Points & GUI Manager`, `Community 43`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **What connects `$schema`, `plugin`, `@opencode-ai/plugin` to the rest of the system?**
  _57 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Force Enchant Command & GUI` be split into smaller, more focused modules?**
  _Cohesion score 0.08879492600422834 - nodes in this community are weakly interconnected._
- **Should `Database Manager` be split into smaller, more focused modules?**
  _Cohesion score 0.13725490196078433 - nodes in this community are weakly interconnected._
- **Should `Force Bar & Connection Listeners` be split into smaller, more focused modules?**
  _Cohesion score 0.05764411027568922 - nodes in this community are weakly interconnected._