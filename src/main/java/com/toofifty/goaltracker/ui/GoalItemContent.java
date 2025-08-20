package com.toofifty.goaltracker.ui;

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
    private final JLabel title = new JLabel();
    private final JLabel progress = new JLabel();

    private final Goal goal;

    GoalItemContent(GoalTrackerPlugin plugin, Goal goal)
    {
        super(new BorderLayout());
        this.goal = goal;

        add(title, BorderLayout.WEST);
        add(progress, BorderLayout.EAST);

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

        progress.setText(
            goal.getComplete().size() + "/" + goal.getTasks().size());
        progress.setForeground(color);
    }
}
