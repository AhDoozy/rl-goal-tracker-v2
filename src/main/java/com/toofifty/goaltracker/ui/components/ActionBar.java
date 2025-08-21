package com.toofifty.goaltracker.ui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public class ActionBar extends JPanel
{
    private final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
    private final JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    private final JPanel spacer = new JPanel();

    public ActionBar()
    {
        super(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(6, 10, 6, 10));

        left.setOpaque(true);
        left.setBackground(ColorScheme.DARK_GRAY_COLOR);
        left.setBorder(new EmptyBorder(0, 0, 0, 4));

        right.setOpaque(true);
        right.setBackground(ColorScheme.DARK_GRAY_COLOR);
        right.setBorder(new EmptyBorder(0, 4, 0, 0));

        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(8, 1));
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        add(spacer, BorderLayout.CENTER);
    }

    public JPanel left() { return left; }
    public JPanel right() { return right; }
}