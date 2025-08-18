package com.toofifty.goaltracker.ui.inputs;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.models.task.SkillLevelTask;
import com.toofifty.goaltracker.models.task.Task;
import com.toofifty.goaltracker.utils.QuestRequirements;
import net.runelite.api.Quest;
import org.apache.commons.lang3.StringUtils;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class QuestTaskInput extends TaskInput
{
    private final JTextField searchField;
    private final JLabel matchLabel;
    private final List<Quest> allQuests;
    private Quest bestMatch;

    public QuestTaskInput(GoalTrackerPlugin plugin, Goal goal)
    {
        super(plugin, goal, "Quest");


        // Initialize all quests and sort them
        allQuests = Arrays.asList(Quest.values());
        allQuests.sort(Comparator.comparing(Quest::getName));


        // Initialize search field
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 24));
        searchField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Add Enter key listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submit();
                }
            }
        });

        // Initialize match label
        matchLabel = new JLabel("Enter a quest name");
        matchLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        matchLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        // Add listener for real-time search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateMatch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateMatch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateMatch(); }
        });

        // Add components to layout
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.add(searchField, BorderLayout.NORTH);
        container.add(matchLabel, BorderLayout.CENTER);
        getInputRow().add(container, BorderLayout.CENTER);
    }

    private void updateMatch()
    {
        String searchText = searchField.getText().trim();
        bestMatch = null;

        if (searchText.isEmpty()) {
            matchLabel.setText("Enter a quest name");
            matchLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            return;
        }

        final String lower = searchText.toLowerCase(Locale.ROOT);

        // 1) Exact (case-insensitive) match first
        for (Quest q : allQuests) {
            if (q.getName().equalsIgnoreCase(searchText)) {
                bestMatch = q;
                matchLabel.setText("Match: " + q.getName());
                matchLabel.setForeground(ColorScheme.BRAND_ORANGE);
                return;
            }
        }

        // 2) Prefix match (case-insensitive); pick the shortest matching name
        Quest prefixBest = null;
        for (Quest q : allQuests) {
            String nameLower = q.getName().toLowerCase(Locale.ROOT);
            if (nameLower.startsWith(lower)) {
                if (prefixBest == null || q.getName().length() < prefixBest.getName().length()) {
                    prefixBest = q;
                }
            }
        }
        if (prefixBest != null) {
            bestMatch = prefixBest;
            matchLabel.setText("Match: " + bestMatch.getName());
            matchLabel.setForeground(ColorScheme.BRAND_ORANGE);
            return;
        }

        // 3) Fuzzy: compute Levenshtein distances once and pick the minimum
        Quest candidate = null;
        int bestDistance = Integer.MAX_VALUE;
        for (Quest q : allQuests) {
            int d = StringUtils.getLevenshteinDistance(
                q.getName().toLowerCase(Locale.ROOT), lower
            );
            if (d < bestDistance) {
                bestDistance = d;
                candidate = q;
            }
        }

        if (candidate != null) {
            int nameLen = candidate.getName().length();
            int threshold = Math.max(1, (int)Math.floor(nameLen * 0.3));
            if (bestDistance <= threshold) {
                bestMatch = candidate;
                matchLabel.setText("Match: " + bestMatch.getName());
                matchLabel.setForeground(ColorScheme.BRAND_ORANGE);
                return;
            }
        }

        matchLabel.setText("No close match found");
        matchLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
    }

    @Override
    protected void submit()
    {
        if (bestMatch != null) {
            // Build list first so reset() in addTasks doesn't clear bestMatch prematurely
            List<Task> toAdd = new ArrayList<>();
            toAdd.add(
                QuestTask.builder()
                    .quest(bestMatch)
                    .indentLevel(0)
                    .build()
            );

            List<Task> requirements = QuestRequirements.getRequirements(bestMatch, 0);
            toAdd.addAll(requirements);

            // Add all at once (single refresh/reset)
            addTasks(toAdd);

            matchLabel.setText("Quest and requirements added: " + bestMatch.getName());
            matchLabel.setForeground(ColorScheme.BRAND_ORANGE);
        } else {
            matchLabel.setText("Please select a valid quest");
            matchLabel.setForeground(Color.RED);
        }
    }

    @Override
    protected void reset()
    {
        searchField.setText("");
        matchLabel.setText("Enter a quest name");
        matchLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        bestMatch = null;
    }
}