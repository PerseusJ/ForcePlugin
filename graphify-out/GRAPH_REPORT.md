# Graph Report - .  (2026-06-08)

## Corpus Check
- Corpus is ~23,456 words - fits in a single context window. You may not need a graph.

## Summary
- 1096 nodes · 2510 edges · 66 communities (65 shown, 1 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 490 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Dark Light|Dark Light]]
- [[_COMMUNITY_Adapter Rank|Adapter Rank]]
- [[_COMMUNITY_Forceenchantmanager Forceenchantgui|Forceenchantmanager Forceenchantgui]]
- [[_COMMUNITY_Passivemanager Guimanager|Passivemanager Guimanager]]
- [[_COMMUNITY_Telekinesis Telekinesismanager|Telekinesis Telekinesismanager]]
- [[_COMMUNITY_Forcebarmanager Forceusermanager|Forcebarmanager Forceusermanager]]
- [[_COMMUNITY_Guimanager Forceuser|Guimanager Forceuser]]
- [[_COMMUNITY_Forcedeflection Light|Forcedeflection Light]]
- [[_COMMUNITY_Forcecorrupt Dark|Forcecorrupt Dark]]
- [[_COMMUNITY_Guilistener Guimanager|Guilistener Guimanager]]
- [[_COMMUNITY_Forcepush Universal|Forcepush Universal]]
- [[_COMMUNITY_Forcemend Light|Forcemend Light]]
- [[_COMMUNITY_Holocronlistener Getforceusermanager|Holocronlistener Getforceusermanager]]
- [[_COMMUNITY_Levelingmanager Forceuser|Levelingmanager Forceuser]]
- [[_COMMUNITY_Chainlightning Dark|Chainlightning Dark]]
- [[_COMMUNITY_Forceuser Databasemanager|Forceuser Databasemanager]]
- [[_COMMUNITY_Forcecommand Forceenchantcommand|Forcecommand Forceenchantcommand]]
- [[_COMMUNITY_Ultimateabilitylistener Entitydamageevent|Ultimateabilitylistener Entitydamageevent]]
- [[_COMMUNITY_Versionadapter Fallingblock|Versionadapter Fallingblock]]
- [[_COMMUNITY_Forceuser Forcebarmanager|Forceuser Forcebarmanager]]
- [[_COMMUNITY_Darkaura Dark|Darkaura Dark]]
- [[_COMMUNITY_Forcechoke Dark|Forcechoke Dark]]
- [[_COMMUNITY_Forcelightning Dark|Forcelightning Dark]]
- [[_COMMUNITY_Forcerage Dark|Forcerage Dark]]
- [[_COMMUNITY_Forcescream Dark|Forcescream Dark]]
- [[_COMMUNITY_Soulrend Dark|Soulrend Dark]]
- [[_COMMUNITY_Unstoppablevengeance Dark|Unstoppablevengeance Dark]]
- [[_COMMUNITY_Databasemanager Forceuser|Databasemanager Forceuser]]
- [[_COMMUNITY_Forcebarrier Light|Forcebarrier Light]]
- [[_COMMUNITY_Forceclarity Light|Forceclarity Light]]
- [[_COMMUNITY_Forceheal Light|Forceheal Light]]
- [[_COMMUNITY_Forcejudgment Light|Forcejudgment Light]]
- [[_COMMUNITY_Forcerepulse Light|Forcerepulse Light]]
- [[_COMMUNITY_Forceserenity Light|Forceserenity Light]]
- [[_COMMUNITY_Forcestasis Light|Forcestasis Light]]
- [[_COMMUNITY_Forcevalor Light|Forcevalor Light]]
- [[_COMMUNITY_Forcepull Universal|Forcepull Universal]]
- [[_COMMUNITY_Forcesense Universal|Forcesense Universal]]
- [[_COMMUNITY_Forcetabcompleter Completeforce|Forcetabcompleter Completeforce]]
- [[_COMMUNITY_Markofthehunt Dark|Markofthehunt Dark]]
- [[_COMMUNITY_Forceplaceholders Createprogressbar|Forceplaceholders Createprogressbar]]
- [[_COMMUNITY_Abilitylistener Passivelistener|Abilitylistener Passivelistener]]
- [[_COMMUNITY_Databasemanager Disconnect|Databasemanager Disconnect]]
- [[_COMMUNITY_Abilitymanager Collection|Abilitymanager Collection]]
- [[_COMMUNITY_Holocronmanager Abilitymanager|Holocronmanager Abilitymanager]]
- [[_COMMUNITY_Ability Execute|Ability Execute]]
- [[_COMMUNITY_Forceadmincommand Handlecheck|Forceadmincommand Handlecheck]]
- [[_COMMUNITY_Experiencelistener Onexpchange|Experiencelistener Onexpchange]]
- [[_COMMUNITY_Abstractability Cfgint|Abstractability Cfgint]]
- [[_COMMUNITY_Rankmanager Getspecializations|Rankmanager Getspecializations]]
- [[_COMMUNITY_Versionutil Enchantment|Versionutil Enchantment]]
- [[_COMMUNITY_Passive Getdescription|Passive Getdescription]]
- [[_COMMUNITY_Ambienteffectsmanager Spawnambientparticle|Ambienteffectsmanager Spawnambientparticle]]
- [[_COMMUNITY_Cooldownmanager Getremainingcooldownformatted|Cooldownmanager Getremainingcooldownformatted]]
- [[_COMMUNITY_Guimanager Abilityconfigmanager|Guimanager Abilityconfigmanager]]
- [[_COMMUNITY_Actionbarutil Send|Actionbarutil Send]]
- [[_COMMUNITY_Vscode Settings|Vscode Settings]]

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

