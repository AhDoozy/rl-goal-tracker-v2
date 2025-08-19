# Changelog

## [Unreleased] - 2025-08-18

### Added
- Quest prerequisites button: Each quest task now has an **Add prereqs** button to insert its prerequisites directly beneath it.
- Shift+Click removal: Shift+Click a task to remove it and all its indented children at once.
- Completion cascading: Marking a parent quest as complete/incomplete now automatically updates all its child tasks.
- Dropdown quest selector: Replaced fuzzy search with a clean dropdown of all quests, displaying natural names (e.g., "Tree Gnome Village").

### Changed
- Pre-req button made more compact (~25% smaller).
- Prerequisite insertion now places child tasks directly below their parent instead of at the end of the list.
- UI now automatically refreshes after task mutations (add/remove/indent/status change).

## [Unreleased] - 2025-08-19

### Added
- Right-click menu option **Add pre-reqs** for quest tasks with prerequisites. Automatically inserts missing prerequisites as subtasks with proper indentation and prevents duplicates.

### Changed
- Right-click context menu reorganized: Move actions grouped under a **Move** submenu; Remove action now labeled as **Remove (Shift+Left Click)**.
- Quest tasks now only display the **Add pre-reqs** option if they actually have missing prerequisites.
- Search button now toggles between **Search...** and **Close** to open/close the item search overlay.
- Removed in-panel close button; close control is now handled directly via the Search button toggle.
- Green **+Add** button hidden for item search inputs (still visible for other input types).
- Duplicate prerequisites can no longer be added multiple times to the same quest.
- Quest dropdown now uses **RuneScape UF** font at a normal crisp size for improved readability.
- ComboBox font scaling updated to use integer point sizes, preventing fuzzy text.
- QuestTaskInput updated to rely on shared ComboBox styling for consistency.
- Back and Undo buttons added to a new top control bar in goal view; later removed and replaced with a cleaner single bar design.
- Embedded red "< Back" button removed from GoalPanel header, leaving only the goal name input aligned cleanly.
- Goal name input updated to support copy, paste, cut, and select-all actions via both context menu and keyboard shortcuts.
- Remove menu option enhanced to also delete all indented child tasks when removing a parent.
- Remove menu label updated so the "(Shift+Left Click)" hint displays smaller and in gray.