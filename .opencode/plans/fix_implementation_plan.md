# Fix Implementation Plan

## Diagnosis of All Four Bugs

---

### Bug #1: Hotbar Slot Synchronization (Keyboard Numbers Issue)

**Root Cause — `HolocronListener.onScroll` cancels `PlayerItemHeldEvent` for sneaking players, blocking number-key selection.**

`HolocronListener.onScroll(PlayerItemHeldEvent)` (`HolocronListener.java:54-69`) checks if the player is in the `selectingPlayers` map (populated when sneaking begins, cleared when sneaking ends). If the player is sneaking, **it unconditionally cancels `event.setCancelled(true)`** and treats every `PlayerItemHeldEvent` as a scroll-wheel direction to cycle the bind.

**The problem:** `PlayerItemHeldEvent` fires for **both** mouse scroll-wheel turns **and** keyboard number-key presses (`1`–`9`). Whenever a player is sneaking (common in combat!) and presses a number key, the event is cancelled, the hotbar slot does **not** actually change, and the player stays on whatever slot they were on before.

**Secondary consequence — Scoreboard desync:** `HudManager.onHotbarChange` (`HudManager.java:122-124`) fires independently — it reads `event.getNewSlot()` and updates the scoreboard to highlight the "new" slot — but the player is **still on the old slot**. This causes the scoreboard to show the wrong slot highlighted with the wrong ability name, perfectly matching the symptom "selects wrong slot" and "jumps randomly."

---

### Bug #2: Abilities Not Activating

**Root Cause — `AbilityListener` gates all ability execution on holding a physical "holocron" item.**

`AbilityListener.java:37-39`:
```java
if (!plugin.getHolocronManager().isHolocron(itemInHand)) {
    return;
}
```

This returns immediately if the player is not holding a holocron. In a Project Korra–style system, abilities should fire based on the **currently selected hotbar slot's bind** and the **action performed** (left-click, right-click, shift), **without** requiring any specific held item.

Additionally, `HolocronListener.onRightClick` (`HolocronListener.java:72-86`) which opens the specialization GUI is also gated on `isHolocron(event.getItem())`.

---

### Bug #3: Legacy Holocron Item Given at Side Selection

**Root Cause — Side selection GUI still calls `giveHolocron()` when player chooses Light/Dark.**

`GUIListener.java:378,384`:
```java
plugin.getHolocronManager().giveHolocron(player);
```

This code path executes when a player clicks the Light or Dark side wool in `handleChooseSideGUI`. The `LevelingManager` at `LevelingManager.java:76` also references the holocron:
```java
player.sendMessage(ChatColor.YELLOW + "Right-click your Holocron to make your permanent choice.");
```

---

### Bug #4: Scoreboard Not Reflecting Abilities Correctly

**Root Cause:** Same underlying cause as Bug #1 (event cancellation causing desync), compounded by missing `updateScoreboard` calls after certain lifecycle events.

Specifically:
1. When `HolocronListener.onScroll` cancels `PlayerItemHeldEvent`, `HudManager` updates the scoreboard with the **wrong slot** (the cancelled event's new slot, not the player's actual slot).
2. `updateScoreboard` is **not called** after `handleChooseSideGUI` — the scoreboard isn't refreshed until the next hotbar change.
3. `updateScoreboard` is **not called** on player join — the scoreboard is blank until the first hotbar change.

---

## Workspace Modifications

### [MODIFY] `AbilityListener.java` — Remove holocron gate, add SHIFT event trigger

- **Remove** lines 37-39 (`if (!isHolocron(itemInHand)) return;`) — abilities fire regardless of held item.
- **Remove** line 32 (`ItemStack itemInHand = event.getItem();`) — no longer needed.
- **Add** `@EventHandler` for `PlayerToggleSneakEvent` that checks if the currently selected slot has an ability configured with `ActionTrigger.SHIFT`, and if so, executes it. (Currently SHIFT-trigger abilities only fire on left-click-while-shifting, which is unreliable.)
- Restructure so `event.setCancelled(true)` only fires when an ability is actually activated or the bind GUI is opened.

### [MODIFY] `HolocronListener.java` — Stop cancelling `PlayerItemHeldEvent`

**`onScroll` (lines 54-69):**
- **Remove** `event.setCancelled(true);` (line 57) so number-key presses always work.
- Fix the `cycleBind` call to operate on the **new** slot (the one the player scrolled to) rather than the old one. This preserves the scroll-cycle convenience without breaking slot selection.

**`onSneak` (lines 38-51):**
- **Remove** the `isHolocron(itemInHand)` check (line 41) — sneaking displays should work for all players.