## Communities (66 total, 1 thin omitted)

### Community 0 - "Dark Light"
Cohesion: 0.06
Nodes (34): Ability, ForceCrush, ForceDrain, ForceAbsorb, ForceCamouflage, AbilityConfigManager, ForcePlugin, ForceSide (+26 more)

### Community 1 - "Adapter Rank"
Cohesion: 0.07
Nodes (27): Rank, Material, List, String, FallingBlock, ForceSide, ItemStack, Location (+19 more)

### Community 2 - "Forceenchantmanager Forceenchantgui"
Cohesion: 0.09
Nodes (19): ForceEnchantment, ForceEnchantment, ForceEnchantGUI, ForceEnchantManager, Enchantment, Enchantment, ForcePlugin, Integer (+11 more)

### Community 3 - "Passivemanager Guimanager"
Cohesion: 0.12
Nodes (14): GUIManager, PassiveManager, PassiveManager, Ability, ForceUser, ItemStack, Passive, String (+6 more)

### Community 4 - "Telekinesis Telekinesismanager"
Cohesion: 0.14
Nodes (12): TelekinesisManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, TelekinesisManager (+4 more)

### Community 5 - "Forcebarmanager Forceusermanager"
Cohesion: 0.11
Nodes (14): ForceBarManager, PlayerConnectionListener, ForceBarManager, ForceUserManager, PlayerQuitEvent, EventHandler, ForcePlugin, PlayerJoinEvent (+6 more)

### Community 6 - "Guimanager Forceuser"
Cohesion: 0.16
Nodes (6): Override, String, Override, String, AbilityConfigManager, Player

### Community 7 - "Forcedeflection Light"
Cohesion: 0.14
Nodes (12): ForceDeflection, ProjectileDeflectionListener, ProjectileHitEvent, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+4 more)

### Community 8 - "Forcecorrupt Dark"
Cohesion: 0.15
Nodes (12): ForceCorrupt, ParticleUtil, Object, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override (+4 more)

### Community 9 - "Guilistener Guimanager"
Cohesion: 0.16
Nodes (12): ForceEnchantGUI, ForceEnchantManager, GUIListener, AbilityManager, Ability, EventHandler, ForcePlugin, ForceUser (+4 more)

### Community 10 - "Forcepush Universal"
Cohesion: 0.15
Nodes (10): AbilityConfigManager, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String, ForcePlugin (+2 more)

### Community 11 - "Forcemend Light"
Cohesion: 0.15
Nodes (11): ForceMend, HealthUtil, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player (+3 more)

### Community 12 - "Holocronlistener Getforceusermanager"
Cohesion: 0.18
Nodes (11): HolocronListener, PlayerDeathEvent, PlayerDropItemEvent, PlayerItemHeldEvent, PlayerToggleSneakEvent, ForceUserManager, EventHandler, ForcePlugin (+3 more)

