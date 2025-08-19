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

    TaskItemContent(GoalTrackerPlugin plugin, Goal goal, Task task)
    {
        super(new BorderLayout());
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
