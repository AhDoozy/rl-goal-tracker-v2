package com.ahdoozy.goaltrackerv2.models;

import java.util.List;
import com.ahdoozy.goaltrackerv2.models.task.Task;

/**
 * Action for indenting or outdenting a task in a list.
 */
public class IndentChangeAction implements ActionHistory.Action
{
    private final List<Task> tasks;
    private final Task task;
    private final int oldIndent;
    private final int newIndent;

    public IndentChangeAction(List<Task> tasks, Task task, int oldIndent, int newIndent)
    {
        this.tasks = tasks;
        this.task = task;
        this.oldIndent = oldIndent;
        this.newIndent = newIndent;
    }

    @Override
    public void undo()
    {
        task.setIndentLevel(oldIndent);
    }

    @Override
    public void redo()
    {
        task.setIndentLevel(newIndent);
    }
}
