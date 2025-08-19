package com.toofifty.goaltracker.ui.inputs;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.utils.QuestRequirements;
import net.runelite.api.Quest;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class QuestTaskInput extends TaskInput
{
    private final List<Quest> allQuests;
    private Quest bestMatch;
    private final JComboBox<Quest> questDropdown;

    public QuestTaskInput(GoalTrackerPlugin plugin, Goal goal)
    {
        super(plugin, goal, "Quest");

        // Initialize all quests and sort them
        allQuests = Arrays.asList(Quest.values());
        allQuests.sort(Comparator.comparing(Quest::getName));

        // Initialize dropdown with all quests and custom renderer
        questDropdown = new JComboBox<>(allQuests.toArray(new Quest[0]));
        questDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Quest) {
                    String name = ((Quest) value).getName();
                    setText(name);
                }
                return this;
            }
        });
        questDropdown.setSelectedIndex(-1); // no selection initially
        questDropdown.addActionListener(e -> {
            Quest selected = (Quest) questDropdown.getSelectedItem();
            if (selected != null) {
                bestMatch = selected;
            }
        });

        questDropdown.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submitQuest");
        questDropdown.getActionMap().put("submitQuest", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

        // Add components to layout
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.add(questDropdown, BorderLayout.CENTER);
        getInputRow().add(container, BorderLayout.CENTER);
    }

    @Override
    protected void submit()
    {
        if (bestMatch != null) {
            QuestTask mainQuestTask = QuestTask.builder()
                .quest(bestMatch)
                .indentLevel(0)
                .build();

            addTask(mainQuestTask);
        }
    }

    @Override
    protected void reset()
    {
        questDropdown.setSelectedIndex(-1);
        bestMatch = null;
    }
}