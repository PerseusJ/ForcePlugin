# ForcePlugin — Long-Term Development Roadmap
> **Current Release:** v2.5 (SpigotMC)
> **Roadmap Scope:** Long-term. Each entry is its own versioned release.
> **Versioning Convention:** Minor bumps (2.x) for targeted fixes/additions, major bumps (3.0, 4.0) for sweeping overhauls.
> After a version is implemented in the project, please mark that version as [COMPLETED] to keep track which next version is to be continue.

---

## Version 2.6 — Dead Code Purge & Legacy Cleanup [COMPLETED]
**Type:** Patch / Refactor  
**Priority:** 🔴 Critical (do first)

The plugin currently carries significant dead code from a removed Holocron scroll-binding system and an unused specialization layer. This version cleans the slate.

**Changes:**
- **Remove `HolocronListener.java`** entirely. The `onSneak`/`onScroll`/`cycleBind` scroll-to-bind mechanic was replaced by the `BindGUI` + `AbilityPickerGUI` system. The file is now completely unused.
- **Remove specialization logic from `RankManager.java`** — delete the `specializations` map, all `loadSpecializations()` code, and the `getSpecializations()` method. Specializations are no longer a planned feature.
- **Remove specializations from `ranks.yml`** — delete the entire `specializations:` block (Guardian, Sentinel, Consular, Warrior, Inquisitor, Sorcerer entries).
- **Remove specialization references from `GUIListener.java`** — the `handleChooseSideGUI` currently only sets Light/Dark, but the `ForceEnchantManager.canUse()` still gates behind `forceUser.getSpecialization() != null`. Fix `canUse()` to instead check that the player has chosen a side (not NONE), removing the specialization dependency.
- **Remove `setSpecialization()` / `getSpecialization()` from `ForceUser.java`** — along with the corresponding database column read/write in `DatabaseManager.java`.
- **Remove `ForceAdminCommand.java` dead reset logic** — `handleReset` calls `forceUser.setSpecialization(null)` and `setNeedsToChoosePath(false)`. Remove both those lines and the corresponding data fields.
- **Audit all imports** — remove any import that referenced removed classes. Run a full build to confirm zero compilation errors.
- **Update `plugin.yml`**  — remove any command/permission entries that were related to the Holocron system if any were left over.

**Outcome:** A leaner, more maintainable codebase with no phantom systems consuming memory or confusing future contributors.

---

## Version 2.7 — Side Switching: Redemption & Fall [COMPLETED]
**Type:** Feature  
**Priority:** 🔴 High

Currently the side choice (Light/Dark) is **permanent**. This update introduces a controlled path for players to switch sides — resetting all abilities and XP while preserving their level.

**Changes:**
- **New GUI: Confirm Side Switch** — Opened via `/force choose` when a player already has a side. Shows a dramatic two-panel warning: current rank lost, all abilities and passives reset, Force level retained. Requires clicking a confirm button (not just clicking a side icon).
- **Add `/force choose` flow update** — instead of blocking the player with "You have already chosen your path", open the Confirm Side Switch GUI.
- **`ForceUser` side-switch logic** — clear all unlocked abilities, clear all unlocked passives, clear all slot binds, reset Force Points to 0, keep Force Level and XP intact, set new side.
- **GUIListener handler** — add `CONFIRM_SIDE_SWITCH_GUI_TITLE` title constant and handler in `GUIListener`. Green confirm and red cancel buttons.
- **Announce side switch in chat** — broadcast configurable message to server (can be disabled in `config.yml` via `side-switch.announce: true/false`).
- **Admin command extension** — `/fa setside` already exists; ensure it also clears abilities/passives when switching side (not just setting the field).
- **Config additions:**
  ```yaml
  side-switch:
    enabled: true
    announce: true
    announce-message: "&e{player} &7has switched from the &c{old_side} &7to the &b{new_side}&7!"
  ```

**Outcome:** Players can now truly change their path, adding replay value and roleplay flexibility.

---

## Version 2.8 — Database & Data Layer Hardening
**Type:** Bug Fix / Stability  
**Priority:** 🔴 High

The database layer (`DatabaseManager.java`) uses SQLite. Several edge cases can cause data corruption or silent failures.

**Changes:**
- **Async save on player quit** — move `savePlayerData()` in `PlayerConnectionListener` to a Bukkit async task to prevent server lag on logout.
- **Retry logic on connection failure** — if `databaseManager.connect()` fails at startup, retry up to 3 times before disabling the plugin.
- **Safe async load with callback** — `loadPlayerData()` should be asynchronous. Use `Bukkit.getScheduler().runTaskAsynchronously()` for the DB read, then re-sync to the main thread to register the `ForceUser`.
- **Null safety guards** — several places call `forceUser.getForceUser(player)` and trust the result without checking if the load is still pending. Add a "data loading" flag to `ForceUser` to prevent ability use during load.
- **Auto-save interval** — add a configurable periodic save task (default every 5 minutes) so data is never more than one interval stale:
  ```yaml
  database:
    auto-save-interval-minutes: 5
  ```
- **Graceful shutdown save** — ensure `onDisable()` waits for all pending async saves to complete before disconnecting.

**Outcome:** Zero data loss scenarios on server crashes, reboots, or lag spikes.

---

## Version 2.9 — ForceBar & Energy System Overhaul
**Type:** Enhancement  
**Priority:** 🟡 Medium-High

The Force energy bar (`ForceBarManager`) currently regenerates at a flat rate and lacks nuance.

**Changes:**
- **Out-of-combat regen bonus** — if a player has not used an ability or taken damage in the last N seconds (configurable), apply a multiplier to regen rate.
- **Low-energy visual warning** — when Force energy drops below 20%, send an action bar pulse warning ("⚡ Force Energy Critical!") in side-appropriate color.
- **Force energy drain on death** — optionally drain a configurable percentage of Force energy on death:
  ```yaml
  force-energy:
    drain-on-death-percent: 25
  ```
- **ForceBar display on login** — currently the bar only updates on ability use. Trigger `updateBar()` immediately on player join (after data loads).
- **Disable vanilla XP bar takeover option** — add a config toggle to use action bar for XP display instead of the XP bar, allowing servers that use XP for other plugins to remain compatible:
  ```yaml
  progression:
    use-exp-bar-for-xp: true
  ```

**Outcome:** The energy system feels alive and responsive, not just a static timer.

---

## Version 3.0 — The GUI Rework
**Type:** Major Feature  
**Priority:** 🔴 High

The GUIs are functional but visually sparse (black glass pane filler, barrier icons). This major version modernizes the entire UI layer.

