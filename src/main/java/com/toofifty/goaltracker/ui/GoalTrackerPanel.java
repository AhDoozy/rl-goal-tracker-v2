package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalManager;
import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.UndoStack;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.ui.components.ListItemPanel;
import com.toofifty.goaltracker.ui.components.ListPanel;
import com.toofifty.goaltracker.ui.components.TextButton;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

@Singleton
public class GoalTrackerPanel extends PluginPanel implements Refreshable
{
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final ListPanel<Goal> goalListPanel;
    private final GoalTrackerPlugin plugin;
    private final GoalManager goalManager;
    private final UndoStack<Goal> undoStack = new UndoStack<>();
    private TextButton undoButtonRef;
    private TextButton redoButtonRef;
    private GoalPanel goalPanel;
    private Consumer<Goal> goalAddedListener;
    private Consumer<Goal> goalUpdatedListener;
    private Consumer<Task> taskAddedListener;
    private Consumer<Task> taskUpdatedListener;

    @Inject
    public GoalTrackerPanel(GoalTrackerPlugin plugin, GoalManager goalManager)
    {
        super(false);
        this.plugin = plugin;
        this.goalManager = goalManager;

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        // (Removed "+ Add goal" button from the title panel)

        JLabel title = new JLabel();
        title.setText("Goal Tracker");
        title.setForeground(Color.WHITE);
        title.setFont(FontManager.getRunescapeBoldFont());
        titlePanel.add(title, BorderLayout.WEST);

        // New action bar below the title
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBorder(new EmptyBorder(6, 10, 6, 10));
        actionBar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel actionsLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        actionsLeft.setOpaque(true);
        actionsLeft.setBackground(ColorScheme.DARK_GRAY_COLOR);

        TextButton addGoalBtn = new TextButton("+ Add goal",
            e -> {
                Goal goal = goalManager.createGoal();
                view(goal);

                if (Objects.nonNull(this.goalAddedListener)) this.goalAddedListener.accept(goal);
                if (Objects.nonNull(this.goalUpdatedListener)) this.goalUpdatedListener.accept(goal);
            }
        ).narrow();

        TextButton moveBtn = new TextButton("Move", e -> {}).narrow();
        moveBtn.setEnabled(false);
        moveBtn.setToolTipText("Coming soon");

        TextButton bulkEditBtn = new TextButton("Bulk Edit", e -> {}).narrow();
        bulkEditBtn.setEnabled(false);
        bulkEditBtn.setToolTipText("Coming soon");

        actionsLeft.add(addGoalBtn);
        actionsLeft.add(moveBtn);
        actionsLeft.add(bulkEditBtn);
        actionBar.add(actionsLeft, BorderLayout.WEST);

        // Stack title and action bar into a single header container
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
        headerContainer.add(titlePanel, BorderLayout.NORTH);
        headerContainer.add(actionBar, BorderLayout.SOUTH);

        goalListPanel = new ListPanel<>(goalManager.getGoals(),
            (goal) -> {
                var panel = new ListItemPanel<>(goalManager.getGoals(), goal);

                panel.onClick(e -> this.view(goal));
                panel.add(new GoalItemContent(plugin, goal));
                panel.onRemovedWithIndex((removedGoal, index) -> {
                    recordGoalRemoval(removedGoal, index);
                });

                return panel;
            }
        );
        goalListPanel.setGap(0);
        goalListPanel.setPlaceholder("Add a new goal using the button above");

        mainPanel.add(headerContainer, BorderLayout.NORTH);
        mainPanel.add(goalListPanel, BorderLayout.CENTER);

        home();
    }

    public void view(Goal goal)
    {
        removeAll();

        this.goalPanel = new GoalPanel(plugin, goal, this::home);

        this.goalPanel.onGoalUpdated(this.goalUpdatedListener);
        this.goalPanel.onTaskAdded(this.taskAddedListener);
        this.goalPanel.onTaskUpdated(this.taskUpdatedListener);

        add(this.goalPanel, BorderLayout.CENTER);
        this.goalPanel.refresh();

        revalidate();
        repaint();
    }

    public void home()
    {
        removeAll();
        add(mainPanel, BorderLayout.CENTER);
        goalListPanel.tryBuildList();
        goalListPanel.refresh();

        revalidate();
        repaint();

        this.goalPanel = null;
    }

    @Override
    public void refresh()
    {
        // refresh single-view goal
        for (Component component : getComponents()) {
            if (component instanceof Refreshable) {
                ((Refreshable) component).refresh();
            }
        }

        goalListPanel.refresh();
    }

    public void onGoalUpdated(Consumer<Goal> listener)
    {
        this.goalUpdatedListener = listener;

        this.goalListPanel.onUpdated(this.goalUpdatedListener);

        if (this.goalPanel != null) {
            this.goalPanel.onGoalUpdated(this.goalUpdatedListener);
        }
    }

    public void onTaskUpdated(Consumer<Task> listener)
    {
        this.taskUpdatedListener = listener;

        if (this.goalPanel != null) {
            this.goalPanel.onTaskUpdated(this.taskUpdatedListener);
        }
    }

    public void onTaskAdded(Consumer<Task> listener)
    {
        this.taskAddedListener = listener;

        if (this.goalPanel != null) {
            this.goalPanel.onTaskAdded(this.taskAddedListener);
        }
    }

    private void updateUndoRedoButtons()
    {
        if (undoButtonRef != null)
        {
            undoButtonRef.setEnabled(undoStack.hasUndo());
            undoButtonRef.setToolTipText(undoStack.hasUndo() ? null : "Nothing to undo");
        }
        if (redoButtonRef != null)
        {
            redoButtonRef.setEnabled(undoStack.hasRedo());
            redoButtonRef.setToolTipText(undoStack.hasRedo() ? null : "Nothing to redo");
        }
    }

    private void doUndo()
    {
        var entry = undoStack.popForUndo();
        if (entry == null) { updateUndoRedoButtons(); return; }

        java.util.List<Goal> goals = goalManager.getGoals();
        int idx = Math.max(0, Math.min(entry.getIndex(), goals.size()));
        goals.add(idx, entry.getItem());

        // If we are on the home view, refresh the list; otherwise leave as-is.
        if (goalPanel == null)
        {
            goalListPanel.tryBuildList();
            goalListPanel.refresh();
            revalidate();
            repaint();
        }
        updateUndoRedoButtons();
    }

    private void doRedo()
    {
        var entry = undoStack.popForRedo();
        if (entry == null) { updateUndoRedoButtons(); return; }

        java.util.List<Goal> goals = goalManager.getGoals();
        int idx = goals.indexOf(entry.getItem());
        if (idx >= 0)
        {
            goals.remove(idx);
        }
        // If on home view, refresh
        if (goalPanel == null)
        {
            goalListPanel.tryBuildList();
            goalListPanel.refresh();
            revalidate();
            repaint();
        }
        updateUndoRedoButtons();
    }

    /**
     * Call this when a goal is removed from the home list to record it for Undo.
     * @param goal the goal that was removed
     * @param index the index it had before removal
     */
    public void recordGoalRemoval(Goal goal, int index)
    {
        undoStack.pushRemove(goal, index);
        updateUndoRedoButtons();
    }
}
