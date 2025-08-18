

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