**Changes:**
- **Themed ability icons** — replace `NETHER_STAR` (light) and `REDSTONE` (dark) generic icons with ability-specific materials. Each ability gets a unique item material configured in `config.yml` under its `icon:` key.
- **Multi-line descriptions** — expand ability lore in GUIs to show all stat values per level (damage, duration, radius, etc.) pulled from config rather than just energy cost and cooldown.
- **Force Points display always visible** — show remaining Force Points in every GUI screen, not just the main Abilities screen.
- **Paginated Ability GUI** — the current grid of 21 slots can overflow if more abilities are added. Add previous/next page buttons and a paginator.
- **Sound polish** — add distinct sound cues for: unlocking ability (different from upgrading), insufficient points (villager no), maxed ability, opening/closing menus.
- **Animated border** — use colored glass pane borders that match the player's current Force side (blue for Light, red for Dark).
- **Tooltip improvements** — the Upgrade GUI shows "Current Level" and "Next Level" but doesn't show what stat changes. Expand each level comparison panel to display all config-driven stats side by side.
- **Choose Side GUI makeover** — replace the plain White/Black Wool with `BEACON` (Light) and `CRYING_OBSIDIAN` (Dark), with a dramatic lore description per side before the player commits.

**Outcome:** The GUI feels like a polished Star Wars plugin, not a default Minecraft inventory hack.

---

## Version 3.1 — New Ability: Force Speed
**Type:** New Ability (Universal)  
**Priority:** 🟡 Medium

Force Speed is one of the most iconic Force powers — used by both Jedi and Sith.

**Implementation:**
- **Type:** Universal ability (available to both sides)
- **Trigger:** `SHIFT` (sneak key, same as Force Rage / Force Deflection pattern)
- **Effect:** Grants `SPEED` potion effect at configurable amplifier + brief invulnerability frames (using `NoDamageTicks`). Leaves a trail of particle effects (side-colored — blue for Light, red for Dark).
- **Levels:**
  - Level 1: Short burst, low speed amplifier
  - Level 2: Longer duration, higher speed
  - Level 3: Max speed, leaves afterimage particles
- **Conflict:** Cannot activate while another SHIFT-trigger ability is active on the same slot.
- **Config:**
  ```yaml
  FORCE_SPEED:
    trigger: SHIFT
    unlock-cost: 2
    max-level: 3
    levels:
      '1': { energy-cost: 20.0, cooldown: 15.0, duration-seconds: 3, speed-amplifier: 2, upgrade-cost: 2 }
      '2': { energy-cost: 18.0, cooldown: 12.0, duration-seconds: 5, speed-amplifier: 3, upgrade-cost: 3 }
      '3': { energy-cost: 15.0, cooldown: 10.0, duration-seconds: 7, speed-amplifier: 4 }
  ```
- **Files:** New `ForceSpeed.java` in `abilities/universal/`.

---

## Version 3.2 — New Ability: Force Vision
**Type:** New Ability (Universal)  
**Priority:** 🟡 Medium

Force Vision (or "Precognition") allows a Force user to sense the location of nearby entities.

**Implementation:**
- **Type:** Universal ability (available to both sides)
- **Trigger:** `RIGHT_CLICK`
- **Effect:** All living entities within radius become visible through walls using `GLOWING` potion effect applied to them for the duration. Also applies `NIGHT_VISION` to the caster. If the target is a player, their name appears with their health level in the action bar (configurable).
- **Levels:**
  - Level 1: Small radius, short duration
  - Level 2: Larger radius, longer duration
  - Level 3: Maximum radius + reveals health of all detected players
- **Config:**
  ```yaml
  FORCE_VISION:
    trigger: RIGHT_CLICK
    unlock-cost: 2
    max-level: 3
    levels:
      '1': { energy-cost: 20.0, cooldown: 20.0, duration-seconds: 5, radius: 15, upgrade-cost: 2 }
      '2': { energy-cost: 17.0, cooldown: 16.0, duration-seconds: 8, radius: 25, upgrade-cost: 3 }
      '3': { energy-cost: 15.0, cooldown: 12.0, duration-seconds: 12, radius: 40 }
  ```
- **Files:** New `ForceVision.java` in `abilities/universal/`.

---

## Version 3.3 — New Ability: Force Shatter (Dark Side)
**Type:** New Ability (Dark)  
**Priority:** 🟡 Medium

A dark-side ability that psychically assaults the target's armor, temporarily reducing their protection.

**Implementation:**
- **Type:** Dark side exclusive
- **Trigger:** `LEFT_CLICK`
- **Effect:** Targets the player/entity the caster is looking at (raycast, same pattern as `ForceChoke`). Applies `WEAKNESS` potion effect AND temporarily stores and removes one level of `PROTECTION` enchantment from the target's armor for the duration, restoring it after. Visual: dark particle burst on target's armor slots.
- **Levels:**
  - Level 1: Brief shatter, one armor piece affected
  - Level 2: Longer duration, two armor pieces affected
  - Level 3: Full armor shatter, all protection removed temporarily
- **Config:** Standard Dark ability config block.
- **Files:** New `ForceShatter.java` in `abilities/dark/`.

---

## Version 3.4 — New Ability: Battle Meditation (Light Side)
**Type:** New Ability (Light)  
**Priority:** 🟡 Medium

An iconic Jedi ability — channeling the Force to inspire and empower nearby allies.

**Implementation:**
- **Type:** Light side exclusive
- **Trigger:** `RIGHT_CLICK`
- **Effect:** Area-of-effect buff centered on the caster. Nearby players (who are also Force users) receive `REGENERATION`, `RESISTANCE`, and `HASTE` for the duration. Non-Force users receive just `REGENERATION`. Caster is slowed (`SLOWNESS I`) during the channel — this is a support ability, not a combat ability.
- **Levels:**
  - Level 1: Small radius, short duration, minor buffs
  - Level 2: Larger radius, longer duration
  - Level 3: Maximum radius + buff amplifiers increase
- **Config:**
  ```yaml
  BATTLE_MEDITATION:
    trigger: RIGHT_CLICK
    unlock-cost: 3
    max-level: 3
    ...
  ```
- **Files:** New `BattleMeditation.java` in `abilities/light/`.

---

## Version 3.5 — New Ability: Dark Transfer (Dark Side)
**Type:** New Ability (Dark)  
**Priority:** 🟡 Medium

A corrupted form of healing — the Sith do not heal themselves, they steal life.

**Implementation:**
- **Type:** Dark side exclusive
- **Trigger:** `RIGHT_CLICK`
- **Effect:** Targeted ability (raycast). Drains health from the target entity over time (similar to `ForceDrain`) but the stolen health is transferred directly to the caster as healing — not just discarded. If the target dies during the effect, the caster gets a burst of health.
- **Distinction from ForceDrain:** ForceDrain costs energy and drains; Dark Transfer is a sustain mechanic during fights — it costs less but requires the target to be in range.
- **Levels:** Level 1-3 scaling drain/heal rate and duration.
- **Files:** New `DarkTransfer.java` in `abilities/dark/`.

---

## Version 3.6 — New Ability: Force Tempest (Universal Ultimate)
**Type:** New Ultimate Ability (Universal)  
**Priority:** 🟡 Medium

A devastating area-clearing ability — a Force-generated windstorm.

