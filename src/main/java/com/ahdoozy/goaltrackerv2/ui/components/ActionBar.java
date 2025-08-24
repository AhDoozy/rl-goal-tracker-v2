package com.ahdoozy.goaltrackerv2.ui.components;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import net.runelite.client.ui.ColorScheme;

public class ActionBar extends JPanel
{
    private final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
    private final JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));

    public ActionBar()
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(6, 6, 6, 6));

        left.setOpaque(true);
        left.setBackground(ColorScheme.DARK_GRAY_COLOR);

        right.setOpaque(true);
        right.setBackground(ColorScheme.DARK_GRAY_COLOR);

        add(left);
        // Removed strut to eliminate extra spacing
        // add(javax.swing.Box.createHorizontalStrut(2));
        add(right);
    }

    public JPanel left() { return left; }
    public JPanel right() { return right; }
}