package com.toofifty.goaltracker.ui.components;

import com.toofifty.goaltracker.ui.TaskItemContent;
import com.toofifty.goaltracker.utils.QuestRequirements;

import com.toofifty.goaltracker.utils.ReorderableList;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.models.enums.Status;
import java.util.function.Consumer;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.SwingUtilities;
import java.awt.Container;

public class ListTaskPanel extends ListItemPanel<Task>
{
    private TaskItemContent taskContent;

    private final JMenuItem indentItem = new JMenuItem("Indent");
    private final JMenuItem unindentItem = new JMenuItem("Unindent");

    private Consumer<Task> indentedListener;
    private Consumer<Task> unindentedListener;

    public ListTaskPanel(ReorderableList<Task> list, Task item)
    {
        super(list, item);

        indentItem.addActionListener(e -> {
            // Indent all of the items children
            var index = list.indexOf(item);
            for (int i = index + 1; i < list.size(); i++) {
                var child = list.get(i);

                // If a child is less indented then this item assume its a parent node and break
                if (item.getIndentLevel() >= child.getIndentLevel()) break;

                child.indent();
            }

            item.indent();
            if (this.indentedListener != null) this.indentedListener.accept(item);
            refreshParentList();
        });

        unindentItem.addActionListener(e -> {
            // Unindent all of the items children
            var index = list.indexOf(item);
            for (int i = index + 1; i < list.size(); i++) {
                var child = list.get(i);

                // If a child is less indented then this item assume its a parent node and break
                if (item.getIndentLevel() >= child.getIndentLevel()) break;

                child.unindent();
            }

            item.unindent();
            if (this.unindentedListener != null) this.unindentedListener.accept(item);
            refreshParentList();
        });
        // Allow shift-click to remove this item and all its indented children
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isShiftDown() && e.getButton() == MouseEvent.BUTTON1) {
                    int index = list.indexOf(item);
                    int baseIndent = item.getIndentLevel();
                    // Remove all children that are more indented than this item
                    while (index + 1 < list.size() && list.get(index + 1).getIndentLevel() > baseIndent) {
                        list.remove(list.get(index + 1));
                    }
                    // Remove the item itself
                    removeItem.doClick();
                    refreshParentList();
                }
            }
        });
        // Also apply the same shift-click removal listener to all child components
        for (Component child : getComponents()) {
            child.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.isShiftDown() && e.getButton() == MouseEvent.BUTTON1) {
                        int index = list.indexOf(item);
                        int baseIndent = item.getIndentLevel();
                        while (index + 1 < list.size() && list.get(index + 1).getIndentLevel() > baseIndent) {
                            list.remove(list.get(index + 1));
                        }
                        removeItem.doClick();
                        refreshParentList();
                    }
                }
            });
        }
    }

    @Override
    public void refreshMenu()
    {
        popupMenu.removeAll();
        javax.swing.JMenu moveMenu = new javax.swing.JMenu("Move");
        boolean hasMove = false;
        if (!list.isFirst(item)) {
            moveMenu.add(moveUp);
            hasMove = true;
        }
        if (!list.isLast(item)) {
            moveMenu.add(moveDown);
            hasMove = true;
        }
        if (!list.isFirst(item)) {
            moveMenu.add(moveToTop);
            hasMove = true;
        }
        if (!list.isLast(item)) {
            moveMenu.add(moveToBottom);
            hasMove = true;
        }
        if (hasMove) {
            popupMenu.add(moveMenu);
        }

        var previousItem = list.getPreviousItem(item);

        if (item.isNotFullyIndented() && previousItem != null && previousItem.getIndentLevel() >= item.getIndentLevel()) {
            popupMenu.add(indentItem);
        }

        if (item.isIndented()) {
            popupMenu.add(unindentItem);
        }

        String toggleLabel = "Mark as " + (item.getStatus() == Status.COMPLETED ? "Incomplete" : "Completed");
        JMenuItem toggleStatusItem = new JMenuItem(toggleLabel);
        toggleStatusItem.addActionListener(e -> {
            Status newStatus = (item.getStatus() == Status.COMPLETED ? Status.NOT_STARTED : Status.COMPLETED);
            item.setStatus(newStatus);

            // Cascade status change to all indented children
            int index = list.indexOf(item);
            int baseIndent = item.getIndentLevel();
            for (int i = index + 1; i < list.size(); i++) {
                Task child = list.get(i);
                if (child.getIndentLevel() <= baseIndent) {
                    break; // stop at siblings or parents
                }
                child.setStatus(newStatus);
            }

            if (taskContent != null) {
                taskContent.refresh();
            }
            // Refresh the entire list panel so UI updates immediately
            refreshParentList();
        });
        popupMenu.add(toggleStatusItem);

        // Add quest pre-reqs menu item only if the quest actually has prereqs
        if (item instanceof com.toofifty.goaltracker.models.task.QuestTask) {
            com.toofifty.goaltracker.models.task.QuestTask questTask = (com.toofifty.goaltracker.models.task.QuestTask) item;
            int baseIndent = item.getIndentLevel();
            // Gather existing direct/descendant children under this quest to avoid duplicates
            java.util.Set<String> existingKeys = new java.util.HashSet<>();
            int parentIndex = list.indexOf(item);
            for (int i = parentIndex + 1; i < list.size(); i++) {
                var child = list.get(i);
                if (child.getIndentLevel() <= baseIndent) {
                    break; // stop at siblings/parents
                }
                existingKeys.add(child.getClass().getName() + "|" + child.toString());
            }
            var rawPrereqs = QuestRequirements.getRequirements(questTask.getQuest(), baseIndent + 1);
            java.util.List<com.toofifty.goaltracker.models.task.Task> missingPrereqs = new java.util.ArrayList<>();
            if (rawPrereqs != null) {
                for (com.toofifty.goaltracker.models.task.Task p : rawPrereqs) {
                    String key = p.getClass().getName() + "|" + p.toString();
                    if (!existingKeys.contains(key)) {
                        missingPrereqs.add(p);
                    }
                }
            }
            if (!missingPrereqs.isEmpty()) {
                JMenuItem prereqItem = new JMenuItem("Add pre-reqs");
                prereqItem.addActionListener(e -> {
                    // Recompute and filter again at click time
                    var raw = QuestRequirements.getRequirements(questTask.getQuest(), baseIndent + 1);
                    if (raw != null) {
                        // Refresh existing keys in case the list changed since menu was built
                        java.util.Set<String> currentKeys = new java.util.HashSet<>();
                        int pIndex = list.indexOf(item);
                        for (int i = pIndex + 1; i < list.size(); i++) {
                            var child = list.get(i);
                            if (child.getIndentLevel() <= baseIndent) break;
                            currentKeys.add(child.getClass().getName() + "|" + child.toString());
                        }
                        java.util.List<com.toofifty.goaltracker.models.task.Task> filtered = new java.util.ArrayList<>();
                        for (com.toofifty.goaltracker.models.task.Task t : raw) {
                            String key = t.getClass().getName() + "|" + t.toString();
                            if (!currentKeys.contains(key)) {
                                filtered.add(t);
                            }
                        }
                        if (!filtered.isEmpty()) {
                            int index = list.indexOf(item);
                            for (com.toofifty.goaltracker.models.task.Task prereq : filtered) {
                                list.add(index + 1, prereq);
                                index++;
                            }
                            refreshParentList();
                        }
                    }
                });
                popupMenu.add(prereqItem);
            }
        }

        removeItem.setText("Remove (Shift+Left Click)");
        popupMenu.add(removeItem);
    }

    public void onIndented(Consumer<Task> indentedListener) {
        this.indentedListener = indentedListener;
    }

    public void onUnindented(Consumer<Task> unindentedListener) {
        this.unindentedListener = unindentedListener;
    }

    public void setTaskContent(TaskItemContent taskContent) {
        this.taskContent = taskContent;
    }

    private void refreshParentList()
    {
        Container parent = SwingUtilities.getAncestorOfClass(ListPanel.class, this);
        if (parent instanceof ListPanel) {
            ((ListPanel<?>) parent).tryBuildList();
            ((ListPanel<?>) parent).refresh();
        } else {
            // Fallback
            revalidate();
            repaint();
        }
    }
}
