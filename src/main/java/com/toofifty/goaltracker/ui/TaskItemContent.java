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

public class TaskItemContent extends JPanel implements Refreshable
{
    private final Task task;
    private final Goal goal;
    private final TaskIconService iconService;
    private final JLabel titleLabel = new JLabel();
    private final JLabel iconLabel = new JLabel();
    private final JButton prereqButton = new JButton("Add prereqs");

    TaskItemContent(GoalTrackerPlugin plugin, Goal goal, Task task)
    {
        super(new BorderLayout());
        this.task = task;
        this.goal = goal;
        iconService = plugin.getTaskIconService();

        titleLabel.setPreferredSize(new Dimension(0, 24));
        add(titleLabel, BorderLayout.CENTER);

        if (task instanceof QuestTask) {
            prereqButton.setMargin(new Insets(1, 3, 1, 3));
            prereqButton.setFont(prereqButton.getFont().deriveFont(prereqButton.getFont().getSize2D() * 0.75f));
            prereqButton.setFocusable(false);
            prereqButton.addActionListener(e -> {
                QuestTask qt = (QuestTask) task;
                List<Task> reqs = QuestRequirements.getRequirements(qt.getQuest(), qt.getIndentLevel());
                // Insert directly after this item
                int insertAt = Math.max(0, goal.getTasks().indexOf(task) + 1);
                goal.getTasks().addAll(insertAt, reqs);
                Container parent = SwingUtilities.getAncestorOfClass(ListPanel.class, TaskItemContent.this);
                if (parent instanceof ListPanel) {
                    ((ListPanel<?>) parent).tryBuildList();
                    ((ListPanel<?>) parent).refresh();
                }
            });
            add(prereqButton, BorderLayout.EAST);
        }

        JPanel iconWrapper = new JPanel(new BorderLayout());
        iconWrapper.setBorder(new EmptyBorder(4, 0, 0, 4));
        iconWrapper.add(iconLabel, BorderLayout.NORTH);
        add(iconWrapper, BorderLayout.WEST);

        plugin.getUiStatusManager().addRefresher(task, this::refresh);
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
