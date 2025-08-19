package com.toofifty.goaltracker.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.function.Function;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.Text;

/**
 * Simple generic ComboBox with optional formatter and a renderer
 * that matches RuneLite dark styling. Includes a helper to keep the
 * popup open after a selection (for rapid multi-add flows).
 */
public class ComboBox<T> extends JComboBox<T>
{
    private Function<T, String> formatter = null;

    public ComboBox()
    {
        super();
        setRenderer(new ComboBoxListRenderer());
        // Keep consistent look with RuneLite
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setForeground(Color.WHITE);
        setUI(new BasicComboBoxUI());
    }
    public ComboBox(T[] items)
    {
        this();
        setItems(java.util.Arrays.asList(items));
    }


    public ComboBox(List<T> items)
    {
        this();
        setItems(items);
    }

    public void setItems(List<T> items)
    {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        if (items != null)
        {
            for (T it : items)
            {
                model.addElement(it);
            }
        }
        setModel(model);
    }

    public void setFormatter(Function<T, String> formatter)
    {
        this.formatter = formatter;
        repaint();
    }

    /**
     * Re-opens the popup after a selection is made, enabling fast repeated adds.
     * Call this once after constructing the combo if you want the behavior.
     */
    public void setStayOpenOnSelection(boolean stayOpen)
    {
        if (stayOpen)
        {
            this.addActionListener(e ->
                SwingUtilities.invokeLater(() -> this.setPopupVisible(true))
            );
        }
    }

    private class ComboBoxListRenderer implements ListCellRenderer<T>
    {
        @Override
        public Component getListCellRendererComponent(
            JList<? extends T> list,
            T value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            JPanel container = new JPanel(new BorderLayout());
            container.setBorder(new EmptyBorder(2, 6, 2, 6));

            JLabel label = new JLabel();
            label.setOpaque(false);

            if (value != null)
            {
                if (formatter != null)
                {
                    label.setText(formatter.apply(value));
                }
                else if (value instanceof Enum)
                {
                    label.setText(Text.titleCase((Enum<?>) value));
                }
                else
                {
                    label.setText(value.toString());
                }
            }
            else
            {
                label.setText("");
            }

            container.add(label, BorderLayout.WEST);

            if (isSelected)
            {
                container.setBackground(ColorScheme.DARK_GRAY_COLOR);
                label.setForeground(Color.WHITE);
            }
            else
            {
                container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                label.setForeground(Color.WHITE);
            }

            return container;
        }
    }
}
