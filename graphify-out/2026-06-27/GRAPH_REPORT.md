# Graph Report - ForcePlugin  (2026-06-26)

## Corpus Check
- 92 files · ~35,175 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1191 nodes · 2673 edges · 80 communities (71 shown, 9 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 544 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `91f6188d`
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
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]
- [[_COMMUNITY_Force Stasis|Force Stasis]]
- [[_COMMUNITY_Community 34|Community 34]]
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
- [[_COMMUNITY_Community 78|Community 78]]
- [[_COMMUNITY_Community 79|Community 79]]
- [[_COMMUNITY_Community 80|Community 80]]
- [[_COMMUNITY_Community 82|Community 82]]
- [[_COMMUNITY_Community 83|Community 83]]

## God Nodes (most connected - your core abstractions)
1. `ForcePlugin — Long-Term Development Roadmap` - 55 edges
2. `ForceUser` - 33 edges
3. `AbstractAbility` - 22 edges
4. `ForcePlugin` - 20 edges
5. `GUIManager` - 17 edges
6. `GUIListener` - 14 edges
7. `UltimateAbilityListener` - 14 edges
8. `String` - 13 edges
9. `ForceAdminCommand` - 12 edges
10. `ForceEnchantManager` - 12 edges

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

## Communities (80 total, 9 thin omitted)

### Community 0 - "Admin Commands & Dark Abilities"
Cohesion: 0.14
Nodes (13): CooldownManager, AbilityListener, PlayerInteractEvent, PlayerToggleSneakEvent, TelekinesisManager, Ability, EntityDamageByEntityEvent, EventHandler (+5 more)

### Community 1 - "Admin Command Handlers"
Cohesion: 0.20
Nodes (7): ForceAdminCommand, ForceSide, Command, CommandSender, ForcePlugin, Override, String

### Community 2 - "Force Enchant Command & GUI"
Cohesion: 0.05
Nodes (33): CommandExecutor, ForceEnchantCommand, ForceStatsCommand, ForceEnchantment, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, RankManager (+25 more)

### Community 3 - "Database Manager"
Cohesion: 0.18
Nodes (5): ForceUser, String, PassiveManager, Passive, EventHandler

### Community 4 - "Force Bar & Connection Listeners"
Cohesion: 0.25
Nodes (8): LevelingManager, ExperienceListener, PlayerExpChangeEvent, PlayerLevelChangeEvent, EntityDeathEvent, EventHandler, ForcePlugin, PlayerJoinEvent

### Community 5 - "Dark Corrupt Ability"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 6 - "Cooldown & Ability Listener"
Cohesion: 0.16
Nodes (10): LevelingManager, Override, String, AbilityManager, RankManager, String, ForcePlugin, ForceUser (+2 more)

### Community 7 - "Rank Data Model"
Cohesion: 0.24
Nodes (8): ForceRepulse, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 8 - "Ability Manager"
Cohesion: 0.21
Nodes (7): ForceEnchantGUI, GUIManager, ItemStack, Ability, ForceUser, Player, String

### Community 9 - "Force Points & GUI Manager"
Cohesion: 0.16
Nodes (15): EventHandler, ForceEnchantManager, ForcePlugin, GUIListener, HudManager, InventoryClickEvent, AbilityConfigManager, ForceUserManager (+7 more)

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
Cohesion: 0.04
Nodes (55): ForcePlugin — Long-Term Development Roadmap, Version 2.6 — Dead Code Purge & Legacy Cleanup [COMPLETED], Version 2.7 — Side Switching: Redemption & Fall [COMPLETED], Version 2.8 — Database & Data Layer Hardening, Version 2.9 — ForceBar & Energy System Overhaul, Version 3.0 — The GUI Rework, Version 3.1 — New Ability: Force Speed, Version 3.2 — New Ability: Force Vision (+47 more)

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
Cohesion: 0.25
Nodes (7): AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, ForcePush

### Community 25 - "Force Absorb Ultimate"
Cohesion: 0.33
Nodes (4): ActionBarUtil, Player, String, SuppressWarnings

### Community 26 - "Force Barrier"
Cohesion: 0.24
Nodes (8): ForceBarrier, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 27 - "Force Clarity"
Cohesion: 0.22
Nodes (8): ForceClarity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 28 - "Force Deflection"
Cohesion: 0.08
Nodes (24): ForceAbsorb, ForceJudgment, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+16 more)

### Community 29 - "Force Heal"
Cohesion: 0.15
Nodes (11): ForceDrain, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

### Community 30 - "Force Mend"
Cohesion: 0.22
Nodes (8): ForceMend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 31 - "Community 31"
Cohesion: 0.24
Nodes (8): ForceCrush, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 32 - "Community 32"
Cohesion: 0.24
Nodes (8): ForceSerenity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 33 - "Force Stasis"
Cohesion: 0.24
Nodes (8): ForceStasis, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 34 - "Community 34"
Cohesion: 0.26
Nodes (6): PassiveManager, ForcePlugin, ForceSide, List, Passive, String

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
Cohesion: 0.26
Nodes (5): HudManager, EventHandler, ForcePlugin, PlayerItemHeldEvent, PlayerQuitEvent

