package com.ahdoozy.goaltrackerv2.models;

import com.ahdoozy.goaltrackerv2.models.task.Task;

import java.util.List;

/**
 * Action for reordering a task within a list (move up/down).
 */
public class ReorderTaskAction implements ActionHistory.Action
{
    private final List<Task> tasks;
    private final Task task;
    private final int oldIndex;
    private final int newIndex;

    public ReorderTaskAction(List<Task> tasks, Task task, int oldIndex, int newIndex)
    {
        this.tasks = tasks;
        this.task = task;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    @Override
    public void undo()
    {
        tasks.remove(task);
        int insertIndex = Math.max(0, Math.min(oldIndex, tasks.size()));
        tasks.add(insertIndex, task);
    }

    @Override
    public void redo()
    {
        tasks.remove(task);
        int insertIndex = Math.max(0, Math.min(newIndex, tasks.size()));
        tasks.add(insertIndex, task);
    }
}
