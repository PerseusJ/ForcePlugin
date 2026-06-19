# Graph Report - ForcePlugin  (2026-06-19)

## Corpus Check
- 94 files · ~28,079 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1214 nodes · 2712 edges · 78 communities (69 shown, 9 thin omitted)
- Extraction: 79% EXTRACTED · 21% INFERRED · 0% AMBIGUOUS · INFERRED: 561 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `34aa1993`
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
- [[_COMMUNITY_Plugin Commands & Enchanting Config|Plugin Commands & Enchanting Config]]
- [[_COMMUNITY_Rank Specializations|Rank Specializations]]
- [[_COMMUNITY_VS Code Settings|VS Code Settings]]
- [[_COMMUNITY_Force Energy Config|Force Energy Config]]
- [[_COMMUNITY_Ultimate Abilities Config|Ultimate Abilities Config]]
- [[_COMMUNITY_Neutral Passives|Neutral Passives]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]
- [[_COMMUNITY_Community 71|Community 71]]
- [[_COMMUNITY_Community 72|Community 72]]
- [[_COMMUNITY_Community 78|Community 78]]
- [[_COMMUNITY_Community 79|Community 79]]
- [[_COMMUNITY_Community 80|Community 80]]
- [[_COMMUNITY_Community 82|Community 82]]
- [[_COMMUNITY_Community 83|Community 83]]

## God Nodes (most connected - your core abstractions)
1. `ForceUser` - 36 edges
2. `AbstractAbility` - 22 edges
3. `ForcePlugin` - 20 edges
4. `GUIManager` - 17 edges
5. `String` - 15 edges
6. `GUIListener` - 15 edges
7. `UltimateAbilityListener` - 14 edges
8. `ForceAdminCommand` - 12 edges
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

## Communities (78 total, 9 thin omitted)

### Community 0 - "Admin Commands & Dark Abilities"
Cohesion: 0.13
Nodes (13): CooldownManager, AbilityListener, TelekinesisManager, Ability, EntityDamageByEntityEvent, EventHandler, ForcePlugin, ForceUser (+5 more)

### Community 1 - "Admin Command Handlers"
Cohesion: 0.20
Nodes (7): LevelingManager, Override, String, RankManager, ForcePlugin, ForceUser, Player

### Community 2 - "Force Enchant Command & GUI"
Cohesion: 0.08
Nodes (21): ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, Enchantment, Enchantment (+13 more)

### Community 3 - "Database Manager"
Cohesion: 0.15
Nodes (6): ForceUser, ForceUser, ForceSide, Integer, Map, UUID

### Community 4 - "Force Bar & Connection Listeners"
Cohesion: 0.09
Nodes (15): ForceBarManager, PlayerConnectionListener, ForceBarManager, HudManager, EventHandler, ForcePlugin, PlayerJoinEvent, PlayerQuitEvent (+7 more)

### Community 5 - "Dark Corrupt Ability"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 6 - "Cooldown & Ability Listener"
Cohesion: 0.18
Nodes (9): ForcePlugin, GUIManager, HudManager, JavaPlugin, AbilityManager, DatabaseManager, ForceUserManager, Player (+1 more)

### Community 7 - "Rank Data Model"
Cohesion: 0.27
Nodes (7): ExperienceListener, PlayerExpChangeEvent, PlayerLevelChangeEvent, EntityDeathEvent, EventHandler, ForcePlugin, PlayerJoinEvent

### Community 8 - "Ability Manager"
Cohesion: 0.06
Nodes (33): GUIManager, Listener, HolocronListener, PassiveListener, PassiveManager, String, PassiveManager, Ability (+25 more)

### Community 9 - "Force Points & GUI Manager"
Cohesion: 0.22
Nodes (9): GUIListener, InventoryClickEvent, Ability, EventHandler, ForcePlugin, ForceUser, Passive, Player (+1 more)

### Community 10 - "Force Enchant GUI"
Cohesion: 0.11
Nodes (18): Bug #1: Hotbar Slot Synchronization (Keyboard Numbers Issue), Bug #2: Abilities Not Activating, Bug #3: Legacy Holocron Item Given at Side Selection, Bug #4: Scoreboard Not Reflecting Abilities Correctly, Diagnosis of All Four Bugs, Fix Implementation Plan, [MODIFY] `AbilityListener.java` — Remove holocron gate, add SHIFT event trigger, [MODIFY] `AmbientEffectsManager.java` — Stop relying on holocron (+10 more)

### Community 11 - "Light Camouflage Ultimate"
Cohesion: 0.22
Nodes (8): ForceValor, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 12 - "Dark Force Drain"
Cohesion: 0.24
Nodes (8): ForceDrain, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 13 - "Holocron Listener"
Cohesion: 0.10
Nodes (19): CommandExecutor, ForceCommand, ForceEnchantCommand, ForceStatsCommand, Command, CommandSender, ForcePlugin, ForceUser (+11 more)

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
Cohesion: 0.10
Nodes (15): UnstoppableVengeance, EntityDamageEvent, UltimateAbilityListener, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+7 more)

### Community 18 - "Version Adapter Interface"
Cohesion: 0.18
Nodes (10): FallingBlock, ForceSide, ItemStack, Location, Particle, Player, PotionEffectType, String (+2 more)

### Community 19 - "Dark Aura"
Cohesion: 0.22
Nodes (8): DarkAura, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 20 - "Force Choke"
Cohesion: 0.57
Nodes (3): CooldownManager, Player, String

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
Cohesion: 0.33
Nodes (4): ActionBarUtil, Player, String, SuppressWarnings

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
Cohesion: 0.14
Nodes (12): ForceDeflection, ProjectileDeflectionListener, ProjectileHitEvent, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+4 more)

