# Graph Report - ForcePlugin  (2026-06-19)

## Corpus Check
- 92 files · ~25,336 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1151 nodes · 2659 edges · 71 communities (62 shown, 9 thin omitted)
- Extraction: 79% EXTRACTED · 21% INFERRED · 0% AMBIGUOUS · INFERRED: 557 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `0b1d5d29`
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
- [[_COMMUNITY_Version Utility|Version Utility]]
- [[_COMMUNITY_Passive Data Model|Passive Data Model]]
- [[_COMMUNITY_Community 46|Community 46]]
- [[_COMMUNITY_Config YAML Documents|Config YAML Documents]]
- [[_COMMUNITY_Rank Specializations|Rank Specializations]]
- [[_COMMUNITY_VS Code Settings|VS Code Settings]]
- [[_COMMUNITY_Ultimate Abilities Config|Ultimate Abilities Config]]
- [[_COMMUNITY_Neutral Passives|Neutral Passives]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 71|Community 71]]
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
9. `List` - 12 edges
10. `ForceEnchantManager` - 12 edges

## Surprising Connections (you probably didn't know these)
- `Adapter_1_16` --implements--> `VersionAdapter`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/versioning/Adapter_1_16.java → src/main/java/org/perseus/forcePlugin/ForcePlugin.java
- `Adapter_1_21` --implements--> `VersionAdapter`  [EXTRACTED]
  src/main/java/org/perseus/forcePlugin/versioning/Adapter_1_21.java → src/main/java/org/perseus/forcePlugin/ForcePlugin.java
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

## Communities (71 total, 9 thin omitted)

### Community 0 - "Admin Commands & Dark Abilities"
Cohesion: 0.15
Nodes (12): CooldownManager, EntityDamageByEntityEvent, AbilityListener, PlayerInteractEvent, Ability, EventHandler, ForcePlugin, ForceUser (+4 more)

### Community 1 - "Admin Command Handlers"
Cohesion: 0.25
Nodes (6): Command, ForceAdminCommand, CommandSender, Override, ForcePlugin, String

### Community 2 - "Force Enchant Command & GUI"
Cohesion: 0.07
Nodes (26): ForceEnchantCommand, ForceEnchantment, ForceEnchantManager, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, Command, CommandSender (+18 more)

### Community 3 - "Database Manager"
Cohesion: 0.07
Nodes (21): DatabaseManager, ForceUser, Integer, PassiveListener, ForceUserManager, Map, ForcePlugin, ForceUser (+13 more)

### Community 4 - "Force Bar & Connection Listeners"
Cohesion: 0.06
Nodes (24): ForceBarManager, Listener, HotbarListener, PlayerConnectionListener, ProjectileDeflectionListener, ForceBarManager, HudManager, PlayerJoinEvent (+16 more)

### Community 5 - "Dark Corrupt Ability"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 6 - "Cooldown & Ability Listener"
Cohesion: 0.36
Nodes (4): LevelingManager, ForcePlugin, ForceUser, Player

### Community 7 - "Rank Data Model"
Cohesion: 0.16
Nodes (10): ForceStatsCommand, LevelingManager, Command, CommandSender, ForcePlugin, Override, String, ForceSide (+2 more)

### Community 8 - "Ability Manager"
Cohesion: 0.24
Nodes (8): ForceChoke, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 9 - "Force Points & GUI Manager"
Cohesion: 0.05
Nodes (43): AbilityConfigManager, AbilityManager, ActionTrigger, CommandExecutor, ForceCommand, EventHandler, ForceEnchantGUI, ForceUserManager (+35 more)

### Community 10 - "Force Enchant GUI"
Cohesion: 0.15
Nodes (11): ForceHeal, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

### Community 11 - "Light Camouflage Ultimate"
Cohesion: 0.22
Nodes (8): ForceValor, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 12 - "Dark Force Drain"
Cohesion: 0.24
Nodes (8): UnstoppableVengeance, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 13 - "Holocron Listener"
Cohesion: 0.24
Nodes (8): ForceRepulse, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

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
Cohesion: 0.11
Nodes (14): EntityDamageEvent, ForceCamouflage, UltimateAbilityListener, AbilityConfigManager, ForceSide, ForceUser, Override, Player (+6 more)

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
Cohesion: 0.24
Nodes (8): ForceDrain, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 30 - "Force Mend"
Cohesion: 0.22
Nodes (8): ForceMend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 31 - "Force Repulse"
Cohesion: 0.12
Nodes (11): ForcePlugin, GUIManager, JavaPlugin, AmbientEffectsManager, DatabaseManager, Override, ForcePlugin, ForceSide (+3 more)

