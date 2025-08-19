package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.services.TaskIconService;
import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.utils.QuestRequirements;
import java.util.List;

import com.toofifty.goaltracker.ui.components.ListPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.toofifty.goaltracker.utils.Constants.STATUS_TO_COLOR;

import com.toofifty.goaltracker.models.ActionHistory;
import com.toofifty.goaltracker.models.ToggleCompleteAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        titleLabel.addMouseListener(new MouseAdapter()
        {
            private void showMenu(MouseEvent e)
            {
                if (!e.isPopupTrigger()) return;

                boolean currentlyComplete = "COMPLETED".equals(task.getStatus().toString());
                String label = currentlyComplete ? "Mark as Incomplete" : "Mark as Completed";

                JPopupMenu menu = new JPopupMenu();
                JMenuItem toggle = new JMenuItem(label);
                toggle.addActionListener(a -> {
                    ToggleCompleteAction act = new ToggleCompleteAction(task, currentlyComplete, !currentlyComplete);
                    act.redo(); // apply immediately
                    if (actionHistory != null)
                    {
                        actionHistory.push(act); // record for undo/redo
                    }
                    plugin.getUiStatusManager().refresh(goal);
                });
                menu.add(toggle);
                menu.show(titleLabel, e.getX(), e.getY());
            }

            @Override public void mousePressed(MouseEvent e) { showMenu(e); }
            @Override public void mouseReleased(MouseEvent e) { showMenu(e); }
        });
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
