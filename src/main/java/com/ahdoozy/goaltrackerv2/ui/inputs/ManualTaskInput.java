package com.ahdoozy.goaltrackerv2.ui.inputs;

import com.ahdoozy.goaltrackerv2.GoalTrackerV2Plugin;
import com.ahdoozy.goaltrackerv2.models.Goal;
import com.ahdoozy.goaltrackerv2.models.task.ManualTask;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ManualTaskInput extends TaskInput
{
    private final FlatTextField titleField;

    public ManualTaskInput(GoalTrackerV2Plugin plugin, Goal goal)
    {
        super(plugin, goal, "Quick add");

        titleField = new FlatTextField();
        titleField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titleField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) return;

                submit();
            }
        });

        getInputRow().add(titleField, BorderLayout.CENTER);
    }

    @Override
    protected void submit()
    {
        if (titleField.getText().isEmpty()) {
            return;
        }

        this.addTask(ManualTask.builder().description(titleField.getText()).build());
    }

    @Override
    protected void reset()
    {
        titleField.setText("");
        titleField.requestFocusInWindow();
    }
}