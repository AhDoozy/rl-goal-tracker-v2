# 🏆 RuneLite Goal Tracker v2

> A complete reimagining of the Goal Tracker plugin — rebuilt with a more modern UI, powerful new features, and improved stability to help you plan, track, and achieve your Old School RuneScape goals with ease.

---

## ✨ New Features

- 🌟 Quest prerequisites button for easy access to quest requirements
- ⚡ Shift+Click removal of tasks for faster task management
- 🔗 Completion cascading to automatically complete related tasks
- 🎯 Dropdown quest selector for quicker quest task addition
- 🖱️ Right-click menus for prereq and child completion options
- 🔄 Manual toggling for preset tasks to customize your workflow
- 🎨 Customizable chatbox colors for notifications
- ⏱️ Automatic goal status checks for up-to-date progress
- 🆕 New ActionBar and ActionBarButton UI components
- 👆 Hover states for better visual feedback
- 📋 New context menu organization for streamlined interaction
- 🔍 Search toggle improvements for easier task searching

---

## 🔧 Improvements

- 🏷️ More compact prereq button for a cleaner interface
- ✨ Refreshed UI with updated design elements
- 🔠 Font and ComboBox readability enhancements
- 🧩 Consistent ActionBar UI throughout the plugin
- 🧑‍💻 Unified goal view header for a cohesive look
- 🖱️ Improved context menus with better usability
- 🎯 Enhanced cursor and hover detection accuracy
- 📋 Copy and paste support in the goal name input field

---
## 🐛 Fixes

- ♻️ Undo/Redo functionality cleanup for smoother editing
- 🎨 ActionBarButton painting fixes to prevent visual glitches
- 🏠 GoalTrackerPanel `home()` method refresh improvements
- 🔄 Correct refresh behavior in ListPanel
- 🖱️ Improved mouse selection accuracy
- ⌨️ Keyboard shortcut fixes and enhancements
- 🗑️ Automatic removal of empty goals to keep lists tidy
- 🔄 Visual refresh issue resolved on login

---

## 📥 Installation

1. Open RuneLite.  
2. Go to the Plugin Hub.  
3. Search for "Goal Tracker v2".  
4. Click **Install**.  

---

## 🚀 Getting Started

- Open the plugin panel in RuneLite once installed.  
- Use **+ Add goal** to create a new goal.  
- Add tasks (quests, skills, items, or manual) via the goal view.  
- Use the new **ActionBar** buttons for navigation, undo/redo, and bulk actions.  
- Right-click goals or tasks for advanced options like adding prerequisites, marking all children complete, or removing tasks quickly.  

---

## 🖼️ Screenshots

_Coming soon_  
*(Screenshots and GIFs will be added here to showcase the new UI and features.)*

---

## 🙏 Acknowledgements

- Original plugin created by **dillydill123**.  
- Fully renovated and maintained by **AhDoozy**.

---
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