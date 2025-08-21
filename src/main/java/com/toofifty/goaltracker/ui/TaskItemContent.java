package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.services.TaskIconService;
import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.utils.QuestRequirements;
import java.util.List;

import com.toofifty.goaltracker.ui.components.ListPanel;
import com.toofifty.goaltracker.ui.components.ListItemPanel;
import javax.swing.SwingUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.toofifty.goaltracker.utils.Constants.STATUS_TO_COLOR;

import com.toofifty.goaltracker.models.ActionHistory;
import com.toofifty.goaltracker.models.ToggleCompleteAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toofifty.goaltracker.models.enums.Status;

public class TaskItemContent extends JPanel implements Refreshable
{
    private final Task task;
    private final Goal goal;
    private final TaskIconService iconService;
    private final JLabel titleLabel = new JLabel();
    private final JLabel iconLabel = new JLabel();

    private final GoalTrackerPlugin plugin;
    private ActionHistory actionHistory;

    TaskItemContent(GoalTrackerPlugin plugin, Goal goal, Task task)
    {
        super(new BorderLayout());
        this.plugin = plugin;
        this.task = task;
        this.goal = goal;
        iconService = plugin.getTaskIconService();

        titleLabel.setPreferredSize(new Dimension(0, 24));
        add(titleLabel, BorderLayout.CENTER);

        JPanel iconWrapper = new JPanel(new BorderLayout());
        iconWrapper.setBorder(new EmptyBorder(4, 0, 0, 4));
        iconWrapper.add(iconLabel, BorderLayout.NORTH);
        add(iconWrapper, BorderLayout.WEST);

        plugin.getUiStatusManager().addRefresher(task, this::refresh);

        // Right-click to toggle completion with ActionHistory
        MouseAdapter contextMenuListener = new MouseAdapter()
        {
            private void showMenuIfNeeded(MouseEvent e)
            {
                if (!(e.isPopupTrigger() || javax.swing.SwingUtilities.isRightMouseButton(e)))
                {
                    return;
                }
                // Prefer the parent ListItemPanel context menu (move up/down/remove, etc.)
                Component src = (Component) e.getSource();
                JComponent listItem = (JComponent) SwingUtilities.getAncestorOfClass(ListItemPanel.class, src);
                if (listItem != null && listItem.getComponentPopupMenu() != null)
                {
                    Point p = SwingUtilities.convertPoint(src, e.getPoint(), listItem);
                    listItem.getComponentPopupMenu().show(listItem, p.x, p.y);
                    return;
                }

                // Fallback: show simple toggle menu if no parent popup menu is available
                boolean currentlyComplete = task.getStatus() == Status.COMPLETED;
                String label = currentlyComplete ? "Mark as Incomplete" : "Mark as Completed";

                JPopupMenu menu = new JPopupMenu();
                JMenuItem toggle = new JMenuItem(label);
                toggle.addActionListener(a -> {
                    ToggleCompleteAction act = new ToggleCompleteAction(task, currentlyComplete, !currentlyComplete);
                    act.redo();
                    if (actionHistory != null)
                    {
                        actionHistory.push(act);
                    }
                    plugin.getUiStatusManager().refresh(goal);
                });
                menu.add(toggle);
                Component invoker = (Component) e.getSource();
                menu.show(invoker, e.getX(), e.getY());
            }

            @Override public void mousePressed(MouseEvent e) { showMenuIfNeeded(e); }
            @Override public void mouseReleased(MouseEvent e) { showMenuIfNeeded(e); }
        };

        // Attach listener to multiple components to make right-click reliable across platforms
        this.addMouseListener(contextMenuListener);
        titleLabel.addMouseListener(contextMenuListener);
        iconLabel.addMouseListener(contextMenuListener);
    }

    public void setActionHistory(ActionHistory history)
    {
        this.actionHistory = history;
    }

    @Override
    public void refresh()
    {
        titleLabel.setText(task.toString());
        titleLabel.setForeground(STATUS_TO_COLOR.get(task.getStatus()));

        int indent = 16 * task.getIndentLevel();
        iconLabel.setIcon(iconService.get(task));
        iconLabel.setBorder(new EmptyBorder(0, indent, 0, 0));

        revalidate();
    }

    @Override
    public void setBackground(Color bg)
    {
        super.setBackground(bg);
        for (Component component : getComponents()) {
            component.setBackground(bg);
        }
    }
}
