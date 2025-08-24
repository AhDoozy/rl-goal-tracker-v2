package com.ahdoozy.goaltrackerv2.ui;

import com.ahdoozy.goaltrackerv2.GoalManager;
import com.ahdoozy.goaltrackerv2.GoalTrackerV2Plugin;
import com.ahdoozy.goaltrackerv2.models.Goal;
import com.ahdoozy.goaltrackerv2.models.UndoStack;
import com.ahdoozy.goaltrackerv2.presets.GoalPresetRepository;
import com.ahdoozy.goaltrackerv2.presets.GoalPresetRepository.Preset;
import com.ahdoozy.goaltrackerv2.models.task.Task;
import com.ahdoozy.goaltrackerv2.utils.ReorderableList;
import com.ahdoozy.goaltrackerv2.ui.components.ActionBar;
import com.ahdoozy.goaltrackerv2.ui.components.ActionBarButton;
import com.ahdoozy.goaltrackerv2.ui.components.ListItemPanel;
import com.ahdoozy.goaltrackerv2.ui.components.ListPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.function.Consumer;
import java.util.Collections;
import java.util.Comparator;

public class GoalTrackerPanel extends PluginPanel implements Refreshable
{
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final ListPanel<Goal> goalListPanel;
    private final GoalTrackerV2Plugin plugin;
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
    public GoalTrackerPanel(GoalTrackerV2Plugin plugin, GoalManager goalManager)
    {
        super(false);
        this.plugin = plugin;
        this.goalManager = goalManager;
        this.goalManager.addGoalsChangedListener(() -> javax.swing.SwingUtilities.invokeLater(this::refreshHomeListIfVisible));

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));

        // Header with title and + Add goal on right
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

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

        JPanel addGoalPanel = new JPanel();
        addGoalPanel.setLayout(new BoxLayout(addGoalPanel, BoxLayout.Y_AXIS));
        addGoalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        JPanel addGoalRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        addGoalRow.setBackground(ColorScheme.DARK_GRAY_COLOR);
        ActionBarButton addGoalBtn = new ActionBarButton("+ Add goal", this::addNewGoal);
        addGoalRow.add(addGoalBtn);
        addGoalPanel.add(addGoalRow);
        addGoalPanel.add(Box.createVerticalStrut(4));
        ActionBarButton addFromPresetBtn = new ActionBarButton("Add from Preset…", this::addFromPreset);
        addGoalPanel.add(addFromPresetBtn);
        titlePanel.add(addGoalPanel, BorderLayout.EAST);

        // Action bar
        ActionBar actionBar = new ActionBar();
        actionBar.right().setBorder(new EmptyBorder(0, 4, 0, 0));

        undoButtonRef = new ActionBarButton("Undo", this::doUndo);
        redoButtonRef = new ActionBarButton("Redo", this::doRedo);
        actionBar.left().add(undoButtonRef);
        actionBar.left().add(redoButtonRef);

        ActionBarButton exportButton = new ActionBarButton("Export", this::exportGoalsToFile);
        ActionBarButton importButton = new ActionBarButton("Import", this::importGoalsFromFile);
        actionBar.right().add(exportButton);
        actionBar.right().add(importButton);

        updateUndoRedoButtons();

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
        titleWrapper.add(titlePanel, BorderLayout.CENTER);

        JPanel headerSeparator = new JPanel();
        headerSeparator.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        headerSeparator.setPreferredSize(new Dimension(1, 4));
        titleWrapper.add(headerSeparator, BorderLayout.SOUTH);

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
        headerContainer.add(titleWrapper, BorderLayout.NORTH);
        headerContainer.add(actionBar, BorderLayout.SOUTH);

        goalListPanel = new ListPanel<>(goalManager.getGoals(), (goal) -> {
            var panel = new ListItemPanel<>(goalManager.getGoals(), goal);
            panel.onClick(e -> this.view(goal));
            panel.add(new GoalItemContent(plugin, goal));
            panel.onRemovedWithIndex(this::recordGoalRemoval);
            return panel;
        });
        goalListPanel.setGap(0);
        goalListPanel.setPlaceholder("<html><div style='text-align:center;color:#bfbfbf;padding:8px 0;'>No goals yet.<br/>Click <b>+ Add goal</b> above to create your first one.</div></html>");

        mainPanel.add(headerContainer, BorderLayout.NORTH);
        mainPanel.add(goalListPanel, BorderLayout.CENTER);

        home();
    }

    private void refreshHomeListIfVisible()
    {
        if (goalPanel == null)
        {
            sortGoalsForHome();
            goalListPanel.tryBuildList();
            goalListPanel.refresh();
            revalidate();
            repaint();
        }
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
        removeAll();
        sortGoalsForHome();
        goalListPanel.tryBuildList();
        goalListPanel.refresh();
        mainPanel.remove(goalListPanel);
        mainPanel.add(goalListPanel, BorderLayout.CENTER);
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

    private void sortGoalsForHome()
    {
        java.util.List<Goal> goals = goalManager.getGoals();
        Collections.sort(goals, Comparator
                .comparing(Goal::isPinned).reversed()
                .thenComparing(g -> {
                    String d = g.getDescription();
                    return d == null ? "" : d.toLowerCase();
                }));
    }

    private void doUndo()
    {
        var entry = undoStack.popForUndo();
        if (entry == null) { updateUndoRedoButtons(); return; }
        java.util.List<Goal> goals = goalManager.getGoals();
        int idx = Math.max(0, Math.min(entry.getIndex(), goals.size()));
        goals.add(idx, entry.getItem());
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
        if (goalPanel == null)
        {
            goalListPanel.tryBuildList();
            goalListPanel.refresh();
            revalidate();
            repaint();
        }
        updateUndoRedoButtons();
    }

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

    private void exportGoalsToFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileNameExtensionFilter("JSON files (*.json)", "json"));
        chooser.setDialogTitle("Export goals to JSON");
        chooser.setSelectedFile(new java.io.File("goals.json"));
        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) { return; }
        java.io.File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".json"))
        {
            file = new java.io.File(file.getParentFile(), file.getName() + ".json");
        }
        try (java.io.FileWriter fw = new java.io.FileWriter(file))
        {
            String json = goalManager.exportJson(true);
            fw.write(json);
            fw.flush();
            JOptionPane.showMessageDialog(this, "Exported " + goalManager.getGoals().size() + " goal(s) to\n" + file.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Failed to export goals: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importGoalsFromFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileNameExtensionFilter("JSON files (*.json)", "json"));
        chooser.setDialogTitle("Import goals from JSON");
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) { return; }
        java.io.File file = chooser.getSelectedFile();
        try
        {
            String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            goalManager.importJson(json);
            plugin.warmItemIcons();
            if (goalPanel != null) {
                home();
            } else {
                goalListPanel.tryBuildList();
                goalListPanel.refresh();
                revalidate();
                repaint();
            }
            JOptionPane.showMessageDialog(this, "Imported goals from\n" + file.getAbsolutePath(), "Import Complete", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Failed to import goals: " + ex.getMessage(), "Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFromPreset()
    {
        java.util.List<Preset> options = GoalPresetRepository.getAll();

        JComboBox<Preset> combo = new JComboBox<>(options.toArray(new Preset[0]));
        JTextArea details = new JTextArea(6, 36);
        details.setWrapStyleWord(true);
        details.setLineWrap(true);
        details.setEditable(false);
        details.setBackground(new JPanel().getBackground());

        java.util.function.Consumer<Preset> update = (opt) -> {
            if (opt == null) { details.setText(""); return; }
            int goals = opt.getGoals().size();
            int tasks = opt.getGoals().stream().mapToInt(g -> g.getTasks().size()).sum();
            details.setText(opt.getDescription() + "\n\nThis will add " + goals + " goal(s) with " + tasks + " task(s).");
        };
        combo.addActionListener(e -> update.accept((Preset) combo.getSelectedItem()));
        update.accept((Preset) combo.getSelectedItem());

        JPanel panel = new JPanel(new BorderLayout(6,6));
        panel.add(new JLabel("Choose a preset:"), BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);
        panel.add(new JScrollPane(details), BorderLayout.SOUTH);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add from Preset…", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) { return; }

        Preset selected = (Preset) combo.getSelectedItem();
        if (selected == null) { return; }

        goalManager.addGoals(selected.getGoals());

        if (goalPanel != null) {
            home();
        } else {
            goalListPanel.tryBuildList();
            goalListPanel.refresh();
            revalidate();
            repaint();
        }
    }
}