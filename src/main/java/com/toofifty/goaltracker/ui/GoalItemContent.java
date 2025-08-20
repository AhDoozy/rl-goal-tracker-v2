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
    private final SlimBar progressBar = new SlimBar();

    private final Goal goal;

    GoalItemContent(GoalTrackerPlugin plugin, Goal goal)
    {
        super(new BorderLayout());
        this.goal = goal;

        setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8)); // padding for centered text
        // Let the parent card body paint the background; avoid double fills
        setOpaque(false);
        setBackground(null);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
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

        topRow.add(progress, BorderLayout.EAST);
        add(topRow, BorderLayout.CENTER);

        // Slim custom progress bar under the title row
        progressBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0)); // gap above the bar
        progressBar.setPreferredSize(new Dimension(0, 6)); // 6px tall bar
        add(progressBar, BorderLayout.SOUTH);

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
        progressBar.addMouseListener(forwardPopup);
        // Ensure item icon/text initialize on first render (e.g., on login)
        javax.swing.SwingUtilities.invokeLater(this::refresh);
    }

    @Override
    public void addNotify()
    {
        super.addNotify();
        // In case construction happened before UI was realized, refresh when shown
        javax.swing.SwingUtilities.invokeLater(this::refresh);
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

        int total = goal.getTasks().size();
        int done = goal.getComplete().size();
        progressBar.setVisible(total > 0);
        progressBar.setProgress(done, total, color);
    }
    private static class SlimBar extends JComponent {
        private int done = 0;
        private int total = 1;
        private Color fill = Color.GREEN;

        void setProgress(int done, int total, Color fill) {
            this.done = done;
            this.total = Math.max(1, total);
            this.fill = fill;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            // track
            g2.setColor(ColorScheme.DARKER_GRAY_COLOR);
            g2.fillRect(0, 0, w, h);
            // fill
            int barW = (int) ((done / (double) total) * w);
            g2.setColor(fill != null ? fill : Color.GREEN);
            g2.fillRect(0, 0, barW, h);
            g2.dispose();
        }
    }
}