**Implementation:**
- **Type:** Universal ultimate (unlocks at a new level milestone, e.g. Level 50 for both sides)
- **Trigger:** Right-click / special key (bound to slot like all ultimates)
- **Effect:** Creates a 10-block radius AoE around the caster. All entities in range are launched into the air (upward velocity vector) and take fall damage. Ground-level blocks are not broken — purely a physics effect. Caster is immune. Duration: 3 seconds of sustained wind effect pulling entities toward the center then blasting them out.
- **Visual:** Mass of `END_ROD` + `CLOUD` particles swirling around the caster.
- **Config block** under `ultimate-abilities:`.
- **LevelingManager update** — add Level 50 ultimate unlock milestone for `FORCE_TEMPEST` for both sides.
- **Files:** New `ForceTempest.java` in `abilities/universal/`.

---

## Version 3.7 — New Ability: Force Phantom (Light Side)
**Type:** New Ability (Light)  
**Priority:** 🟡 Medium

A Jedi ability to project a decoy illusion of themselves, confusing enemies.

**Implementation:**
- **Type:** Light side exclusive
- **Trigger:** `RIGHT_CLICK`
- **Effect:** Spawns an `ARMOR_STAND` (invisible, named with the player's name, wearing the player's current armor skin using NMS head trick or skull meta) at the player's location. The phantom moves in a preset pattern (wanders randomly in a 5-block radius). Enemies targeting the phantom deal 0 damage to the caster. Phantom despawns after duration or when hit enough times. The caster becomes slightly transparent (`INVISIBILITY` for 1 second) as the phantom appears.
- **Levels:** Level 1 — 1 phantom, short life; Level 2 — 2 phantoms; Level 3 — 3 phantoms.
- **Files:** New `ForcePhantom.java` in `abilities/light/`.

---

## Version 3.8 — New Ability: Death Field (Dark Side Ultimate)
**Type:** New Ultimate Ability (Dark)  
**Priority:** 🟡 Medium

The most feared Sith power — a mass drain of life from everything nearby.

**Implementation:**
- **Type:** Dark side ultimate (unlocks at Level 50 for Dark)
- **Effect:** All living entities within a large radius (configurable, default 12 blocks) simultaneously take damage equal to a percentage of their current health. That aggregate damage is converted to healing for the caster (capped at max health). Visual: black void particles erupting from the ground, all targets briefly glow dark red.
- **Limitations:** Does not affect other dark-side Force users (configurable toggle). Long cooldown (120+ seconds).
- **Config block** under `ultimate-abilities:`.
- **LevelingManager:** Hook into Level 50 for DARK side alongside `FORCE_TEMPEST`.
- **Files:** New `DeathField.java` in `abilities/dark/`.

---

## Version 3.9 — New Ability: Force Bond (Universal)
**Type:** New Ability (Universal)  
**Priority:** 🟢 Lower priority

A deeply lore-accurate ability — a Force Bond links two Force users.

**Implementation:**
- **Type:** Universal (works for both sides but differently)
  - **Light Side variant:** Shared healing — if either bonded player heals, both benefit from a percentage.
  - **Dark Side variant:** Shared pain — if either bonded player takes damage, both take it (but both deal bonus damage for the duration).
- **Trigger:** `RIGHT_CLICK` on a target player within range
- **Mechanic:** Target player sees a confirmation request in chat/action bar. If they accept (sneak within 3 seconds), the bond activates. If they're on the opposite side, a special mixed-effect applies.
- **Duration:** 30-60 seconds, configurable.
- **Files:** New `ForceBond.java` in `abilities/universal/`, new `BondManager.java` in `managers/` to track active bond pairs.

---

## Version 4.0 — The Progression Overhaul
**Type:** Major Feature  
**Priority:** 🔴 High

The progression system (XP → Level → Points) works but is mechanically shallow. This major update adds depth to the progression loop.

**Changes:**
- **Force Mastery system** — after reaching max level (100), players enter "Force Mastery" mode. No more XP gain or leveling, but instead earn `Mastery Points` from special challenges. Mastery Points unlock cosmetic titles, ability skins, and particle trail colors.
- **Prestige reset (optional, admin-configurable)** — admins can enable a "prestige" mode where players at Level 100 can voluntarily reset to Level 1 in exchange for a permanent passive bonus (e.g., +5% energy regen per prestige, max 5 prestiges).
- **Tiered XP sources** — expand beyond the 3 current XP gain sources:
  - `per-ability-hit` (new): XP when ability actually hits a target (not just fires)
  - `per-player-kill` (new): Bonus XP for Force-finishing a player
  - `per-ultimate-use` (new): Extra XP for ultimate abilities
  - `per-passive-trigger` (new): Small XP when a passive procs
- **XP multiplier events** — admin command `/fa xpmultiplier <multiplier> <duration>` for timed XP boost events.
- **Level milestone rewards** — configurable rewards at set levels (beyond just ultimates at 33/66/100):
  ```yaml
  milestones:
    10:
      give-points: 3
      message: "&bYou've grown stronger in the Force!"
    25:
      give-points: 5
    50:
      give-points: 10
      unlock-ultimate: FORCE_TEMPEST
  ```
- **Config restructure** for all new settings under `progression:`.

---

## Version 4.1 — Passive System Expansion
**Type:** Enhancement + New Content  
**Priority:** 🟡 Medium

The passive system currently has 9 passives (4 Light, 4 Dark, 1 Neutral). This update doubles it.

**New Light Passives:**
- **Jedi Resolve** — Upon taking lethal damage, once per cooldown, survive with 1 HP and gain brief invulnerability.
- **Force Clarity Enhancement** — When you use a RIGHT_CLICK ability, your next ability fires 15% cheaper energy.
- **Echoes of Light** — Healing abilities also restore a small amount of Force energy.

**New Dark Passives:**
- **Ruthless Efficiency** — Every kill extends active dark-side ability durations by 1 second.
- **Dark Resilience** — Absorb a percentage of incoming ability damage as Force energy.
- **Hunger of the Dark Side** — The lower your health, the stronger your dark-side abilities become (scaling damage buff at low health).

**New Neutral Passives:**
- **Force Equilibrium** — Reduces all ability cooldowns by a flat percentage.
- **Resilient Spirit** — Reduces Force energy lost when taking damage by 15%.

**Changes:**
- Add new passives to `passives.yml`.
- Implement new passive logic in `PassiveListener.java`.
- Expand the Passives GUI to support pagination (currently limited to 7 slots).

---

## Version 4.2 — HUD & Visual Polish
**Type:** Enhancement  
**Priority:** 🟡 Medium

The sidebar HUD shows slot binds. This update makes it far more useful.

**Changes:**
- **Cooldown display in HUD** — show remaining cooldown next to each bound ability name in the sidebar (e.g. `> 1: Force Push [3.2s]`).
- **Force energy in HUD** — add a visual Force energy bar at the top of the sidebar using block characters (█████░░░░░) in side-appropriate color.
- **Current rank display** — show the player's current rank title at the top of the HUD scoreboard.
- **Force Level display** — show current level next to rank in HUD.
- **Config options:**
  ```yaml
  hud:
    enabled: true
    show-cooldowns: true
    show-energy-bar: true
    show-rank: true
    show-level: true
  ```
