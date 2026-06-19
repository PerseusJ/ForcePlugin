# ForcePlugin — Hotbar Binding & GUI Implementation Plan

## 1. Architecture Overview

The current activation flow uses a **single global `activeAbilityId`** in `ForceUser`. Left-clicking the Holocron fires that one ability. We will replace this with a **per-hotbar-slot binding system** where each of the 9 inventory slots can hold a different bound Force ability.

### New Activation Flow

```
Player holds Holocron
  ├─ Left-click (air/block)  →  Look up bound ability for current hotbar slot → execute
  ├─ Right-click (air/block) →  Open Hotbar-Bind GUI
  └─ Sneak + Scroll          →  Cycle unlocked abilities bound to current slot
```

### Key Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Action triggers | **3 triggers per ability** (LEFT_CLICK, RIGHT_CLICK, SHIFT) stored in config | Allows data-driven trigger assignment per ability |
| GUI interaction | **2-phase**: first a "hotbar overview" GUI, then click a slot to pick ability | Prevents theft via `setCancelled(true)`, matches existing GUI patterns |
| Persistence | **SQLite TEXT column** with JSON map | Reuses existing `DatabaseManager` + Gson pattern |
| HUD | **Sidebar Scoreboard** (optional, toggle in config) | Visual feedback without mods; can be disabled |
| Backward compat | Old `activeAbilityId` field **removed**; cycling system repurposed to bind to current slot | Clean break — the new system is a superset |

---

## 2. Data Structures

### 2.1 New Enum — `ActionTrigger`

Defines what player action activates an ability.

```java
public enum ActionTrigger {
    LEFT_CLICK,
    RIGHT_CLICK,
    SHIFT
}
```

### 2.2 New Field in `ForceUser` — `slotBinds`

```java
private final Map<Integer, String> slotBinds = new HashMap<>();
// Int key = hotbar slot index (0-8), String value = ability ID (null = empty)
```

**Accessor methods to add:**
- `getBoundAbilityId(int slot)` → returns ability ID or null
- `setSlotBind(int slot, String abilityId)` → sets bind (removes key if abilityId is null)
- `getSlotBinds()` → returns the full map
- `clearSlotBinds()` → empties all binds

### 2.3 Database `slot_binds` Column

```sql
ALTER TABLE force_users ADD COLUMN slot_binds TEXT;
```

Stored as JSON: `{"0":"FORCE_PUSH","2":"FORCE_LIGHTNING","4":"FORCE_HEAL"}`

### 2.4 No Changes to `Ability` Interface

Triggers are **not hardcoded** into the interface. Instead, they are read from config (new `trigger` key per ability). This keeps the interface clean and makes triggers data-driven.

**Config addition (in `config.yml`):**
```yaml
abilities:
  FORCE_PUSH:
    trigger: LEFT_CLICK   # <-- NEW
    unlock-cost: 1
    ...
  TELEKINESIS:
    trigger: RIGHT_CLICK  # alternative trigger example
    ...
```

`AbilityConfigManager` will gain a method:
```java
public ActionTrigger getTrigger(String abilityId)
```

---

## 3. Workspace Modifications — File-by-File Breakdown

All paths are relative to `src/main/java/org/perseus/forcePlugin/` unless noted.

### 3.1 NEW Files

| # | File Path | Purpose |
|---|-----------|---------|
| 1 | `data/ActionTrigger.java` | Enum: LEFT_CLICK, RIGHT_CLICK, SHIFT |
| 2 | `gui/BindGUI.java` | Hotbar-binding chest inventory (main bind UI) |
| 3 | `gui/AbilityPickerGUI.java` | Sub-GUI showing unlocked abilities to pick for a slot |
| 4 | `listeners/HotbarListener.java` | Handle `PlayerItemHeldEvent` for HUD updates, slot-change feedback |

### 3.2 MODIFY Files

