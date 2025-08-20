package com.toofifty.goaltracker.ui.components;

import com.toofifty.goaltracker.utils.ReorderableList;
import com.toofifty.goaltracker.ui.Refreshable;
import com.toofifty.goaltracker.models.Goal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public class ListItemPanel<T> extends JPanel implements Refreshable
{
    protected final JMenuItem moveUp = new JMenuItem("Move up");
    protected final JMenuItem moveDown = new JMenuItem("Move down");
    protected final JMenuItem moveToTop = new JMenuItem("Move to top");
    protected final JMenuItem moveToBottom = new JMenuItem("Move to bottom");
    protected final JMenuItem removeItem = new JMenuItem("Remove");
    protected final JPopupMenu popupMenu = new JPopupMenu();

    protected final ReorderableList<T> list;
    protected final T item;

    protected Consumer<T> reorderedListener;
    protected Consumer<T> removedListener;
    protected BiConsumer<T, Integer> removedWithIndexListener;

    private MouseAdapter clickListenerAdapter;

    private void addClickListenerRecursive(Component c)
    {
        if (clickListenerAdapter == null) return;
        c.addMouseListener(clickListenerAdapter);
        if (c instanceof java.awt.Container)
        {
            for (Component child : ((java.awt.Container) c).getComponents())
            {
                addClickListenerRecursive(child);
            }
        }
    }

    private void addContextMenuListenerRecursive(Component c)
    {
        c.addMouseListener(contextMenuListener);
        if (c instanceof java.awt.Container)
        {
            for (Component child : ((java.awt.Container) c).getComponents())
            {
                addContextMenuListenerRecursive(child);
            }
        }
    }

    private final MouseAdapter contextMenuListener = new MouseAdapter()
    {
        private void maybeShow(MouseEvent e)
        {
            if (!(e.isPopupTrigger() || javax.swing.SwingUtilities.isRightMouseButton(e)))
            {
                return;
            }
            popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }

        @Override public void mousePressed(MouseEvent e) { maybeShow(e); }
        @Override public void mouseReleased(MouseEvent e) { maybeShow(e); }
    };

    public ListItemPanel(ReorderableList<T> list, T item)
    {
        super(new BorderLayout());
        this.list = list;
        this.item = item;

        if (item instanceof Goal) {
            setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 2, 4, 2), // minimal outer spacing
                javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1, true), // rounded card outline
                    new EmptyBorder(2, 4, 2, 4) // minimal inner padding
                )
            ));
            setBackground(ColorScheme.DARKER_GRAY_COLOR);
        } else {
            setBorder(new EmptyBorder(2, 4, 2, 4)); // add horizontal and vertical spacing for tasks
            setBackground(ColorScheme.DARK_GRAY_COLOR);
        }

        moveUp.addActionListener(e -> {
            list.moveUp(item);
            if (this.reorderedListener != null) this.reorderedListener.accept(item);
        });

        moveDown.addActionListener(e -> {
            list.moveDown(item);
            if (this.reorderedListener != null) this.reorderedListener.accept(item);
        });

        moveToTop.addActionListener(e -> {
            list.moveToTop(item);
            if (this.reorderedListener != null) this.reorderedListener.accept(item);
        });

        moveToBottom.addActionListener(e -> {
            list.moveToBottom(item);
            if (this.reorderedListener != null) this.reorderedListener.accept(item);
        });

        removeItem.addActionListener(e -> {
            int index = list.indexOf(item);
            list.remove(item);
            if (this.removedWithIndexListener != null) this.removedWithIndexListener.accept(item, index);
            if (this.removedListener != null) this.removedListener.accept(item);
        });

        popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));

        setComponentPopupMenu(popupMenu);
        // Also show context menu on press/release to handle platform differences
        this.addMouseListener(contextMenuListener);
        // Ensure popup and click listeners cover all descendants initially
        addContextMenuListenerRecursive(this);
        setOpaque(true);
    }

    @Override
    public void setBackground(Color bg)
    {
        super.setBackground(bg);
        for (Component component : getComponents()) {
            component.setBackground(bg);
        }
    }

    @Override
    public void refresh()
    {
        refreshMenu();

        for (Component component : getComponents()) {
            if (component instanceof Refreshable) {
                ((Refreshable) component).refresh();
            }
        }
    }

    public void refreshMenu()
    {
        popupMenu.removeAll();
        if (!list.isFirst(item)) {
            popupMenu.add(moveUp);
        }
        if (!list.isLast(item)) {
            popupMenu.add(moveDown);
        }
        if (!list.isFirst(item)) {
            popupMenu.add(moveToTop);
        }
        if (!list.isLast(item)) {
            popupMenu.add(moveToBottom);
        }
        popupMenu.add(removeItem);

        if (item instanceof Goal) {
            JMenuItem markAllComplete = new JMenuItem("Mark all as completed");
            JMenuItem markAllIncomplete = new JMenuItem("Mark all as incomplete");

            markAllComplete.addActionListener(e -> {
                Goal goal = (Goal) item;
                goal.setAllTasksCompleted(true);
                refresh();
            });

            markAllIncomplete.addActionListener(e -> {
                Goal goal = (Goal) item;
                goal.setAllTasksCompleted(false);
                refresh();
            });

            popupMenu.addSeparator();
            popupMenu.add(markAllComplete);
            popupMenu.add(markAllIncomplete);
        }
    }

    public ListItemPanel<T> add(Component comp)
    {
        super.add(comp, BorderLayout.CENTER);
        addContextMenuListenerRecursive(comp);
        if (clickListenerAdapter != null) addClickListenerRecursive(comp);
        return this;
    }

    public void onClick(Consumer<MouseEvent> clickListener)
    {
        clickListenerAdapter = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    clickListener.accept(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (item instanceof Goal) {
                    setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (item instanceof Goal) {
                    setBackground(ColorScheme.DARKER_GRAY_COLOR);
                }
            }
        };

        // Attach to this panel and all current children
        addMouseListener(clickListenerAdapter);
        addClickListenerRecursive(this);

        // Optional: use a hand cursor to indicate clickability
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public void onRemoved(Consumer<T> removeListener) {
        this.removedListener = removeListener;
    }

    public void onReordered(Consumer<T> reorderListener) {
        this.reorderedListener = reorderListener;
    }

    public void onRemovedWithIndex(BiConsumer<T, Integer> removeListener) {
        this.removedWithIndexListener = removeListener;
    }
}
