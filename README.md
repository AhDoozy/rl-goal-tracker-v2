# 🏆 Goal Tracker v2

> A complete reimagining of the Goal Tracker plugin — rebuilt with a more modern UI, powerful new features, and improved stability to help you plan, track, and achieve your Old School RuneScape goals with ease.

---

<details>
<summary><h2 style="margin:0;display:inline;">✨ New Features</h2></summary>

- Shift+Click removal of tasks for faster task management
- Automatic goal status checks for up-to-date progress
- New ActionBar and ActionBarButton UI components
- Hover states for better visual feedback
- New context menu organization for streamlined interaction
- Search toggle improvements for easier task searching
- New task right‑click menu with grouped **Move** submenu and cascading complete/incomplete toggle that applies to children (expanded beyond v1 functionality).
- Goal cards redesigned with lighter fills, full shadows, and hover/press effects only on the card face.
- Thicker header divider under “Goal Tracker” for clearer separation.
- Automatic refresh propagation so Home goals update instantly when tasks change.
- Completion chat messages delivered as proper Game messages with configurable colors.

### ♻️ Redesigned Features

- Redesigned quest prerequisites button for quick access to quest requirements
- Redesigned completion cascading to automatically complete related tasks
- Redesigned dropdown quest selector for faster quest task addition
- Redesigned right-click menus for prerequisites and child completion options
- Redesigned manual toggling for preset tasks to customize workflow
- Redesigned chatbox notification colors (now fully customizable)

</details>

<details>
<summary><h2 style="margin:0;display:inline;">🔧 Improvements</h2></summary>

- More compact prereq button for a cleaner interface
- Refreshed UI with updated design elements
- Font and ComboBox readability enhancements
- Consistent ActionBar UI throughout the plugin
- Unified goal view header for a cohesive look
- Improved context menus with better usability
- Enhanced cursor and hover detection accuracy
- Copy and paste support in the goal name input field
- Context menu entries rebuilt dynamically before opening to always reflect the latest state.
- Task and goal content now force an initial refresh so icons and text render correctly at login.

</details>

<details>
<summary><h2 style="margin:0;display:inline;">🐛 Fixes</h2></summary>

- Undo/Redo functionality cleanup for smoother editing
- ActionBarButton painting fixes to prevent visual glitches
- GoalTrackerPanel `home()` method refresh improvements
- Correct refresh behavior in ListPanel
- Improved mouse selection accuracy
- Keyboard shortcut fixes and enhancements
- Automatic removal of empty goals to keep lists tidy
- Visual refresh issue resolved on login
- Fixed child task refresh issues by recursively refreshing all descendants.
- Fixed blank panel issue when switching from Home to Goal view.
- Fixed completion chat message not appearing on task completion.

</details>

<details>
<summary><h2 style="margin:0;display:inline;">📥 Installation</h2></summary>

1. Open RuneLite.  
2. Go to the Plugin Hub.  
3. Search for "Goal Tracker v2".  
4. Click **Install**.  

</details>

<details>
<summary><h2 style="margin:0;display:inline;">🚀 Getting Started</h2></summary>

- Open the plugin panel in RuneLite once installed.  
- Use **+ Add goal** to create a new goal.  
- Add tasks (quests, skills, items, or manual) via the goal view.  
- Use the new **ActionBar** buttons for navigation, undo/redo, and bulk actions.  

</details>

<details>
<summary><h2 style="margin:0;display:inline;">🖼️ Screenshots</h2></summary>

[screenshot] Home panel with goal cards  
[screenshot] Inside a goal with task list  
[screenshot] Right‑click menu on a task  
[screenshot] Config panel with customizable chat color  

</details>

<details>
<summary><h2 style="margin:0;display:inline;">🙏 Acknowledgements</h2></summary>

- Original plugin created by **dillydill123**.  
- Fully renovated and maintained by **AhDoozy**.

</details>
## 📄 License
Licensed under the [BSD 2-Clause License](LICENSE).  

-----
<details>
<summary><span style="margin-left:8px;"><h3 style="display:inline;">📜 Original Goal Tracker v1 Readme & Documentation (created by dillydill123)</h3></span></summary>

# Runelite Goal Tracker Plugin

Keep track of your OSRS goals and complete them automatically.

## Features

- Track different types of tasks
    - Manual tasks
    - Skill tasks
    - Quests
    - Item tasks
- Organise tasks lists into goals
- Reorder and manage goal and task lists
- Chat notification on task completion

### Planned

- More task types
    - Achievement diaries
    - Minigame rewards
    - Kourend favour
    - NPC kills

Suggestions are welcome - please submit an issue :)

## Usage

### Goals

Goals are lists of tasks, and at a glance provide a quick way to view your progress towards the goal.

![Goals list](img/goals_list.png)

You can add a new goal with the "+ Add goal" button, and you can reorder/remove goals using right click. Clicking a goal will show the tasks within:

![Goal view](img/goal_view.png)

From here, you can add tasks to the goal.

### Adding tasks

![Task inputs](img/task_inputs.png)

#### Manual tasks

Basically a simple to-do list item. You can add these via the "Quick add" text box.

You can toggle them on and off manually just by clicking them.

Use the "+ More options" button to reveal the automatic task options.

#### Skill level/XP tasks

Use these tasks to automatically track skill progress. Just select a skill, and the desired level or XP amount. The task will automatically complete once you hit that level/xp.

#### Quest tasks

Track quest progress and completion, just select a quest or miniquest from the dropdown. Will also display in progress quests as orange.

#### Item tasks

Select an item using the search button and searching via the in-game chatbox, then set the desired quantity. The plugin will keep track of your items and tally up quantities stored in different inventories (bank, player, GIMP storage), and will be automatically completed once you get that amount of the item.

</details>