| # | File | Changes |
|---|------|---------|
| 1 | `data/ForceUser.java` | Add `slotBinds` map + accessor methods |
| 2 | `data/DatabaseManager.java` | Add `slot_binds` column to `initializeDatabase()` + `addColumnIfNotExists()`; load/save binds in `loadPlayerData()` / `savePlayerData()` |
| 3 | `managers/ForceUserManager.java` | After async load, set default binds for slot 0 (FORCE_PUSH) if binds are empty |
| 4 | `managers/AbilityConfigManager.java` | Add `getTrigger(String abilityId)` method reading `"trigger"` key from config |
| 5 | `gui/GUIManager.java` | Add `openBindGUI(Player)` and `openAbilityPickerGUI(Player, int slot)` methods |
| 6 | `gui/GUIListener.java` | Add handlers for `BindGUI` and `AbilityPickerGUI` inventory titles |
| 7 | `listeners/AbilityListener.java` | **Major refactor**: replace `activeAbilityId` lookup with `slotBinds[player.getInventory().getHeldItemSlot()]` lookup; add right-click handling to open GUI; add sneaking check for SHIFT-trigger abilities |
| 8 | `listeners/HolocronListener.java` | Repurpose sneak+scroll: instead of cycling `activeAbilityId`, **bind the cycled ability to the current slot**; right-click now opens `BindGUI` instead of old ability GUI |
| 9 | `commands/ForceCommand.java` | Add `/force bind [slot] [ability]` command for chat-based binding |
| 10 | `commands/ForceTabCompleter.java` | Add tab completion for `/force bind` subcommand |
| 11 | `ForcePlugin.java` | Register `HotbarListener`; no new managers needed (binds live on `ForceUser` directly) |

### 3.3 DELETE / Deprecate

| Item | Reason |
|------|--------|
| `ForceUser.activeAbilityId` field (and getter/setter) | Replaced by per-slot bind map |
| `HolocronListener.selectingPlayers` + `cycleAbility()` method | Repurposed: cycling now writes to `slotBinds` for current slot |
| Old "Selected:" action-bar display in `HolocronListener.showSelectedAbility()` | Replaced by HUD/bind GUI feedback |

---

## 4. Step-by-Step Execution Plan

### Phase 1 — Data Layer (Foundation)

- [ ] **1.1** Create `data/ActionTrigger.java`
- [ ] **1.2** Edit `data/ForceUser.java`: add `slotBinds` field + accessor methods; remove `activeAbilityId`
- [ ] **1.3** Edit `data/DatabaseManager.java`: add `slot_binds` column migration; update `loadPlayerData`/`savePlayerData` to read/write bind JSON
- [ ] **1.4** Edit `managers/ForceUserManager.java`: after loading, set default binds (slot 0 = FORCE_PUSH, slot 1 = FORCE_PULL) if empty
- [ ] **1.5** Edit `managers/AbilityConfigManager.java`: add `getTrigger(abilityId)` method

### Phase 2 — Event Listener Refactor

- [ ] **2.1** Edit `listeners/AbilityListener.java`:
  - Replace `forceUser.getActiveAbilityId()` with `slotBinds[heldItemSlot]`
  - Add `RIGHT_CLICK_AIR`/`RIGHT_CLICK_BLOCK` handling → open BindGUI
  - Add `SHIFT` trigger check (if ability trigger is SHIFT, require `player.isSneaking()`)
- [ ] **2.2** Edit `listeners/HolocronListener.java`:
  - Repurpose `cycleAbility()`: instead of setting `activeAbilityId`, call `forceUser.setSlotBind(currentSlot, cycledAbilityId)`
  - `onRightClick()`: change from opening `ABILITY_GUI` to opening `BIND_GUI`
- [ ] **2.3** Create `listeners/HotbarListener.java`:
  - Listen for `PlayerItemHeldEvent` → update HUD / send action-bar showing bound ability for new slot

### Phase 3 — GUI Layer

- [ ] **3.1** Create `gui/BindGUI.java`:
  - 27-slot inventory (3 rows)
  - Top row (slots 0-8): representations of hotbar slots 1-9 with bound ability icons
  - Middle row: instructional glass panes, bottom row: controls (close, reset slot)
  - Click a hotbar slot → open `AbilityPickerGUI` for that slot
- [ ] **3.2** Create `gui/AbilityPickerGUI.java`:
  - 54-slot inventory (6 rows) — paginated list of player's unlocked abilities
  - Click an ability → bind it to the chosen slot → close / return to BindGUI
- [ ] **3.3** Edit `gui/GUIManager.java`:
  - Add `openBindGUI(Player)`, `openAbilityPickerGUI(Player, int slot)` methods
  - Add title constants: `BIND_GUI_TITLE`, `ABILITY_PICKER_TITLE_PREFIX`