### Community 32 - "Force Serenity Ultimate"
Cohesion: 0.24
Nodes (8): ForceSerenity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 33 - "Force Stasis"
Cohesion: 0.24
Nodes (8): ForceStasis, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 34 - "Force Valor"
Cohesion: 0.33
Nodes (4): AbilityManager, ForceUserManager, Player, String

### Community 35 - "Force Sense Ability"
Cohesion: 0.23
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForceSense

### Community 36 - "Telekinesis Ability"
Cohesion: 0.14
Nodes (12): TelekinesisManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, TelekinesisManager (+4 more)

### Community 37 - "Force Tab Completer"
Cohesion: 0.32
Nodes (9): ForceTabCompleter, List, Nullable, Command, CommandSender, ForcePlugin, Override, String (+1 more)

### Community 38 - "Community 38"
Cohesion: 0.17
Nodes (9): HolocronListener, ActionBarUtil, PlayerItemHeldEvent, EventHandler, ForcePlugin, PlayerToggleSneakEvent, Player, String (+1 more)

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

### Community 44 - "Version Utility"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 45 - "Passive Data Model"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 46 - "Community 46"
Cohesion: 0.25
Nodes (7): AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, ForcePush

### Community 50 - "Rank Specializations"
Cohesion: 0.67
Nodes (3): Dark Side Specializations, Light Side Specializations, Specialization Paths

### Community 64 - "Community 64"
Cohesion: 0.12
Nodes (11): Rank, AbilityPickerGUI, BindGUI, Material, List, String, ForcePlugin, Player (+3 more)

### Community 66 - "Community 66"
Cohesion: 0.06
Nodes (33): Ability, ForceCrush, ForceLightning, ForceAbsorb, ForceDeflection, AbilityConfigManager, ForcePlugin, ForceSide (+25 more)

### Community 68 - "Community 68"
Cohesion: 0.24
Nodes (8): ForceJudgment, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 71 - "Community 71"
Cohesion: 0.33
Nodes (7): Collection, AbilityManager, Ability, AbilityConfigManager, ForcePlugin, ForceSide, TelekinesisManager

## Knowledge Gaps
- **56 isolated node(s):** `Override`, `AbilityManager`, `AbilityConfigManager`, `RankManager`, `PassiveManager` (+51 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstract Ability Framework` to `Community 66`, `Force Sense Ability`, `Plugin Entry Point`, `Light Camouflage Ultimate`, `Dark Aura`, `Soul Rend`, `Force Clarity`, `Force Mend`?**
  _High betweenness centrality (0.097) - this node is a cross-community bridge._
- **Why does `Material` connect `Community 64` to `Version Adapter 1.21`, `Force Points & GUI Manager`, `Force Enchant Command & GUI`, `Version Adapter 1.16`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **Why does `ForcePlugin` connect `Force Repulse` to `Admin Commands & Dark Abilities`, `Admin Command Handlers`, `Force Valor`, `Force Enchant Command & GUI`, `Database Manager`, `Force Bar & Connection Listeners`, `Rank Data Model`, `Force Points & GUI Manager`, `Ultimate Ability Listener`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **What connects `Override`, `AbilityManager`, `AbilityConfigManager` to the rest of the system?**
  _56 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Commands & Dark Abilities` be split into smaller, more focused modules?**
  _Cohesion score 0.14624505928853754 - nodes in this community are weakly interconnected._
- **Should `Force Enchant Command & GUI` be split into smaller, more focused modules?**
  _Cohesion score 0.06918238993710692 - nodes in this community are weakly interconnected._
- **Should `Database Manager` be split into smaller, more focused modules?**
  _Cohesion score 0.06829573934837092 - nodes in this community are weakly interconnected._