package com.toofifty.goaltracker.ui.components;

import com.toofifty.goaltracker.ui.TaskItemContent;

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

                System.out.println(String.format("%s >= %s", item.getIndentLevel(), child.getIndentLevel()));
                // If a child is less indented then this item assume its a parent node and break
                if (item.getIndentLevel() >= child.getIndentLevel()) break;

                child.indent();
            }

            item.indent();
            this.indentedListener.accept(item);
            refreshParentList();
        });

        unindentItem.addActionListener(e -> {
            // Unindent all of the items children
            var index = list.indexOf(item);
            for (int i = index + 1; i < list.size(); i++) {
                var child = list.get(i);

                System.out.println(String.format("%s >= %s", item.getIndentLevel(), child.getIndentLevel()));
                // If a child is less indented then this item assume its a parent node and break
                if (item.getIndentLevel() >= child.getIndentLevel()) break;

                child.unindent();;
            }

            item.unindent();
            this.unindentedListener.accept(item);
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
        if (!list.isFirst(item)) {
            popupMenu.add(moveUp);
        }
        if (!list.isLast(item)) {
            popupMenu.add(moveDown);
        }
        if (!list.isFirst(item)) {
            popupMenu.add(moveToTop);
        }
        if (!list.isLast(item)) {
            popupMenu.add(moveToBottom);
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