- [ ] **3.4** Edit `gui/GUIListener.java`:
  - Add `handleBindGUI()` and `handleAbilityPickerGUI()` private methods
  - Add title checks in the main `onInventoryClick()` dispatcher
  - All clicks cancelled; items are decorative/functional only

### Phase 4 — Commands & Polish

- [ ] **4.1** Edit `commands/ForceCommand.java`:
  - Add `/force bind <slot> [ability]` — bind specific ability to slot (with tab completion)
  - Add `/force bind list` — shows current binds in chat
  - Add `/force bind gui` — opens the Bind GUI
- [ ] **4.2** Edit `commands/ForceTabCompleter.java`: complete ability IDs for `/force bind <slot> <ability>`
- [ ] **4.3** Edit `ForcePlugin.java`: register `HotbarListener`

### Phase 5 — Optional HUD (Scoreboard)

- [ ] **5.1** (Optional) Create `managers/HudManager.java`:
  - Manages a per-player `Scoreboard` with 9 lines (slots 1-9)
  - Highlights current slot
  - Updates on `PlayerItemHeldEvent` or when binds change
- [ ] **5.2** (Optional) Toggle in `config.yml`: `hud.enabled: true`
- [ ] **5.3** (Optional) Hook into `ForcePlugin.onEnable`/`onDisable` for cleanup

---

## 5. Event Flow — Detailed Sequence

### Scenario: Player activates a bound ability

```
1. Player left-clicks air/block while holding Holocron
2. PlayerInteractEvent fires (LEFT_CLICK_AIR or LEFT_CLICK_BLOCK)
3. AbilityListener.onPlayerInteract() receives the event
4. Check: item in hand is Holocron? Yes → proceed
5. Check: player is sneaking? If ability trigger is SHIFT, require true
6. Get heldItemSlot from player.getInventory().getHeldItemSlot()
7. Look up forceUser.getSlotBinds().get(heldItemSlot) → "FORCE_PUSH"
8. Get Ability from AbilityManager
9. Check cooldown, energy cost, unlocked, etc. (existing logic)
10. Execute ability
```

### Scenario: Player opens Bind GUI

```
1. Player right-clicks air/block while holding Holocron
2. PlayerInteractEvent fires (RIGHT_CLICK_AIR or RIGHT_CLICK_BLOCK)
3. HolocronListener.onRightClick() receives the event
4. Open BindGUI inventory
5. BindGUI shows 9 hotbar slots with current binds
6. Player clicks slot 3 → opens AbilityPickerGUI for slot 3
7. Player sees all unlocked abilities; clicks "Force Lightning"
8. ForceUser.setSlotBind(3, "FORCE_LIGHTNING")
9. Return to BindGUI with updated display
```

---

## 6. Bind GUI Mockup

```
┌─────────────────────────────────────────────┐
│  Hotbar Binding — Click a slot to bind      │
├─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┤
│  1  │  2  │  3  │  4  │  5  │  6  │  7  │  8  │  9  │
│ Push│Pull │     │Light│     │     │     │     │     │
├─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┤
│                                                     │
│  [Reset Slot]                    [Unbind All] [Close]│
└─────────────────────────────────────────────────────┘
```

Each hotbar slot item shows:
- If bound: ability's material icon (Nether Star / Redstone) + ability name + level
- If empty: gray stained glass pane with "Empty Slot" text

---

## 7. Config Additions

`config.yml` additions:

```yaml
# Per-ability trigger (LEFT_CLICK, RIGHT_CLICK, or SHIFT)
abilities:
  FORCE_PUSH:
    trigger: LEFT_CLICK
    ...
  TELEKINESIS:
    trigger: RIGHT_CLICK
    ...

# Optional HUD settings
hud:
  enabled: true        # Toggle sidebar scoreboard
  title: "&6Force Binds"
```

---

## 8. Compatibility Notes

- **Existing save files**: The old `active_ability` column in SQLite is kept but ignored. The new `slot_binds` column is written fresh.
- **Placeholders**: `%forceplugin_active_ability%` should now return the ability bound to the player's current hotbar slot (update `ForcePlaceholders.java`).
- **Permissions**: No new permission nodes needed. Existing `forceplugin.use` covers all binding operations.
- **API version**: All changes use Spigot/Paper API compatible with 1.16–1.21 (matching existing adapter range).