- **BossBar option** — alternative to the sidebar, allow admins to configure Force energy to display as a BossBar instead (configurable per player preference via `/force hud bossbar|sidebar`).

---

## Version 4.3 — Admin Tools Expansion
**Type:** Enhancement  
**Priority:** 🟡 Medium

The `/fa` admin command is solid but needs more tools.

**New Subcommands:**
- `/fa unlock <player> <ability>` — unlock a specific ability for a player without spending their points.
- `/fa upgrade <player> <ability>` — upgrade a specific ability level for a player.
- `/fa unlockpassive <player> <passive>` — unlock a passive.
- `/fa info <player>` — expanded version of `/fa check` with full passive list, all slot binds, and mastery status.
- `/fa energy <player> <amount>` — set or give Force energy directly.
- `/fa setxp <player> <amount>` — set XP directly (companion to givexp).
- `/fa reload <section>` — partial reload (e.g. `/fa reload abilities` or `/fa reload passives`) instead of full plugin reload.
- `/fa broadcast` — trigger the side-switch announcement manually.

**ForceStatsCommand Enhancement:**
- `/forcestats` currently shows limited info. Expand it to show: level, rank, side, XP progress bar, all unlocked abilities with levels, all unlocked passives with levels, slot binds.

---

## Version 4.4 — Telekinesis Rework
**Type:** Enhancement  
**Priority:** 🟡 Medium

`TelekinesisManager.java` is one of the most complex managers (4774 bytes) but the ability itself can be deepened.

**Changes:**
- **Multi-entity pick-up** — at higher levels, lift multiple entities simultaneously (up to 3 at Level 3).
- **Directional throw** — instead of only launching forward, allow the caster to look at a different direction after picking up to change launch direction.
- **Environmental use** — allow lifting non-entity blocks (falling sand/gravel) and flinging them as projectiles (cosmetic, no block destruction).
- **Energy scaling with weight** — heavier entities (named, boss mobs) cost more energy per second to hold.
- **Visual improvements** — particles orbit the held entity while lifted (currently none?). Use `SOUL_FIRE_FLAME` for dark users, `END_ROD` for light users.
- **Config expansion:**
  ```yaml
  TELEKINESIS:
    trigger: RIGHT_CLICK
    max-level: 3
    levels:
      '1': { initial-energy-cost: 10.0, energy-cost-per-second: 5.0, cooldown: 20.0, range: 20, launch-strength: 3.0, max-entities: 1, upgrade-cost: 3 }
      '2': { initial-energy-cost: 9.0, energy-cost-per-second: 4.5, cooldown: 18.0, range: 25, launch-strength: 3.5, max-entities: 2, upgrade-cost: 4 }
      '3': { initial-energy-cost: 8.0, energy-cost-per-second: 4.0, cooldown: 15.0, range: 30, launch-strength: 4.0, max-entities: 3 }
  ```

---

## Version 4.5 — Force Enchanting QoL Update
**Type:** Enhancement  
**Priority:** 🟡 Medium

The Force Enchanting system (ForceEnchantManager + ForceEnchantGUI) is well-built but has usability gaps.

**Changes:**
- **Preview mode** — before confirming, show exact item preview with proposed enchantments applied (using a separate display slot in the GUI).
- **Remove enchantment option** — allow players to remove Force-enchanted enchantments (at a refund of 50% energy cost, configurable).
- **Category tabs** — the enchantment GUI currently lists everything at once. Add category tabs: Armor / Weapons / Tools / Bows / Tridents.
- **Enchantment conflict detection** — warn the player in the GUI if they try to select conflicting enchantments (e.g. Silk Touch + Fortune) before they spend energy.
- **Right-click to downgrade** — currently clicking cycles up. Add right-click to cycle back down.
- **Enchant history** — store the last 3 enchantment sessions per player in memory (lost on server restart) so players can redo common enchantments quickly.
- **Gate change** — currently gated behind having a specialization (`forceUser.getSpecialization() != null`). After v2.6 removes specializations, re-gate it behind having reached Level 25:
  ```yaml
  force-enchanting:
    min-level-required: 25
  ```

---

## Version 4.6 — Cooldown Manager Overhaul
**Type:** Enhancement  
**Priority:** 🟡 Medium

`CooldownManager.java` is a simple HashMap-based timer. This update makes it robust and feature-rich.

**Changes:**
- **Persistent cooldowns across restarts** — store active cooldowns in the database so players can't bypass cooldowns by logging out and back in.
- **Global cooldown system** — add a short global cooldown (configurable, default 0.5s) after any ability activation to prevent rapid-fire multi-slot spam.
- **Cooldown reduction support** — add a `reduceCooldown(player, abilityId, seconds)` method so future passives and abilities can interact with cooldowns.
- **Admin cooldown clear** — new admin subcommand `/fa clearcooldowns <player>` to reset all active cooldowns.
- **Cooldown events** — fire a custom Bukkit event `ForceCooldownStartEvent` and `ForceCooldownEndEvent` for API extensibility.

---

## Version 4.7 — PlaceholderAPI Expansion
**Type:** Enhancement  
**Priority:** 🟢 Lower priority

`ForcePlaceholders.java` exists and is registered with PlaceholderAPI. Expand it significantly.

**New Placeholders:**
- `%force_side%` — LIGHT, DARK, or NONE
- `%force_level%` — current Force level (integer)
- `%force_xp%` — current XP (decimal)
- `%force_xp_needed%` — XP needed for next level
- `%force_xp_percent%` — XP progress as percentage (0-100)
- `%force_points%` — available Force Points
- `%force_rank%` — current rank display name (color coded)
- `%force_energy%` — current Force energy (decimal)
- `%force_energy_max%` — max Force energy (always 100.0)
- `%force_energy_percent%` — energy as percentage
- `%force_ability_<slot>%` — ability name bound to slot 1-9
- `%force_cooldown_<ability_id>%` — remaining cooldown for a specific ability
- `%force_passive_<passive_id>%` — level of a specific passive (or "Locked")
- `%force_is_ultimate_unlocked_<id>%` — true/false for ultimate unlock status

---

## Version 4.8 — Sound & Particle System Refactor
**Type:** Enhancement  
**Priority:** 🟡 Medium

Currently sounds and particles are hardcoded inside each ability class. This creates massive duplication and makes customization impossible without editing Java.

**Changes:**
- **New `SoundManager` utility** — centralized sound definitions. Map ability IDs to sound profiles in `config.yml`:
  ```yaml
  sounds:
    FORCE_PUSH:
      cast: ENTITY_IRON_GOLEM_HURT
      hit: ENTITY_PLAYER_HURT
    FORCE_LIGHTNING:
      cast: ENTITY_LIGHTNING_BOLT_THUNDER
      hit: ENTITY_GENERIC_HURT
  ```
