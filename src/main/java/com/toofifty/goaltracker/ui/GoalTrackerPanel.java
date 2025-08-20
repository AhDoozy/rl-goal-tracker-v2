package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalManager;
import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.UndoStack;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.ui.components.ActionBar;
import com.toofifty.goaltracker.ui.components.ActionBarButton;
import com.toofifty.goaltracker.ui.components.ListItemPanel;
import com.toofifty.goaltracker.ui.components.ListPanel;
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
    private ActionBarButton undoButtonRef;
    private ActionBarButton redoButtonRef;
    private GoalPanel goalPanel;
    private Consumer<Goal> goalAddedListener;
    private Consumer<Goal> goalUpdatedListener;
    private Consumer<Task> taskAddedListener;
    private Consumer<Task> taskUpdatedListener;
    private Goal pendingNewGoal;

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

        // Action bar (shared style)
        ActionBar actionBar = new ActionBar();

        // Left-side actions
        ActionBarButton addGoalBtn = new ActionBarButton("+ Add goal", () ->
        {
            Goal goal = goalManager.createGoal();
            pendingNewGoal = goal;
            view(goal);
            if (goalAddedListener != null) goalAddedListener.accept(goal);
            if (goalUpdatedListener != null) goalUpdatedListener.accept(goal);
        });
        ActionBarButton moveBtn = new ActionBarButton("Move", () -> {});
        moveBtn.setEnabled(false);
        moveBtn.setToolTipText("Coming soon");

        ActionBarButton bulkEditBtn = new ActionBarButton("Bulk Edit", () -> {});
        bulkEditBtn.setEnabled(false);
        bulkEditBtn.setToolTipText("Coming soon");

        actionBar.left().add(addGoalBtn);
        actionBar.left().add(moveBtn);
        actionBar.left().add(bulkEditBtn);

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
        headerContainer.add(titlePanel, BorderLayout.NORTH);
        headerContainer.add(actionBar, BorderLayout.SOUTH   );

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
        // Auto-remove an empty goal created via "+ Add goal" if user backs out without adding tasks
        if (pendingNewGoal != null)
        {
            try {
                if (pendingNewGoal.getTasks() == null || pendingNewGoal.getTasks().isEmpty()) {
                    goalManager.getGoals().remove(pendingNewGoal);
                }
            } finally {
                pendingNewGoal = null;
            }
        }
        // Clear the GoalTrackerPanel content and switch back to the main panel
        removeAll();

        // Rebuild the list BEFORE attaching the main panel to ensure layout has components
        goalListPanel.tryBuildList();
        goalListPanel.refresh();

        // Make sure the list is the CENTER of the main panel (in case layout got disturbed)
        mainPanel.remove(goalListPanel);
        mainPanel.add(goalListPanel, BorderLayout.CENTER);

        // Attach main panel and validate
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

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