### Community 29 - "Force Heal"
Cohesion: 0.15
Nodes (11): ForceHeal, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

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
Cohesion: 0.31
Nodes (5): ForceUserManager, DatabaseManager, ForcePlugin, ForceUser, Player

### Community 35 - "Force Sense Ability"
Cohesion: 0.23
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForceSense

### Community 36 - "Telekinesis Ability"
Cohesion: 0.14
Nodes (12): TelekinesisManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, TelekinesisManager (+4 more)

### Community 37 - "Force Tab Completer"
Cohesion: 0.09
Nodes (21): ForceTabCompleter, DatabaseManager, ForcePlaceholders, NotNull, Nullable, PlaceholderExpansion, Command, CommandSender (+13 more)

### Community 38 - "Community 38"
Cohesion: 0.24
Nodes (8): ForceChoke, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 39 - "Plugin Entry Point"
Cohesion: 0.07
Nodes (25): ForceCorrupt, ForceLightning, AmbientEffectsManager, ParticleUtil, Object, AbilityConfigManager, ForcePlugin, ForceSide (+17 more)

### Community 40 - "Ability Base Class"
Cohesion: 0.22
Nodes (5): Ability, ForceSide, ForceUser, Player, String

### Community 41 - "Holocron Manager"
Cohesion: 0.08
Nodes (25): 1. Architecture Overview, 2.1 New Enum — `ActionTrigger`, 2.2 New Field in `ForceUser` — `slotBinds`, 2.3 Database `slot_binds` Column, 2.4 No Changes to `Ability` Interface, 2. Data Structures, 3.1 NEW Files, 3.2 MODIFY Files (+17 more)

### Community 42 - "Abstract Ability Framework"
Cohesion: 0.27
Nodes (5): AbstractAbility, AbilityConfigManager, ForcePlugin, Override, String

### Community 43 - "Community 43"
Cohesion: 0.25
Nodes (7): ForceCamouflage, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 44 - "Version Utility"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 45 - "Passive Data Model"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 46 - "Community 46"
Cohesion: 0.25
Nodes (7): AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, ForcePush

### Community 47 - "Config YAML Documents"
Cohesion: 0.36
Nodes (8): Abilities Configuration, Dark Side Abilities, Light Side Abilities, Progression System Configuration, Dark Side Passives, Light Side Passives, Dark Side Ranks, Light Side Ranks

### Community 48 - "Community 48"
Cohesion: 0.31
Nodes (4): ActionTrigger, AbilityConfigManager, ForcePlugin, String

### Community 49 - "Plugin Commands & Enchanting Config"
Cohesion: 0.33
Nodes (6): Force Enchanting Configuration, Force Command, ForceAdmin Command, ForceEnchant Command, ForcePlugin, ForceStats Command

### Community 50 - "Rank Specializations"
Cohesion: 0.67
Nodes (3): Dark Side Specializations, Light Side Specializations, Specialization Paths

### Community 64 - "Community 64"
Cohesion: 0.08
Nodes (18): Rank, AbilityPickerGUI, BindGUI, RankManager, Material, Rank, List, String (+10 more)

### Community 65 - "Community 65"
Cohesion: 0.24
Nodes (7): ForceAdminCommand, LevelingManager, Command, CommandSender, ForcePlugin, Override, String

### Community 66 - "Community 66"
Cohesion: 0.23
Nodes (8): ForceCrush, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 68 - "Community 68"
Cohesion: 0.24
Nodes (8): ForceJudgment, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 69 - "Community 69"
Cohesion: 0.24
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForcePull

### Community 71 - "Community 71"
Cohesion: 0.33
Nodes (7): Collection, AbilityManager, Ability, AbilityConfigManager, ForcePlugin, ForceSide, TelekinesisManager

### Community 72 - "Community 72"
Cohesion: 0.33
Nodes (5): Ability, HotbarListener, EventHandler, ForcePlugin, PlayerItemHeldEvent

## Knowledge Gaps
- **92 isolated node(s):** `$schema`, `plugin`, `@opencode-ai/plugin`, `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration` (+87 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstract Ability Framework` to `Force Sense Ability`, `Plugin Entry Point`, `Community 72`, `Light Camouflage Ultimate`, `Dark Aura`, `Soul Rend`, `Force Clarity`, `Force Mend`?**
  _High betweenness centrality (0.103) - this node is a cross-community bridge._
- **Why does `Material` connect `Community 64` to `Force Enchant Command & GUI`, `Ability Manager`, `Force Points & GUI Manager`, `Holocron Listener`, `Version Adapter 1.16`, `Version Adapter 1.21`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Why does `ForceUser` connect `Database Manager` to `Admin Commands & Dark Abilities`, `Admin Command Handlers`, `Community 65`, `Force Enchant Command & GUI`, `Cooldown & Ability Listener`, `Ability Manager`, `Force Points & GUI Manager`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **What connects `$schema`, `plugin`, `@opencode-ai/plugin` to the rest of the system?**
  _92 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Commands & Dark Abilities` be split into smaller, more focused modules?**
  _Cohesion score 0.13230769230769232 - nodes in this community are weakly interconnected._
- **Should `Force Enchant Command & GUI` be split into smaller, more focused modules?**
  _Cohesion score 0.08078231292517007 - nodes in this community are weakly interconnected._
- **Should `Force Bar & Connection Listeners` be split into smaller, more focused modules?**
  _Cohesion score 0.0946969696969697 - nodes in this community are weakly interconnected._