- **New `ParticleManager` utility** — centralize particle dispatch. Config-driven particle types per ability:
  ```yaml
  particles:
    FORCE_PUSH:
      type: EXPLOSION_NORMAL
      count: 10
      spread: 0.3
    FORCE_LIGHTNING:
      type: END_ROD
      count: 20
  ```
- **Per-side particle color overrides** — Light side uses cool-tone particles (white, blue, aqua), Dark side uses warm-tone (red, dark red, purple). Config-driven.
- **Migrate existing hardcoded sounds/particles** to use the new managers.

---

## Version 4.9 — Ability API (Developer Extension Layer)
**Type:** Developer Feature  
**Priority:** 🟢 Lower priority

Other plugin developers should be able to add their own Force abilities without forking ForcePlugin.

**Changes:**
- **Public API package** — create `org.perseus.forcePlugin.api` with:
  - `ForceAPI.java` — static access to all managers
  - `ForceAbilityRegistrar.java` — interface to register external abilities at runtime
  - `ForceUserAccessor.java` — safe read-only view of a ForceUser
- **Custom events** — publish `ForceAbilityUseEvent`, `ForceSideChooseEvent`, `ForceLevelUpEvent` as Bukkit events that external plugins can listen to and cancel.
- **API Maven artifact** — document how to depend on ForcePlugin as a soft/hard dependency in `plugin.yml`.
- **JavaDoc** — document all public API methods.

---

## Version 5.0 — The Force Mastery Endgame Update
**Type:** Major Feature  
**Priority:** 🟡 Medium (depends on v4.0 Progression Overhaul)

After completing the base progression (Level 100), players enter Force Mastery — a prestige-like endgame loop.

**Changes:**
- **Mastery Points system** — after Level 100, earning any XP converts to Mastery Points at a configurable ratio.
- **Mastery Skill Tree** — a new GUI (Force Mastery Tree) with exclusively cosmetic and quality-of-life unlocks:
  - Custom particle trail colors (5 options per side)
  - Custom ability activation sounds (from a curated list)
  - Ability name display customization (prefix/suffix your ability name in HUD)
  - "Force Echo" passive — a 5% chance to activate an ability at no energy cost
  - Mastery Titles (prefixes for chat/tab, requires permissions integration or EssentialsX hook)
- **Mastery Leaderboard** — `/force leaderboard` command showing top players by Mastery Points (uses DB query).
- **Admin Mastery commands** — `/fa setmastery <player> <points>`, `/fa resetmastery <player>`.

---

## Version 5.1 — New Ability: Force Immunity (Light Ultimate)
**Type:** New Ultimate Ability  
**Priority:** 🟡 Medium

The Light Side Level 50 ultimate slot. A brief but powerful defensive shield.

**Implementation:**
- **Type:** Light side ultimate (Level 50 milestone)
- **Effect:** For 3 seconds, the caster becomes completely immune to all damage, status effects, knockback, and ability effects. During this window, any ability that would have hit them is reflected back at the attacker at 50% damage. After the immunity ends, the caster is briefly slowed (Slowness II, 2 seconds) as the effort of sustaining the shield exhausts them.
- **Visual:** White shockwave pulse on activation, golden particles orbiting the player during the window.
- **Config block** under `ultimate-abilities:`.

---

## Version 5.2 — New Ability: Force Phantom Storm (Dark Ultimate)
**Type:** New Ultimate Ability  
**Priority:** 🟡 Medium

The Dark Side Level 75 ultimate. A massive, terrifying display of Sith power.

**Implementation:**
- **Type:** Dark side ultimate (Level 75 milestone)
- **Effect:** Summons 5 waves of Force Lightning that arc between the caster and all enemies within 20 blocks over 5 seconds. Each wave deals increasing damage (starting low, ramping up). All targets caught in the storm are slowed and cannot jump.
- **Visual:** Overhead dark storm cloud particle effect, lightning arcs represented by `END_ROD` + `CRIT_MAGIC` particles.
- **Config block** under `ultimate-abilities:`.
- **LevelingManager:** Add Level 75 ultimate unlock milestone for DARK.

---

## Version 5.3 — Force PvP: Dueling System
**Type:** New Feature (Multiplayer)  
**Priority:** 🟢 Lower priority (Late stage)

A structured Force PvP duel system for competitive gameplay.

**Changes:**
- **`/force duel <player>`** — challenge another Force user to a duel.
- **Duel acceptance flow** — challenged player receives a chat prompt with [Accept]/[Decline] clickable text.
- **Duel arena logic** — duels take place within a configurable radius of the challenger. Players outside the radius cannot interfere.
- **Duel rules:**
  - No item use (potions, food) during duel
  - No death — when one player reaches 2 HP, the duel ends
  - Loser is briefly frozen (Slowness + Slowness V for 2 seconds) as the winner is announced
- **Duel statistics** — track wins/losses per player in the database. Display via `/forcestats`.
- **Duel leaderboard** — `/force duel leaderboard` shows top duelists by win rate.
- **Config:**
  ```yaml
  dueling:
    enabled: true
    duel-radius-blocks: 30
    allow-items: false
    end-at-hp: 2.0
  ```

---

## Version 5.4 — Force Lore: Codex System
**Type:** New Feature  
**Priority:** 🟢 Lower priority

A lightweight in-game lore system for servers that want Star Wars immersion without a full quest system.

**Changes:**
- **`/force codex`** — opens a book-and-quill style GUI (using `WRITTEN_BOOK` item meta) with Force lore entries.
- **Codex entries** unlock as the player progresses:
  - Level 1: "Origins of the Force" (both sides)
  - Level 10: Side-specific lore (Jedi Code / Sith Code)
  - Level 25: Ability-specific lore entries (one per unlocked ability)
  - Level 50: Ultimate ability backstories
  - Level 100: Final lore entry (Force Transcendence / Sith Emperor legend)
- **Book format** — multi-page `BookMeta` using `ChatColor` formatting.
- **New data field** — `unlockedCodexEntries` list in `ForceUser`, saved to DB.
- **Config-driven lore** — all codex text lives in a new `codex.yml` resource file, fully customizable by server admins.

---

## Version 5.5 — Multiplayer: Force Alliance & Rivalry System
**Type:** New Feature (Multiplayer — Late Stage)  
**Priority:** 🟢 Lower priority

A social layer for Force users on shared servers.

**Changes:**
- **Alliance system** — Light-side players can form an "Order" (group of up to 5 players). Dark-side players form a "Cabal". Members see each other on radar via Force Sense.
- **Group buffs** — being near your Alliance members provides a small passive bonus (Light: +10% healing received; Dark: +10% ability damage).
- **Rivalry mechanic** — when two Alliances PvP each other, both sides gain a temporary Force Frenzy buff (all cooldowns reduced by 20%).
- **`/force alliance create <name>`**, `/force alliance invite <player>`, `/force alliance leave`.
- **DB storage** — Alliance data stored in a new table.
- **Config:**
  ```yaml
  alliances:
    enabled: true
    max-size: 5
    group-buff-radius: 20
  ```

