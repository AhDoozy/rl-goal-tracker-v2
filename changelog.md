# Changelog

## [Unreleased]

-### Added
- Quest prerequisites button: Each quest task now has an **Add prereqs** button to insert its prerequisites  
  directly beneath it.
- Shift+Click removal: Shift+Click a task to remove it and all its indented children at once.
- Completion cascading: Marking a parent quest as complete/incomplete now automatically updates all its  
  child tasks.
- Dropdown quest selector: Replaced fuzzy search with a clean dropdown of all quests, displaying natural  
  names (e.g., "Tree Gnome Village").
- Right-click menu option **Add pre-reqs** for quest tasks with prerequisites. Automatically inserts  
  missing prerequisites as subtasks with proper indentation and prevents duplicates.
- New `ActionBar` and `ActionBarButton` UI components introduced for consistent toolbar styling across  
  panels.
- Cursor hover state and hand cursor indicator for goal list items to improve usability.
- Added right-click menu to parent goals to mark all child tasks as completed or incomplete.
- Added manual completion toggling for tasks created from presets, allowing users to right-click and mark them complete/incomplete just like quick-added tasks.
- Added customizable color setting for task completion messages shown in the chatbox.
- Implemented automatic goal status checking upon login to mark goals as completed if requirements are already met.
- Context menu entries now rebuilt dynamically right before opening to ensure the latest state.
- Task list panel context menus reorganized with a **Move** submenu and cascading complete/incomplete toggle that applies to children.
- Initial refresh calls for Task and Goal content so icons/text render correctly on login.
- Automatic ancestor `ListPanel` refresh propagation to keep Home goal list in sync when tasks change.
- Export and Import functionality: Goals can now be exported to JSON and imported back with full UI refresh.
- JSON file filter in Import/Export dialogs for safer file selection.
- Automatic warming of ItemTask icons on plugin startup and login tick, and after JSON import, ensuring icons display immediately.
- GoalsChangedListener system in GoalManager to notify panels and auto-refresh home view when goals are saved or loaded.
- Preset Goal Lists: Added “Add from Preset…” button in the Goal Tracker panel, with initial presets (Quest Cape Core, Early Game Ironman). Presets now expand to include prerequisite quests automatically.
- Centralized presets into a new `GoalPresetRepository` class for easier management and expansion.
- Automatic prerequisite expansion for presets leverages the same logic as the quest right-click **Add prereqs** option.
- Ellipsized titles: Goal cards and Task rows now ellipsize long titles with `…` and show full text on hover via tooltip.
- Click-to-edit: Goal titles and ManualTask descriptions can now be edited by clicking their label; label swaps to an inline text field and saves on Enter or blur.
- Added per-lane Ladlor presets (Melee, Ranged, Magic, Utility, Void Set, God Capes, Crystal & Bowfa, Jewelry & Boots, Slayer & Undead, Imbued Rings, God Wars Armor, Elite Void, Prayer Scrolls, Raids Uniques, Milestones & Capes), alongside the full combined Ladlor Ironman preset.
- Added Early, Mid, and Late Ironman presets as consolidated single-goal lists rather than split by category.
- Added a new **Add pre-reqs** button to the Goal view header, next to Undo/Redo. This button triggers the same logic as the quest right-click **Add prerequisites** option, but applies to all quest tasks in the goal at once.
- Converted all Early, Mid, and Late Ironman preset skill milestones from ManualTask to SkillLevelTask for accurate level tracking.
- Expanded Early Ironman preset with Birdhouse run unlocks (Dig Site, Bone Voyage, Hunter 5, Crafting 8, Construction 16) and Seaweed run unlock (Farming 23).
- Reworked Early, Mid, and Late Ironman presets with more appropriate content and progression pacing (e.g., Early emphasizes graceful set and mobility, Mid includes Iban’s staff, Ava’s, Barrows gloves, Late includes Blowpipe, Bowfa, Bandos, endgame quests).
- Commented out Ladlor presets from default loading in GoalPresetRepository to simplify active preset list.
- Fixed incorrect ItemID constants for Salve amulet (ei) in Slayer & Undead presets (SALVE_AMULET_EI).
- Removed stray selection marker and corrected field declaration in GoalPresetRepository.
- Added Full Void Armor preset (base Void top, robe, gloves, melee/range/mage helms).
- Added Free-to-Play Quests preset including all 20 F2P quests, ordered from quick to long, with partner note for Shield of Arrav.
- Added Fast Travel Unlocks preset covering quests that unlock teleport networks, transportation methods, and teleport items (spirit trees, gliders, fairy rings, balloons, Kourend memoirs, Drakan's medallion, etc.).

### Changed
- Pre-req button made more compact (~25% smaller).
- Prerequisite insertion now places child tasks directly below their parent instead of at the end of the  
  list.
- UI now automatically refreshes after task mutations (add/remove/indent/status change).
- Right-click context menu reorganized: Move actions grouped under a **Move** submenu; Remove action now  
  labeled as **Remove (Shift+Left Click)**.
- Quest tasks now only display the **Add pre-reqs** option if they actually have missing prerequisites.
- Search button now toggles between **Search...** and **Close** to open/close the item search overlay.
- Removed in-panel close button; close control is now handled directly via the Search button toggle.
- Green **+Add** button hidden for item search inputs (still visible for other input types).
- Duplicate prerequisites can no longer be added multiple times to the same quest.
- Quest dropdown now uses **RuneScape UF** font at a normal crisp size for improved readability.
- ComboBox font scaling updated to use integer point sizes, preventing fuzzy text.

  - Back and Undo buttons added to a new top control bar in goal view; later removed and replaced with a  
    cleaner single bar design.
  - Embedded red "< Back" button removed from GoalPanel header, leaving only the goal name input aligned  
    cleanly.
  - Goal name input updated to support copy, paste, cut, and select-all actions via both context menu and  
    keyboard shortcuts.
  - Remove menu option enhanced to also delete all indented child tasks when removing a parent.
  - Remove menu label updated so the "(Shift+Left Click)" hint displays smaller and in gray.
  - Home view updated: "Goal Tracker" title moved to its own header, with a new action bar beneath it  
    containing **+ Add goal**, **Move**, and **Bulk Edit** buttons (the latter two are placeholders marked  
    "Coming soon").
  - Home panel action bar refactored to use new `ActionBar` and `ActionBarButton` components for consistent  
    styling.
  - Goal view header updated to use unified `ActionBar` with Back (left) and Undo/Redo (right) buttons.
  - ActionBar now includes a center spacer to properly separate left and right button groups.
  - Right-click context menus fixed to reliably open across platforms (Windows, macOS, Linux).
  - Task right-click now defaults to showing full ListItemPanel menu (move, remove, etc.), with fallback to  
    toggle-only menu if no parent menu exists.
  - Goal item context menus updated to forward right-clicks to parent ListItemPanel menus for consistent  
    options.
  - Cursor/hover detection improved on home goal list: listeners now attach recursively to all child  
    components for accurate selection and highlighting.
- Goal cards now use a lighter fill with a full shadow around borders, and hover/press only affect the card face.
- Header divider under “Goal Tracker” made thicker (4px) for stronger separation.
- Right-click menus refactored so Tasks build their own menu and Goals build theirs, preventing duplicate/unusable items.
- ActionBar spacing refined between Redo and Export buttons to prevent overlap and fit Import button.
- Plugin startup now triggers item icon warm-up so icons are ready before login.
- “+ Add goal” and “Add from Preset…” buttons now stacked vertically in the Goal Tracker panel header for cleaner layout.
- Goal card progress text (e.g., “1/10”) reserved fixed width and no longer clips; typography is consistent and does not shrink.
- Task rows updated with consistent styling and ellipsis/edit behavior, matching Goal cards for a unified UI.

-### Fixed
- Home panel Undo/Redo buttons removed; these controls now exist only in Goal view.
- ActionBarButton painting fixed: clears background correctly, text always drawn on top, and hover state no longer causes overlapping artifacts.
- GoalTrackerPanel `home()` method fixed so returning from Goal view refreshes and displays the goal list instead of a blank panel.
- ListPanel `refresh()` updated to rebuild list before refreshing children, preventing stale or empty views.
- Improved accuracy of mouse selection in home goal list; click/hover listeners now consistently cover the entire item area.
- Goal name input now fully supports keyboard shortcuts (Ctrl/Cmd+C, V, X, A, Insert/Delete variants) and right-click context menu for copy/paste across all platforms.
- Empty goals (0/0 tasks) created via **+ Add goal** are now automatically removed when backing out without adding tasks, preventing clutter in saved goal lists.
- Fixed visual refresh issue where quest task statuses didn’t show correctly on login unless re-entering the goal.
- Fixed child task refresh issues after parent complete/incomplete cascades by recursively refreshing all descendants.
- Fixed blank panel issue when switching from Home to Goal view by only using inner card body for Goal rows.
- Fixed completion chat message not appearing; now delivered as a proper `GAMEMESSAGE` with customizable config color.
- Export/Import buttons previously non-functional; now wired to save/load JSON correctly.
- Item icons not appearing until entering a goal; fixed by warming icons at startup/login and after import.
- Home panel not refreshing after task completion; fixed with GoalsChangedListener refresh hook.
- Overlapping UI issue around Export button resolved by adjusting panel borders and layout.
