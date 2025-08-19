package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.enums.TaskType;
import com.toofifty.goaltracker.models.enums.Status;
import com.toofifty.goaltracker.models.task.ManualTask;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.ui.components.EditableInput;
import com.toofifty.goaltracker.ui.components.ListPanel;
import com.toofifty.goaltracker.ui.components.ListTaskPanel;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public class GoalPanel extends JPanel implements Refreshable
{
    private final GoalTrackerPlugin plugin;
    private final Goal goal;

    private final EditableInput descriptionInput;
    private final ListPanel<Task> taskListPanel;
    private Consumer<Goal> goalUpdatedListener;
    private Consumer<Task> taskAddedListener;
    private Consumer<Task> taskUpdatedListener;

    GoalPanel(GoalTrackerPlugin plugin, Goal goal, Runnable closeListener)
    {
        super();
        this.plugin = plugin;
        this.goal = goal;

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
        add(headerPanel, BorderLayout.NORTH);

        descriptionInput = new EditableInput((value) -> {
            goal.setDescription(value);
            this.goalUpdatedListener.accept(goal);
        });
        headerPanel.add(descriptionInput, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> installClipboardSupport(descriptionInput));
        descriptionInput.addContainerListener(new java.awt.event.ContainerAdapter()
        {
            @Override public void componentAdded(java.awt.event.ContainerEvent e)
            {
                SwingUtilities.invokeLater(() -> installClipboardSupport(descriptionInput));
            }
        });

        taskListPanel = new ListPanel<>(goal.getTasks(), (task) -> {
            ListTaskPanel taskPanel = new ListTaskPanel(goal.getTasks(), task);
            TaskItemContent taskContent = new TaskItemContent(plugin, goal, task);
            taskPanel.add(taskContent);
            taskPanel.setTaskContent(taskContent);
            taskContent.refresh();
            taskPanel.setBorder(new EmptyBorder(2, 4, 2, 4));


            taskPanel.onIndented(e -> {
                this.goalUpdatedListener.accept(goal);
                plugin.getUiStatusManager().refresh(goal);
                this.refresh();
            });

            taskPanel.onUnindented(e -> {
                this.goalUpdatedListener.accept(goal);
                plugin.getUiStatusManager().refresh(goal);
                this.refresh();
            });

            return taskPanel;
        });
        taskListPanel.setGap(0);
        taskListPanel.setPlaceholder("No tasks added yet");
        add(taskListPanel, BorderLayout.CENTER);

        NewTaskPanel newTaskPanel = new NewTaskPanel(plugin, goal);
        newTaskPanel.onTaskAdded(this::updateFromNewTask);
        add(newTaskPanel, BorderLayout.SOUTH);
    }

    public void updateFromNewTask(Task task)
    {
        taskListPanel.tryBuildList();
        taskListPanel.refresh();
        plugin.setValidateAll(true);
        plugin.getUiStatusManager().refresh(goal);
        revalidate();
        repaint();

        if (Objects.nonNull(this.taskAddedListener)) this.taskAddedListener.accept(task);
        if (Objects.nonNull(this.taskUpdatedListener)) this.taskUpdatedListener.accept(task);
    }

    public void refreshTaskList()
    {
        taskListPanel.tryBuildList();
        taskListPanel.refresh();
        plugin.getUiStatusManager().refresh(goal);
        revalidate();
        repaint();
    }

    @Override
    public void refresh()
    {
        descriptionInput.setValue(goal.getDescription());
        taskListPanel.refresh();
    }

    public void onGoalUpdated(Consumer<Goal> listener)
    {
        this.goalUpdatedListener = listener;
    }

    public void onTaskAdded(Consumer<Task> listener)
    {
        this.taskAddedListener = listener;

        taskListPanel.onUpdated(this.taskAddedListener);
    }

    public void onTaskUpdated(Consumer<Task> listener)
    {
        this.taskUpdatedListener = listener;

        taskListPanel.onUpdated(this.taskUpdatedListener);
    }
    private void installClipboardSupport(Component root)
    {
        JTextComponent tc = findTextComponent(root);
        if (tc == null)
        {
            return;
        }

        // Ensure transfer handler to enable clipboard operations on text property
        tc.setTransferHandler(new TransferHandler("text"));
        tc.setDragEnabled(true);

        // Context menu with Cut/Copy/Paste/Select All
        JPopupMenu menu = new JPopupMenu();
        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> tc.selectAll());
        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.addSeparator();
        menu.add(selectAll);
        tc.setComponentPopupMenu(menu);

        // Keyboard shortcuts (Ctrl/Cmd + C/V/X/A)
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        InputMap im = tc.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = tc.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, mask), DefaultEditorKit.copyAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, mask), DefaultEditorKit.pasteAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, mask), DefaultEditorKit.cutAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, mask), DefaultEditorKit.selectAllAction);
        am.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        am.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        am.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        am.put(DefaultEditorKit.selectAllAction, new AbstractAction()
        {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { tc.selectAll(); }
        });
    }

    private JTextComponent findTextComponent(Component c)
    {
        if (c instanceof JTextComponent)
        {
            return (JTextComponent) c;
        }
        if (c instanceof Container)
        {
            for (Component child : ((Container) c).getComponents())
            {
                JTextComponent tc = findTextComponent(child);
                if (tc != null)
                {
                    return tc;
                }
            }
        }
        return null;
    }
}