---

## Version 5.6 — Config Validation & Startup Diagnostics
**Type:** Quality of Life / Developer  
**Priority:** 🟡 Medium

Server admins frequently misconfigure the plugin and get cryptic errors. This update adds a robust startup validation layer.

**Changes:**
- **Config validator** — on startup/reload, validate every ability config block against the expected schema. If an ability is missing a required key (e.g. `cooldown` for a level), log a clear warning with the exact YAML path.
- **Missing ability handler** — if an ability defined in `plugin.yml` or player data references an ability ID that no longer exists in config, log a warning and skip gracefully instead of throwing a NPE.
- **Version mismatch detection** — compare the `version:` key in `config.yml` to the plugin version. If they differ, log a migration warning telling the admin to back up their config before reloading.
- **Startup health report** — log a structured summary at startup:
  ```
  [ForcePlugin] === Startup Diagnostics ===
  [ForcePlugin] Abilities loaded: 30/30
  [ForcePlugin] Passives loaded: 17/17
  [ForcePlugin] DB connection: OK
  [ForcePlugin] PlaceholderAPI: Hooked
  [ForcePlugin] Version: 5.6 | MC: 1.21.x
  [ForcePlugin] All systems nominal.
  ```

---

## Version 5.7 — Unit Testing & CI Pipeline
**Type:** Developer / Infrastructure  
**Priority:** 🟢 Lower priority

Introduce automated testing to prevent regressions as the plugin grows.

**Changes:**
- **JUnit 5 test suite** — add `src/test/java` with tests for:
  - `LevelingManager.getXpForNextLevel()` — verify XP curve math
  - `CooldownManager` — verify cooldown start/expiry logic
  - `ForceUser` — test ability unlock/upgrade/passive state transitions
  - `AbilityConfigManager` — verify config read fallbacks
- **MockBukkit integration** — use MockBukkit library (added to `build.gradle`) to simulate Bukkit server environment in tests.
- **GitHub Actions workflow** — add `.github/workflows/build.yml` to run Gradle build + tests on every push and PR.
- **Code coverage reporting** — add JaCoCo plugin to `build.gradle`, generate HTML coverage reports.

---

## Version 5.8 — Performance & Memory Optimization
**Type:** Optimization  
**Priority:** 🟡 Medium

As the plugin grows (more passives, more abilities, more players), performance becomes critical.

**Changes:**
- **Ability execution profiling** — add optional timing logs (enabled via debug config flag) to measure how long each ability's `execute()` takes.
- **Entity lookup caching** — abilities that do entity raycasting (ForceChoke, ForceSense, etc.) each do their own `world.getNearbyEntities()`. Introduce a per-tick entity cache in a new `EntityCacheManager` that all abilities share.
- **Scoreboard batching** — `HudManager.updateScoreboard()` is called frequently. Add a debounce so it doesn't fire more than once per tick per player.
- **Memory leak audit** — ensure all BukkitRunnable tasks are properly cancelled on ability end and on player quit. Ensure maps in `CooldownManager`, `TelekinesisManager`, `ForceEnchantManager` clean up player data on quit.
- **ForceBarManager tick rate** — make the regen tick rate configurable:
  ```yaml
  force-energy:
    regen-tick-rate-ticks: 20
  ```

---

## Version 5.9 — Multilingual & Message System Overhaul
**Type:** Enhancement  
**Priority:** 🟢 Lower priority

Every player-facing message is currently hardcoded in Java. This makes localization and customization impossible.

**Changes:**
- **New `messages.yml`** — extract all player-facing strings into a resource file:
  ```yaml
  messages:
    no-permission: "&cYou do not have permission."
    ability-on-cooldown: "&c{ability} is on cooldown: {time}"
    not-enough-energy: "&bNot enough Force Energy!"
    level-up-title: "&bForce Level Up!"
    level-up-subtitle: "&eYou are now Level {level}"
    ...
  ```
- **`MessageManager` utility** — reads from `messages.yml`, supports `{placeholder}` token substitution.
- **Replace all hardcoded strings** in all command, listener, and manager classes with `MessageManager.get("key", placeholders)` calls.
- **Language packs** — document how to create a translation by copying `messages.yml` and changing the values. Provide an example Spanish translation.

---

## Version 6.0 — The World Integration Update
**Type:** Major Feature  
**Priority:** 🟢 Lower priority (Far future)

Force powers interacting with the Minecraft world in meaningful ways.

**Changes:**
- **Force-affected blocks** — Force Push/Pull/Repulse can optionally displace specific block types (gravel, sand, torches, items on ground). Configurable whitelist of breakable/movable block types.
- **Force Ritual Sites** — configurable world locations (set via admin command) where Force energy regenerates 2x faster. Displayed on `/force sense`.
- **Force-sensitive NPCs** (Citizens integration, optional) — if Citizens plugin is present, ForcePlugin can tag NPCs as "Force Sensitive" and allow abilities to interact with them (they react to Force Push, can be choked, etc.).
- **Force Shrines** — placeable by admins (`/fa placeshrine <type>`), cylindrical particle effects that grant nearby players a timed buff (Light: healing aura; Dark: damage boost).
- **Config-driven world features:**
  ```yaml
  world-integration:
    enable-block-interaction: false
    force-shrine-radius: 5
    enable-citizens-npc-support: false
  ```

---

## Version 6.1 — Economy Integration
**Type:** Feature (Server Integration)  
**Priority:** 🟢 Lower priority

For servers using Vault + economy plugins, allow Force Points to optionally be purchased or earned via economy.

**Changes:**
- **Vault hook** — soft-depend on Vault in `plugin.yml`. If present, allow Force Points to be purchased:
  ```yaml
  economy:
    enabled: false
    cost-per-force-point: 1000
  ```
- **`/force buy points <amount>`** — player spends server currency to buy Force Points.
- **Force energy as economic resource** — optionally allow energy to be sold for a small currency reward (opt-in server feature).
- **Shop integration** — support for AdminShop / ShopGUIPlus integration via PlaceholderAPI placeholders for Force economy.

---

## Version 6.2 — New Ability: Force Shockwave (Universal)
**Type:** New Ability (Universal)  
**Priority:** 🟡 Medium

A ground-pound ability — slam the Force into the earth.

**Implementation:**
- **Type:** Universal
- **Trigger:** `SHIFT` (activated while in the air — mid-jump or from a fall)
- **Effect:** When the player lands while this ability is queued, an AoE explosion of Force energy radiates outward from landing point. All entities in radius are knocked back and take damage scaled to fall height (the higher the fall, the more powerful). Caster takes no fall damage during the activation window.
- **Levels:** 3 levels scaling radius and damage multiplier.
- **Files:** New `ForceShockwave.java` in `abilities/universal/`.

---

## Version 6.3 — New Ability: Force Wound (Dark Side)
**Type:** New Ability (Dark)  
**Priority:** 🟡 Medium

A long-range dark side attack that tears at the target from a distance.

