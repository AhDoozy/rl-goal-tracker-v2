package com.ahdoozy.goaltrackerv2.ui.inputs;

import com.ahdoozy.goaltrackerv2.GoalTrackerV2Plugin;
import com.ahdoozy.goaltrackerv2.models.Goal;
import com.ahdoozy.goaltrackerv2.models.task.Task;
import com.ahdoozy.goaltrackerv2.ui.components.TextButton;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import java.util.function.Consumer;
import java.util.Collection;

public abstract class TaskInput extends JPanel
{
    protected final int PREFERRED_INPUT_HEIGHT = 16;
    protected final GoalTrackerV2Plugin plugin;
    private final Goal goal;
    @Getter
    private final JPanel inputRow;
    @Getter
    private Consumer<Task> listener;

    TaskInput(GoalTrackerV2Plugin plugin, Goal goal, String title)
    {
        super(new GridBagLayout());
        this.plugin = plugin;
        this.goal = goal;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 0;
        constraints.ipady = 8;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getRunescapeSmallFont());
        titleLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        titleLabel.setBorder(new EmptyBorder(2, 8, 0, 8));

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.add(titleLabel, BorderLayout.WEST);

        add(titleContainer, constraints);
        constraints.gridy++;

        inputRow = new JPanel(new BorderLayout());
        inputRow.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        if (showAddButton()) {
            TextButton addButton = new TextButton("Add");
            addButton.onClick(e -> submit());
            inputRow.add(addButton, BorderLayout.EAST);
        }
        add(inputRow, constraints);
        constraints.gridy++;
    }

    abstract protected void submit();

    public void addTask(Task task)
    {
        goal.getTasks().add(task);
        if (listener != null) {
            listener.accept(task);
        }
        this.reset();
    }

    public void addTasks(Collection<Task> tasks)
    {
        goal.getTasks().addAll(tasks);
        if (listener != null) {
            for (Task t : tasks) {
                listener.accept(t);
            }
        }
        this.reset();
    }

    abstract protected void reset();

    public TaskInput onSubmit(Consumer<Task> listener)
    {
        this.listener = listener;
        return this;
    }
    protected boolean showAddButton()
    {
        return true;
    }
}