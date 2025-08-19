package com.toofifty.goaltracker.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class TextButton extends JLabel
{
    private Color mainColor = ColorScheme.PROGRESS_COMPLETE_COLOR;

    public TextButton(String text, Color mainColor)
    {
        this(text);
        setMainColor(mainColor);
    }

    public TextButton(String text)
    {
        super(text);

        setFont(FontManager.getRunescapeSmallFont());
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(0, 8, 0, 8));
        setForeground(mainColor);
    }

    public TextButton setMainColor(Color mainColor)
    {
        this.mainColor = mainColor;
        setForeground(mainColor);
        return this;
    }

    public TextButton(String text, Consumer<MouseEvent> clickListener)
    {
        this(text);
        onClick(clickListener);
    }

    public TextButton onClick(Consumer<MouseEvent> clickListener) {
        return null;
    }

    public TextButton narrow()
    {
        setBorder(new EmptyBorder(0, 2, 0, 2));
        return this;
    }

    public void setOnClick(Object o) {
    }
}