**`onRightClick` (lines 72-86):**
- **Remove** the `isHolocron(event.getItem())` check (line 78) — right-click should open bind GUI regardless of held item.

**Remove entire methods (holocron-specific):**
- `onDrop` (lines 88-94) — drop protection no longer needed.
- `onInventoryClick` (lines 96-111) — inventory protection no longer needed.
- `onPlayerDeath` (lines 113-127) — death-keep no longer needed.

**Optionally remove** the `selectingPlayers` map and scroll-cycle feature entirely as a simplification.

### [MODIFY] `HolocronManager.java` — Strip down

- **Keep** `isHolocron(ItemStack)` — useful for detecting legacy items.
- **Keep** `updateHolocronName(Player)` — non-essential but harmless.
- **Remove** `giveHolocron(Player)` — no longer called.
- **Remove** `removeHolocron(Player)` — no longer needed (or keep as cleanup tool).
- **Remove** `createHolocronItem(ForceSide)` — private, no callers left.

### [MODIFY] `GUIListener.java` — Remove holocron from side selection

`handleChooseSideGUI` (lines 365-388):
- **Remove** line 378 (`plugin.getHolocronManager().giveHolocron(player);`) and line 379 (holocron message).
- **Remove** line 384 (`plugin.getHolocronManager().giveHolocron(player);`) and line 385 (holocron message).

### [MODIFY] `HudManager.java` — Fix scoreboard update reliability

- **Add** a public `setupPlayer(Player)` method (or modify `addPlayer`) that calls `updateScoreboard(player)`.
- **Call** `hudManager.setupPlayer(player)` from `PlayerConnectionListener.onPlayerJoin` after data is loaded.
- **Call** `hudManager.updateScoreboard(player)` from `GUIListener.handleChooseSideGUI` after side is set.

### [MODIFY] `PlayerConnectionListener.java` — Trigger scoreboard on join

- **Add** `plugin.getHudManager().updateScoreboard(player)` after `userManager.loadPlayerData(player)`.

### [MODIFY] `AmbientEffectsManager.java` — Stop relying on holocron

- Change the condition at line 32 to remove the `isHolocron(itemInHand)` requirement. Show particles based on side alone whenever the player is in the world, or remove the feature.

### [MODIFY] `LevelingManager.java` — Fix milestone message

- Change line 76 from `"Right-click your Holocron to make your permanent choice."` to `"Use §6/force choose§e to select your specialization."`.

### [MODIFY] `ForceAdminCommand.java` — Remove holocron admin commands

- **Remove** the `giveholocron` case (line 41).
- **Remove** `handleGiveHolocron` method (lines 199-216).
- **Keep** `removeHolocron` in `handleReset` (line 97) for cleaning up legacy items.
- **Remove** the giveholocron line from `sendHelpMessage` (line 227).

### [MODIFY] `ForcePlugin.java` — Deregister HolocronListener

- **Remove** line 74: `getServer().getPluginManager().registerEvents(new HolocronListener(this), this);`
- Remove `import org.perseus.forcePlugin.listeners.HolocronListener;` (or the wildcard import covers it).

---

## Step-by-Step Fix Execution Checklist

- [ ] **1. Fix `AbilityListener.java`** — Remove holocron gate; add `PlayerToggleSneakEvent` handler for SHIFT triggers.
- [ ] **2. Fix `HolocronListener.java`** — Remove `event.setCancelled(true)`; remove all `isHolocron` checks; remove drop/click/death handlers.
- [ ] **3. Strip `HolocronManager.java`** — Remove `giveHolocron`, `removeHolocron`, `createHolocronItem`.
- [ ] **4. Remove holocron from `GUIListener.java`** — Remove `giveHolocron` calls + messages in `handleChooseSideGUI`.
- [ ] **5. Fix `HudManager.java`** — Ensure `updateScoreboard` is called on join and after side selection.
- [ ] **6. Fix `PlayerConnectionListener.java`** — Call `hudManager.updateScoreboard` on join.
- [ ] **7. Fix `AmbientEffectsManager.java`** — Remove `isHolocron` depedency for particle effects.
- [ ] **8. Fix `LevelingManager.java`** — Update holocron milestone message.
- [ ] **9. Fix `ForceAdminCommand.java`** — Remove `giveholocron` subcommand.
- [ ] **10. Fix `ForcePlugin.java`** — Deregister `HolocronListener`.
- [ ] **11. Build & test** — `./gradlew build`; verify number keys, ability activation, scoreboard, and no holocron on side selection.