**Implementation:**
- **Type:** Dark side exclusive
- **Trigger:** `LEFT_CLICK`
- **Effect:** Targeted ability (long-range, up to 40 blocks). Applies an invisible "wound" debuff to the target for 8 seconds. While wounded: every time the target uses an ability, they take bonus damage equal to their ability's energy cost converted to health damage. A subtle dark particle effect marks the wounded target.
- **Levels:** 3 levels scaling wound duration and damage conversion ratio.
- **Files:** New `ForceWound.java` in `abilities/dark/`.

---

## Version 6.4 — New Ability: Force Sanctuary (Light Side)
**Type:** New Ability (Light)  
**Priority:** 🟡 Medium

A Light Side protective field — create a sanctuary where dark powers weaken.

**Implementation:**
- **Type:** Light side exclusive
- **Trigger:** `RIGHT_CLICK`
- **Effect:** Creates a 6-block radius sanctuary zone at the caster's feet (not following them — stationary). Duration 15-30 seconds (scaling). Inside the zone:
  - Dark side ability damage is reduced by 30% (configurable)
  - Light side users inside regenerate Force energy faster
  - Visual: soft golden particle dome
- **Uses a BukkitRunnable** that tracks who is inside the zone and applies/removes buffs each tick.
- **Config:** Standard ability config + `damage-reduction-percent` key.
- **Files:** New `ForceSanctuary.java` in `abilities/light/`.

---

## Version 6.5 — New Ability: Force Maelstrom (Dark Ultimate)
**Type:** New Ultimate Ability  
**Priority:** 🟡 Medium (depends on v5.2 ultimate framework)

The ultimate dark side environmental attack.

**Implementation:**
- **Type:** Dark side ultimate (Level 100 slot replacement — this would extend the ultimate tiers to have 4 per side, requiring LevelingManager update)
- **Effect:** Creates a 15-block-radius sphere of dark Force energy centered on the caster. For 6 seconds, all entities inside are repeatedly knocked toward the caster (gravity inversion), bombarded with mini lightning strikes (visual only at lower damage), and slowed. At the end of the 6 seconds, an enormous Force implosion launches everything outward with massive velocity.
- **Visual:** Swirling vortex of black and purple particles for the duration, followed by a screen-shaking explosion particle burst.
- **Config block** under `ultimate-abilities:`.

---

## Version 6.6 — New Ability: Sever Force (Light Ultimate)
**Type:** New Ultimate Ability  
**Priority:** 🟡 Medium

The Light Side's most devastating offensive ultimate — severing a Sith's connection to the Force.

**Implementation:**
- **Type:** Light side ultimate (extended tier at Level 100, alongside `FORCE_SERENITY`)
- **Effect:** Targeted at a single player. If hit, the target's Force energy is reduced to 0 and they cannot regenerate Force energy for 10 seconds (configurable). All active dark-side ability effects on the target are cancelled. The target's HUD displays "Force Severed" in white for the duration.
- **Counterplay:** Target can break the effect early by using a SHIFT-triggered ability (costs double energy if Severed).
- **Visual:** Brilliant white beam from caster to target, white explosion of particles at the target.

---

## Version 6.7 — Config Migration System
**Type:** Developer / Admin QoL  
**Priority:** 🟡 Medium

As the plugin evolves, `config.yml` changes versions frequently, frustrating server admins who upgrade.

**Changes:**
- **`ConfigMigrator.java`** — on startup, detect the existing config's version and apply automatic migrations to bring it up to date.
- **Migration stages** — each config version bump has a corresponding migration function that adds missing keys with defaults and renames changed keys.
- **Backup on migration** — automatically back up the old config to `config_backup_<version>.yml` before migrating.
- **`config-version: 6.7`** key added to `config.yml` top level.
- **Admin notification** — after migration, log a summary of what changed.

---

## Version 7.0 — Comprehensive Bug Hunt & Technical Debt Repayment
**Type:** Patch / Refactor  
**Priority:** 🔴 High (periodic)

A dedicated version with no new features — only bug fixes, code quality improvements, and technical debt payoff accumulated from all prior versions.

**Areas to address:**
- **`GUIListener.java`** uses string-matching for GUI titles (fragile). Migrate to a custom `InventoryHolder` pattern for robust GUI identification.
- **`ForceTabCompleter.java`** — tab completion is incomplete. Complete all subcommands, player names, and ability IDs for all commands.
- **Null safety audit** — run a static analysis tool (e.g. SpotBugs, Checkstyle) and resolve all null pointer warnings.
- **Method extraction** — several methods in `GUIManager.java` and `GUIListener.java` are 50+ lines. Extract into smaller helper methods.
- **Remove `AmbientEffectsManager` anonymous construction** — in `ForcePlugin.java` it's constructed as `new AmbientEffectsManager(this)` and immediately discarded (no field). If it's a Listener it needs to be stored or registered properly; if not needed, remove it.
- **Consistent logging** — standardize all log messages to use the plugin logger (`getLogger().info/warning/severe`) rather than any `System.out` calls.

---

## Version 7.1 — Spectator Mode Compatibility
**Type:** Bug Fix / Compatibility  
**Priority:** 🟡 Medium

Abilities fire from `PlayerInteractEvent` which can be triggered in spectator mode.

**Changes:**
- Add `player.getGameMode() == GameMode.SPECTATOR` checks to all ability activation paths.
- Add same check to `PassiveListener`, `UltimateAbilityListener`, `ExperienceListener`.
- Add to `ForceBarManager` — don't show/update force bar for spectators.

---

## Version 7.2 — Version Adapter Modernization
**Type:** Maintenance  
**Priority:** 🟡 Medium

The version adapter system (`Adapter_1_16`, `Adapter_1_21`, `VersionUtil`) handles multi-MC-version compatibility. As old versions become obsolete and new ones are released, this needs updating.

**Changes:**
- **Drop 1.16 support** — retire `Adapter_1_16.java` (1.16 is very old; most servers run 1.20+). Update the version check in `ForcePlugin.setupVersionAdapter()`.
- **Add 1.21.x explicit adapter** — ensure the `Adapter_1_21` handles all 1.21 API changes correctly (attribute API changes, component-based text, etc.).
- **Prepare for 1.22** — stub out an `Adapter_1_22.java` with a clear TODO so future updates have a clear integration point.
- **`VersionUtil` audit** — ensure `VersionUtil.UNBREAKING`, `VersionUtil.PARTICLE_*` references are all current and not deprecated.

---

## Version 7.3 — New Ability: Force Cloak (Dark Side)
**Type:** New Ability (Dark)  
**Priority:** 🟡 Medium

