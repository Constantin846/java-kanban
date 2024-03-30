package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.TaskManager;
import tracker.service.Managers;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;

class InMemoryHistoryManagerTest {
    private Task task;
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("name", "description", TaskStatus.NEW);
        inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(task);
    }
    @Test
    public void add_shouldSaveLastFieldsOfGetTask() {
        // prepare
        inMemoryTaskManager.getTaskById(task.getId());
        Task expectedTask = new Task("new name", "new description", TaskStatus.IN_PROGRESS);

        // do
        inMemoryTaskManager.updateTask(task, expectedTask);
        inMemoryTaskManager.getTaskById(task.getId());
        Task taskFromHistory = (Task) inMemoryTaskManager.getHistory().get(0);

        // check
        Assertions.assertEquals(expectedTask.getName(), taskFromHistory.getName());
        Assertions.assertEquals(expectedTask.getDescription(), taskFromHistory.getDescription());
        Assertions.assertEquals(expectedTask.getTaskStatus(), taskFromHistory.getTaskStatus());
    }
}