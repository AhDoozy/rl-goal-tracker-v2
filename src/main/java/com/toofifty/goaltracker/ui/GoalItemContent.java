package com.toofifty.goaltracker.ui;
import net.runelite.client.ui.ColorScheme;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Goal;

import com.toofifty.goaltracker.ui.Refreshable;

import javax.swing.*;
import java.awt.*;

import com.toofifty.goaltracker.ui.components.ListItemPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

import static com.toofifty.goaltracker.utils.Constants.STATUS_TO_COLOR;

public class GoalItemContent extends JPanel implements Refreshable
{
    private final JTextField title = new JTextField();
    private final JLabel progress = new JLabel();

    private final Goal goal;

    GoalItemContent(GoalTrackerPlugin plugin, Goal goal)
    {
        super(new BorderLayout());
        this.goal = goal;

        setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8)); // padding for centered text
        // Let the parent card body paint the background; avoid double fills
        setOpaque(false);
        setBackground(null);

        add(title, BorderLayout.WEST);
        // Make goal title editable with standard copy/paste
        title.setBorder(null);
        title.setOpaque(false);
        title.setEditable(true);
        title.setDragEnabled(true);
        title.setCaretPosition(0);
        // Commit edits on Enter and when focus is lost
        title.addActionListener(e -> {
            String newText = title.getText();
            if (newText != null) {
                goal.setDescription(newText);
            }
        });
        title.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String newText = title.getText();
                if (newText != null) {
                    goal.setDescription(newText);
                }
            }
        });

        add(progress, BorderLayout.EAST);

        // Initialize visible text and colors immediately (before first refresh)
        {
            Color color = STATUS_TO_COLOR.get(goal.getStatus());
            title.setText(goal.getDescription());
            title.setForeground(color);
            title.setCaretColor(color);
            progress.setText(goal.getComplete().size() + "/" + goal.getTasks().size());
            progress.setForeground(color);
        }

        plugin.getUiStatusManager().addRefresher(goal, this::refresh);

        // Ensure right-click on any child shows the parent ListItemPanel context menu
        MouseAdapter forwardPopup = new MouseAdapter()
        {
            private void maybeShow(MouseEvent e)
            {
                if (!(e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)))
                {
                    return;
                }
                java.awt.Component src = (java.awt.Component) e.getSource();
                javax.swing.JComponent listItem = (javax.swing.JComponent) SwingUtilities.getAncestorOfClass(ListItemPanel.class, src);
                if (listItem == null)
                {
                    listItem = (javax.swing.JComponent) SwingUtilities.getAncestorOfClass(ListItemPanel.class, GoalItemContent.this);
                }
                if (listItem != null && listItem.getComponentPopupMenu() != null)
                {
                    java.awt.Point p = SwingUtilities.convertPoint(src, e.getPoint(), listItem);
                    listItem.getComponentPopupMenu().show(listItem, p.x, p.y);
                }
            }

            @Override public void mousePressed(MouseEvent e) { maybeShow(e); }
            @Override public void mouseReleased(MouseEvent e) { maybeShow(e); }
        };

        this.addMouseListener(forwardPopup);
        title.addMouseListener(forwardPopup);
        progress.addMouseListener(forwardPopup);
    }

    @Override
    public void refresh()
    {
        Color color = STATUS_TO_COLOR.get(goal.getStatus());

        title.setText(goal.getDescription());
        title.setForeground(color);
        title.setCaretColor(color);

        progress.setText(
            goal.getComplete().size() + "/" + goal.getTasks().size());
        progress.setForeground(color);
    }
}
