package com.toofifty.goaltracker.models;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Generic history manager for undo/redo of user actions.
 *
 * Usage:
 *  - Define actions implementing {@link ActionHistory.Action}
 *  - Call {@link #push(Action)} whenever an action is performed
 *  - Call {@link #undo()} or {@link #redo()} from UI buttons
 */
public class ActionHistory
{
    public interface Action
    {
        void undo();
        void redo();
    }

    private final Deque<Action> undoStack = new ArrayDeque<>();
    private final Deque<Action> redoStack = new ArrayDeque<>();

    /** Push a new action, clearing the redo history. */
    public void push(Action action)
    {
        undoStack.addLast(action);
        redoStack.clear();
    }

    /** Undo the most recent action, if any. */
    public void undo()
    {
        Action a = undoStack.pollLast();
        if (a != null)
        {
            a.undo();
            redoStack.addLast(a);
        }
    }

    /** Redo the most recently undone action, if any. */
    public void redo()
    {
        Action a = redoStack.pollLast();
        if (a != null)
        {
            a.redo();
            undoStack.addLast(a);
        }
    }

    public boolean hasUndo()
    {
        return !undoStack.isEmpty();
    }

    public boolean hasRedo()
    {
        return !redoStack.isEmpty();
    }

    public void clear()
    {
        undoStack.clear();
        redoStack.clear();
    }
}
