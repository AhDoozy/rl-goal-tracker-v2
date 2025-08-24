package com.ahdoozy.goaltrackerv2.models;

import com.google.gson.annotations.SerializedName;
import com.ahdoozy.goaltrackerv2.utils.ReorderableList;
import com.ahdoozy.goaltrackerv2.models.enums.Status;
import com.ahdoozy.goaltrackerv2.models.task.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Setter
@Getter
@SuperBuilder
public class Goal
{
    @Builder.Default
    private String description = "New goal";

    @Builder.Default
    private int displayOrder = -1;

    @Builder.Default
    private boolean pinned = false;

    @SerializedName("items")
    @Builder.Default
    private ReorderableList<Task> tasks = new ReorderableList<>();

    private List<Task> filterBy(Predicate<Task> predicate)
    {
        return this.getTasks().stream().filter(predicate).collect(Collectors.toList());
    }

    public boolean isStatus(Status status) {
        return this.getTasks().stream().allMatch((task) -> task.getStatus() == status);
    }

    public boolean isAnyStatus(Status ...statuses) {
        return this.getTasks().stream().anyMatch((task) -> Arrays.stream(statuses).anyMatch((status) -> status == task.getStatus()));
    }

    public List<Task> getComplete() {
        return this.filterBy(Task::isDone);
    }

    public Status getStatus() {
        if (this.isStatus(Status.COMPLETED)) {
            return Status.COMPLETED;
        }

        if (this.isAnyStatus(Status.IN_PROGRESS, Status.COMPLETED)) {
            return Status.IN_PROGRESS;
        }

        return Status.NOT_STARTED;
    }

    public void setAllTasksCompleted(boolean completed) {
        for (Task task : tasks) {
            task.setStatus(completed ? Status.COMPLETED : Status.NOT_STARTED);
        }
    }
}
