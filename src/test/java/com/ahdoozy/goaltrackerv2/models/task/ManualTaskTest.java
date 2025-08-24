package com.ahdoozy.goaltrackerv2.models.task;

import com.ahdoozy.goaltrackerv2.models.enums.Status;
import com.ahdoozy.goaltrackerv2.models.enums.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManualTaskTest {

    @Test
    void toggle_shouldSupportCompletingTasks() {
        ManualTask task = ManualTask.builder().build();

        assertEquals(Status.NOT_STARTED, task.getStatus());

        task.toggle();

        assertEquals(Status.COMPLETED, task.getStatus());
    }

    @Test
    void toggle_supportResettingTasks() {
        ManualTask task = ManualTask.builder().status(Status.COMPLETED).build();

        assertEquals(Status.COMPLETED, task.getStatus());

        task.toggle();

        assertEquals(Status.NOT_STARTED, task.getStatus());
    }

    @Test
    void toString_shouldReturnTheDescription() {
        ManualTask task = ManualTask.builder().description("Do all the things!").build();

        assertEquals("Do all the things!", task.toString());
    }

    @Test
    void getType_shouldReturnManual() {
        ManualTask task = ManualTask.builder().build();

        assertEquals(TaskType.MANUAL, task.getType());
    }
}