### Community 13 - "Levelingmanager Forceuser"
Cohesion: 0.19
Nodes (8): LevelingManager, Override, String, ForcePlugin, ForceUser, Player, ForceUser, String

### Community 14 - "Chainlightning Dark"
Cohesion: 0.19
Nodes (11): ChainLightning, Entity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, List, LivingEntity (+3 more)

### Community 15 - "Forceuser Databasemanager"
Cohesion: 0.19
Nodes (5): ForceUser, Integer, Map, String, UUID

### Community 16 - "Forcecommand Forceenchantcommand"
Cohesion: 0.13
Nodes (14): CommandExecutor, ForceCommand, ForceEnchantCommand, ForceStatsCommand, HolocronManager, Command, CommandSender, ForcePlugin (+6 more)

### Community 17 - "Ultimateabilitylistener Entitydamageevent"
Cohesion: 0.20
Nodes (6): EntityDamageEvent, UltimateAbilityListener, EntityDamageByEntityEvent, EventHandler, ForcePlugin, UUID

### Community 18 - "Versionadapter Fallingblock"
Cohesion: 0.18
Nodes (10): FallingBlock, ForceSide, ItemStack, Location, Particle, Player, PotionEffectType, String (+2 more)

### Community 19 - "Forceuser Forcebarmanager"
Cohesion: 0.18
Nodes (4): CooldownManager, LevelingManager, TelekinesisManager, Player

### Community 20 - "Darkaura Dark"
Cohesion: 0.22
Nodes (8): DarkAura, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 21 - "Forcechoke Dark"
Cohesion: 0.24
Nodes (8): ForceChoke, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 22 - "Forcelightning Dark"
Cohesion: 0.23
Nodes (8): ForceLightning, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 23 - "Forcerage Dark"
Cohesion: 0.24
Nodes (8): ForceRage, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 24 - "Forcescream Dark"
Cohesion: 0.24
Nodes (8): ForceScream, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 25 - "Soulrend Dark"
Cohesion: 0.22
Nodes (8): SoulRend, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 26 - "Unstoppablevengeance Dark"
Cohesion: 0.24
Nodes (8): UnstoppableVengeance, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 27 - "Databasemanager Forceuser"
Cohesion: 0.15
Nodes (6): DatabaseManager, ForcePlugin, ForceUser, String, UUID, ForceSide

### Community 28 - "Forcebarrier Light"
Cohesion: 0.24
Nodes (8): ForceBarrier, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 29 - "Forceclarity Light"
Cohesion: 0.22
Nodes (8): ForceClarity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 30 - "Forceheal Light"
Cohesion: 0.24
Nodes (8): ForceHeal, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 31 - "Forcejudgment Light"
Cohesion: 0.24
Nodes (8): ForceJudgment, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 32 - "Forcerepulse Light"
Cohesion: 0.24
Nodes (8): ForceRepulse, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 33 - "Forceserenity Light"
Cohesion: 0.24
Nodes (8): ForceSerenity, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 34 - "Forcestasis Light"
Cohesion: 0.24
Nodes (8): ForceStasis, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 35 - "Forcevalor Light"
Cohesion: 0.22
Nodes (8): ForceValor, AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String

### Community 36 - "Forcepull Universal"
Cohesion: 0.24
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForcePull

### Community 37 - "Forcesense Universal"
Cohesion: 0.23
Nodes (8): AbilityConfigManager, ForcePlugin, ForceSide, ForceUser, Override, Player, String, ForceSense

### Community 38 - "Forcetabcompleter Completeforce"
Cohesion: 0.38
Nodes (8): ForceTabCompleter, Nullable, Command, CommandSender, List, Override, String, TabCompleter

### Community 39 - "Markofthehunt Dark"
Cohesion: 0.26
Nodes (7): MarkOfTheHunt, AbilityConfigManager, ForceSide, ForceUser, Override, Player, String

### Community 40 - "Forceplaceholders Createprogressbar"
Cohesion: 0.26
Nodes (7): ForcePlaceholders, NotNull, PlaceholderExpansion, ForcePlugin, Override, Player, String

