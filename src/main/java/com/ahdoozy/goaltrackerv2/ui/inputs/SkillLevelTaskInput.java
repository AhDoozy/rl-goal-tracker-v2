package com.ahdoozy.goaltrackerv2.ui.inputs;

import com.ahdoozy.goaltrackerv2.GoalTrackerV2Plugin;
import com.ahdoozy.goaltrackerv2.models.Goal;
import com.ahdoozy.goaltrackerv2.models.task.SkillLevelTask;
import com.ahdoozy.goaltrackerv2.ui.SimpleDocumentListener;
import com.ahdoozy.goaltrackerv2.ui.components.ComboBox;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

public class SkillLevelTaskInput extends TaskInput
{
    private FlatTextField levelField;
    private String levelFieldValue = "99";

    private ComboBox<Skill> skillField;

    private Pattern numberPattern = Pattern.compile("^(?:\\d{1,2})?$");

    public SkillLevelTaskInput(GoalTrackerV2Plugin plugin, Goal goal)
    {
        super(plugin, goal, "Skill level");

        levelField = new FlatTextField();
        levelField.setBorder(new EmptyBorder(0, 8, 0, 8));
        levelField.getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
        levelField.setText(levelFieldValue);
        levelField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        levelField.getDocument().addDocumentListener(
            (SimpleDocumentListener) e -> SwingUtilities.invokeLater(() -> {
                String value = levelField.getText();
                if (!numberPattern.matcher(value).find()) {
                    levelField.setText(levelFieldValue);
                    return;
                }
                levelFieldValue = value;
            }));
        levelField.setPreferredSize(new Dimension(92, PREFERRED_INPUT_HEIGHT));

        getInputRow().add(levelField, BorderLayout.CENTER);

        skillField = new ComboBox<>(Skill.values());

        getInputRow().add(skillField, BorderLayout.WEST);
    }

    @Override
    protected void submit()
    {
        if (levelField.getText().isEmpty()) {
            return;
        }

        addTask(SkillLevelTask.builder()
            .skill((Skill) skillField.getSelectedItem())
            .level(Integer.parseInt(levelField.getText()))
            .build());
    }

    @Override
    protected void reset()
    {
        levelFieldValue = "99";
        levelField.setText(levelFieldValue);

        skillField.setSelectedIndex(0);
    }
}