A dark-side stealth ability (contrast with Light's `ForceCamouflage` which already exists as an ultimate).

**Implementation:**
- **Type:** Dark side exclusive (non-ultimate)
- **Trigger:** `RIGHT_CLICK`
- **Effect:** Applies near-invisibility (particles still visible when moving, but name tag hidden). Unlike the Light Side's `ForceCamouflage`, the Dark Cloak is **offensive** — while cloaked, the next ability used deals 25% bonus damage (the element of surprise). Cloak breaks immediately on ability use.
- **Levels:** 3 levels scaling invisibility duration and bonus damage.
- **Files:** New `ForceCloak.java` in `abilities/dark/`.

---

## Version 7.4 — New Ability: Force Enlightenment (Light Side)
**Type:** New Ability (Light)  
**Priority:** 🟡 Medium

A moment of pure Force communion — the Jedi grants insight to themselves and nearby allies.

**Implementation:**
- **Type:** Light side exclusive
- **Trigger:** `SHIFT`
- **Effect:** The caster enters a brief 3-second meditation. During this window:
  - Cannot move or attack
  - All ability cooldowns reduce at 3x speed
  - Nearby Force allies get their Force energy regenerated at a boosted rate
- **Interruption:** Taking damage cancels the meditation early (partial cooldown reduction is still applied).
- **Levels:** 3 levels scaling cooldown reduction rate and nearby energy boost radius.
- **Files:** New `ForceEnlightenment.java` in `abilities/light/`.

---

## Version 7.5 — New Ability: Force Polarity (Dark Side)
**Type:** New Ability (Dark)  
**Priority:** 🟡 Medium

A unique dark side ability that exploits the flow of Force energy.

**Implementation:**
- **Type:** Dark side exclusive
- **Trigger:** `LEFT_CLICK`
- **Effect:** Temporarily inverts a targeted player's Force energy management for 5 seconds. Their abilities cost double energy. Any energy their abilities consume is siphoned to the caster as Force energy. Counter-intuitive and disorienting — enemies can't safely use abilities without feeding the dark side user.
- **Levels:** 3 levels scaling duration and siphon efficiency.
- **Files:** New `ForcePolarity.java` in `abilities/dark/`.

---

## Version 8.0 — The Mastery Prestige Update
**Type:** Major Feature  
**Priority:** 🟢 Lower priority (Endgame)

Expands the Mastery system introduced in v4.0 with the full prestige mechanic.

**Changes:**
- **Full prestige system** — at Level 100 Mastery, players can `/force prestige` to reset to Level 1, keeping their side and 1 permanent bonus per prestige (up to 5 prestiges).
- **Prestige bonuses** — one new bonus per prestige level:
  - Prestige 1: +5% Force energy regeneration (permanent passive)
  - Prestige 2: +1 Force Point per level-up
  - Prestige 3: All ability cooldowns reduced by 5% permanently
  - Prestige 4: Force energy pool increased by 10 permanently
  - Prestige 5: Unlock a unique cosmetic title and particle effect exclusive to full-prestige players
- **Prestige visual distinction** — prestige players get a star prefix in their rank display and a unique particle aura.
- **Database update** — new `prestige_level` column in player data table.
- **Admin commands** — `/fa setprestige <player> <level>`.

---

## Version 8.1 — Cross-Server Support (BungeeCord / Velocity)
**Type:** Feature (Network Infrastructure)  
**Priority:** 🟢 Lower priority (Far future)

For networks running multiple servers, Force data should persist and sync across all of them.

**Changes:**
- **Switch from SQLite to MySQL** (optional) — add config option to use MySQL for shared DB access across servers:
  ```yaml
  database:
    type: sqlite # or mysql
    mysql:
      host: localhost
      port: 3306
      database: forceplugin
      username: root
      password: secret
  ```
- **Redis pub/sub for live sync** — use Redis (via Jedis library) to broadcast player data changes across servers instantly (so switching servers doesn't cause stale data).
- **BungeeCord plugin messaging** — alternative to Redis, use BungeeCord plugin channels for data sync.
- **Vault integration for economy sync** — ensure economy features (v6.1) also work across network.

---

## Version 8.2 — Modular Ability Loading
**Type:** Developer / Infrastructure  
**Priority:** 🟢 Lower priority

Allow administrators to enable/disable individual abilities at startup without removing config blocks.

**Changes:**
- **Per-ability enable flag in config:**
  ```yaml
  abilities:
    FORCE_PUSH:
      enabled: true
      ...
    FORCE_LIGHTNING:
      enabled: false   # This ability is disabled on this server
      ...
  ```
- **`AbilityManager`** — respect `enabled: false` flag during `loadAbilities()`. Disabled abilities are not registered, don't appear in GUIs, and can't be used.
- **Admin command** — `/fa toggleability <ability_id>` to enable/disable an ability at runtime without full reload.

---

## Version 8.3 — Plugin Integration: ItemsAdder / Oraxen
**Type:** Integration  
**Priority:** 🟢 Lower priority

For servers using custom resource packs via ItemsAdder or Oraxen, allow Force ability icons to use custom textures.

**Changes:**
- **Soft-depend on ItemsAdder and Oraxen** in `plugin.yml`.
- **Config option** — if ItemsAdder/Oraxen is present and enabled, use custom model data IDs for ability icons in GUIs:
  ```yaml
  custom-icons:
    provider: itemsadder # or oraxen or none
    FORCE_PUSH: "forceplugin:force_push"
    FORCE_LIGHTNING: "forceplugin:lightning"
  ```
- **Fallback** — if provider is `none` or the item ID is not found, fall back to vanilla materials.

---

## Version 8.4 — Full Accessibility & UX Audit
**Type:** Enhancement  
**Priority:** 🟢 Lower priority

A dedicated update ensuring the plugin is accessible and usable for all player types.

**Changes:**
- **Colorblind mode** — config option to use pattern-based (bold, underline) formatting instead of relying solely on color to distinguish Light/Dark UI elements.
- **Screen reader friendly messages** — all action bar messages include text content, not just symbols/particles.
- **Reduced-motion mode** — config option to reduce particle density by 75% globally.
- **`/force help`** — a comprehensive in-game help command listing all abilities, commands, and mechanics, formatted as a clickable book.
- **`/force tutorial`** — a guided step-by-step tutorial (as a series of messages and action bar prompts) for new players.

---

## Version 9.0 — The Grand Unification Update
**Type:** Major Feature / Polish  
**Priority:** 🟢 Lower priority (Very far future)

The culmination of the entire roadmap — polish, unify, and solidify all systems added across every prior update.

**Changes:**
- **Full documentation rewrite** — new, comprehensive wiki on SpigotMC covering every feature, config option, and mechanic.
- **All GUIs redesigned** for visual consistency across every screen.
- **Config schema 9.0** — fully documented, fully validated config structure.
- **Complete API 2.0** — full developer API with Javadoc and Maven Central publication.
- **Performance verified** — plugin runs at <0.5ms per tick average on a 100-player stress test.
- **Integration test suite** — automated test covering full player flow (join → choose side → level up → unlock abilities → use abilities → prestige).
- **SpigotMC resource listing update** — detailed, professional plugin page with screenshots, video showcase, and comprehensive FAQ.

---

*This roadmap is a living document. Updates may be reordered, merged, or split as development evolves. Each version number reflects the intended scale of the change — not a strict timeline. Contributions, bug reports, and feature suggestions always welcome.*