### Community 41 - "Abilitylistener Passivelistener"
Cohesion: 0.18
Nodes (10): Listener, AbilityListener, PassiveListener, EntityDamageByEntityEvent, EventHandler, ForcePlugin, PlayerInteractEvent, EntityDamageByEntityEvent (+2 more)

### Community 42 - "Databasemanager Disconnect"
Cohesion: 0.18
Nodes (7): ForcePlugin, GUIManager, JavaPlugin, DatabaseManager, Override, RankManager, VersionAdapter

### Community 43 - "Abilitymanager Collection"
Cohesion: 0.33
Nodes (7): Collection, AbilityManager, Ability, AbilityConfigManager, ForcePlugin, ForceSide, TelekinesisManager

### Community 44 - "Holocronmanager Abilitymanager"
Cohesion: 0.27
Nodes (6): HolocronManager, String, ForcePlugin, ForceSide, ItemStack, Player

### Community 45 - "Ability Execute"
Cohesion: 0.22
Nodes (5): Ability, ForceSide, ForceUser, Player, String

### Community 46 - "Forceadmincommand Handlecheck"
Cohesion: 0.44
Nodes (4): ForceAdminCommand, CommandSender, Override, String

### Community 47 - "Experiencelistener Onexpchange"
Cohesion: 0.27
Nodes (7): ExperienceListener, PlayerExpChangeEvent, PlayerLevelChangeEvent, EntityDeathEvent, EventHandler, ForcePlugin, PlayerJoinEvent

### Community 48 - "Abstractability Cfgint"
Cohesion: 0.27
Nodes (5): AbstractAbility, AbilityConfigManager, ForcePlugin, Override, String

### Community 49 - "Rankmanager Getspecializations"
Cohesion: 0.36
Nodes (5): RankManager, Rank, ForcePlugin, ForceSide, List

### Community 50 - "Versionutil Enchantment"
Cohesion: 0.31
Nodes (5): Enchantment, Particle, PotionEffectType, SuppressWarnings, VersionUtil

### Community 51 - "Passive Getdescription"
Cohesion: 0.43
Nodes (3): Passive, List, String

### Community 52 - "Ambienteffectsmanager Spawnambientparticle"
Cohesion: 0.39
Nodes (4): AmbientEffectsManager, ForcePlugin, ForceSide, Player

### Community 53 - "Cooldownmanager Getremainingcooldownformatted"
Cohesion: 0.57
Nodes (3): CooldownManager, Player, String

### Community 54 - "Guimanager Abilityconfigmanager"
Cohesion: 0.29
Nodes (6): AbilityConfigManager, AbilityManager, ForcePlugin, ForceUserManager, PassiveManager, RankManager

### Community 55 - "Actionbarutil Send"
Cohesion: 0.33
Nodes (4): ActionBarUtil, Player, String, SuppressWarnings

## Knowledge Gaps
- **42 isolated node(s):** `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `ForcePlugin`, `ForceUserManager`, `AbilityManager` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AbstractAbility` connect `Abstractability Cfgint` to `Dark Light`, `Forcevalor Light`, `Forcesense Universal`, `Forcecorrupt Dark`, `Forcemend Light`, `Darkaura Dark`, `Soulrend Dark`, `Forceclarity Light`?**
  _High betweenness centrality (0.091) - this node is a cross-community bridge._
- **Why does `Material` connect `Adapter Rank` to `Forceenchantmanager Forceenchantgui`, `Passivemanager Guimanager`, `Guilistener Guimanager`, `Holocronmanager Abilitymanager`, `Forcecommand Forceenchantcommand`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **Why does `ForcePlugin` connect `Databasemanager Disconnect` to `Forceenchantmanager Forceenchantgui`, `Passivemanager Guimanager`, `Guimanager Forceuser`, `Guilistener Guimanager`, `Holocronlistener Getforceusermanager`, `Forceuser Forcebarmanager`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **What connects `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `ForcePlugin` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Dark Light` be split into smaller, more focused modules?**
  _Cohesion score 0.05669710806697108 - nodes in this community are weakly interconnected._
- **Should `Adapter Rank` be split into smaller, more focused modules?**
  _Cohesion score 0.07013574660633484 - nodes in this community are weakly interconnected._
- **Should `Forceenchantmanager Forceenchantgui` be split into smaller, more focused modules?**
  _Cohesion score 0.09080841638981174 - nodes in this community are weakly interconnected._