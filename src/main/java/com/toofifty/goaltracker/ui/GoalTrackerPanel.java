package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalManager;
import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.UndoStack;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.utils.ReorderableList;
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

        JLabel title = new JLabel("Goal Tracker v2");
        title.setForeground(Color.WHITE);
        title.setFont(FontManager.getRunescapeBoldFont());

        JLabel author = new JLabel("By: AhDoozy");
        author.setForeground(Color.LIGHT_GRAY);
        author.setFont(title.getFont().deriveFont(title.getFont().getSize2D() - 3f));

        JPanel titleTextPanel = new JPanel(new GridLayout(2, 1));
        titleTextPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        titleTextPanel.add(title);
        titleTextPanel.add(author);

        titlePanel.add(titleTextPanel, BorderLayout.WEST);

        // Re-add "+ Add goal" to the header (right side)
        JPanel addGoalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        addGoalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        ActionBarButton addGoalBtn = new ActionBarButton("+ Add goal", this::addNewGoal);
        addGoalPanel.add(addGoalBtn);
        titlePanel.add(addGoalPanel, BorderLayout.EAST);

        // Action bar (shared style)
        ActionBar actionBar = new ActionBar();
        actionBar.right().setBorder(new EmptyBorder(0, 4, 0, 0));

        // Right-side actions: Undo/Redo
        undoButtonRef = new ActionBarButton("Undo", this::doUndo);
        redoButtonRef = new ActionBarButton("Redo", this::doRedo);
        actionBar.left().add(undoButtonRef);
        actionBar.left().add(redoButtonRef);

        ActionBarButton exportButton = new ActionBarButton("Export", () -> {
            // TODO: implement export
        });
        ActionBarButton importButton = new ActionBarButton("Import", () -> {
            // TODO: implement import
        });
        actionBar.right().add(exportButton);
        actionBar.right().add(importButton);

        updateUndoRedoButtons();

        // Wrap the title panel with a subtle bottom separator for visual polish
        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
        titleWrapper.add(titlePanel, BorderLayout.CENTER);

        // 1px divider under the header title
        JPanel headerSeparator = new JPanel();
        headerSeparator.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        headerSeparator.setPreferredSize(new Dimension(1, 4));
        titleWrapper.add(headerSeparator, BorderLayout.SOUTH);

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
        headerContainer.add(titleWrapper, BorderLayout.NORTH);
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
        goalListPanel.setPlaceholder("<html><div style='text-align:center;color:#bfbfbf;padding:8px 0;'>No goals yet.<br/>Click <b>+ Add goal</b> above to create your first one.</div></html>");

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
    private void addNewGoal()
    {
        Goal goal = Goal.builder().tasks(ReorderableList.from()).build();
        goalManager.getGoals().add(0, goal);
        pendingNewGoal = goal;
        view(goal);
    }
}
