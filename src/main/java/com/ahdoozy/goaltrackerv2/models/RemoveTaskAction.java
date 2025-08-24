package com.ahdoozy.goaltrackerv2.models;

import com.ahdoozy.goaltrackerv2.models.task.Task;

import java.util.List;

/**
 * Action for removing a task from a list.
 */
public class RemoveTaskAction implements ActionHistory.Action
{
    private final List<Task> tasks;
    private final Task task;
    private final int index;

    public RemoveTaskAction(List<Task> tasks, Task task, int index)
    {
        this.tasks = tasks;
        this.task = task;
        this.index = index;
    }

    @Override
    public void undo()
    {
        int insertIndex = Math.max(0, Math.min(index, tasks.size()));
        tasks.add(insertIndex, task);
    }

    @Override
    public void redo()
    {
        tasks.remove(task);
    }
}