### Community 39 - "Plugin Entry Point"
Cohesion: 0.06
Nodes (29): ForceCorrupt, ForceLightning, GUIManager, JavaPlugin, AmbientEffectsManager, ParticleUtil, Object, AbilityConfigManager (+21 more)

### Community 40 - "Ability Base Class"
Cohesion: 0.22
Nodes (5): Ability, ForceSide, ForceUser, Player, String

### Community 41 - "Holocron Manager"
Cohesion: 0.57
Nodes (3): CooldownManager, Player, String

### Community 42 - "Abstract Ability Framework"
Cohesion: 0.24
Nodes (5): AbstractAbility, AbilityConfigManager, ForcePlugin, Override, String

### Community 43 - "Community 43"
Cohesion: 0.27
Nodes (6): ForceBarManager, PlayerConnectionListener, EventHandler, ForcePlugin, PlayerJoinEvent, PlayerQuitEvent

### Community 44 - "Version Utility"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 45 - "Passive Data Model"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 46 - "Community 46"
Cohesion: 0.33
Nodes (4): ActionTrigger, AbilityConfigManager, ForcePlugin, String

### Community 48 - "Community 48"
Cohesion: 0.36
Nodes (4): ProjectileDeflectionListener, ProjectileHitEvent, EventHandler, UUID

### Community 49 - "Community 49"
Cohesion: 0.24
Nodes (8): ForceDeflection, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 50 - "Rank Specializations"
Cohesion: 0.67
Nodes (3): Dark Side Specializations, Light Side Specializations, Specialization Paths

### Community 60 - "Community 60"
Cohesion: 0.14
Nodes (15): Ability, Collection, ForceCamouflage, AbilityManager, AbilityConfigManager, ForceSide, ForceUser, Override (+7 more)

### Community 64 - "Community 64"
Cohesion: 0.12
Nodes (11): Rank, AbilityPickerGUI, BindGUI, Material, List, String, ForcePlugin, Player (+3 more)

### Community 65 - "Community 65"
Cohesion: 0.29
Nodes (6): AbilityConfigManager, AbilityManager, ForceUserManager, PassiveManager, RankManager, ForcePlugin

### Community 66 - "Community 66"
Cohesion: 0.24
Nodes (8): ForceChoke, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 67 - "Community 67"
Cohesion: 0.38
Nodes (5): Listener, HotbarListener, EventHandler, ForcePlugin, PlayerItemHeldEvent

### Community 68 - "Community 68"
Cohesion: 0.38
Nodes (4): PassiveListener, EntityDamageByEntityEvent, EntityDeathEvent, ForcePlugin

### Community 69 - "Community 69"
Cohesion: 0.27
Nodes (8): ForceCommand, Command, CommandSender, ForcePlugin, ForceUser, Override, Player, String

### Community 70 - "Community 70"
Cohesion: 0.12
Nodes (9): DatabaseManager, Integer, Map, ForcePlugin, ForceUser, String, UUID, Override (+1 more)

### Community 71 - "Community 71"
Cohesion: 0.43
Nodes (3): ForceBarManager, ForcePlugin, ForceUserManager

### Community 72 - "Community 72"
Cohesion: 0.33
Nodes (5): ForceUserManager, DatabaseManager, ForcePlugin, ForceUser, Player

## Knowledge Gaps
- **110 isolated node(s):** `Version 2.6 — Dead Code Purge & Legacy Cleanup [COMPLETED]`, `Version 2.7 — Side Switching: Redemption & Fall [COMPLETED]`, `Version 2.8 — Database & Data Layer Hardening`, `Version 2.9 — ForceBar & Energy System Overhaul`, `Version 3.0 — The GUI Rework` (+105 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstract Ability Framework` to `Force Sense Ability`, `Plugin Entry Point`, `Light Camouflage Ultimate`, `Dark Aura`, `Soul Rend`, `Force Clarity`, `Community 60`, `Force Mend`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **Why does `Material` connect `Community 64` to `Force Enchant Command & GUI`, `Ability Manager`, `Force Points & GUI Manager`, `Version Adapter 1.16`, `Version Adapter 1.21`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **Why does `ForceUser` connect `Database Manager` to `Admin Commands & Dark Abilities`, `Admin Command Handlers`, `Force Enchant Command & GUI`, `Community 70`, `Cooldown & Ability Listener`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `Version 2.6 — Dead Code Purge & Legacy Cleanup [COMPLETED]`, `Version 2.7 — Side Switching: Redemption & Fall [COMPLETED]`, `Version 2.8 — Database & Data Layer Hardening` to the rest of the system?**
  _110 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Commands & Dark Abilities` be split into smaller, more focused modules?**
  _Cohesion score 0.14 - nodes in this community are weakly interconnected._
- **Should `Force Enchant Command & GUI` be split into smaller, more focused modules?**
  _Cohesion score 0.052917232021709636 - nodes in this community are weakly interconnected._
- **Should `Holocron Listener` be split into smaller, more focused modules?**
  _Cohesion score 0.03571428571428571 - nodes in this community are weakly interconnected._