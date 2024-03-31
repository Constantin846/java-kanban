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
        // create a task and add it to the taskManager
        task = new Task("name", "description", TaskStatus.NEW);
        inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(task);
    }
    @Test
    public void add_shouldSaveLastFieldsOfGetTask() {
        // get the task from the taskManager by id
        inMemoryTaskManager.getTaskById(task.getId());

        // create a second task
        Task expectedTask = new Task("new name", "new description", TaskStatus.IN_PROGRESS);

        // update the first task to the second task
        inMemoryTaskManager.updateTask(task, expectedTask);
        // get the task from the taskManager
        inMemoryTaskManager.getTaskById(task.getId());
        // get the task from the historyManager
        Task taskFromHistory = (Task) inMemoryTaskManager.getHistory().get(0);

        // check that fields of task, gotten from the historyManager, equal fields of second task
        Assertions.assertEquals(expectedTask.getName(), taskFromHistory.getName());
        Assertions.assertEquals(expectedTask.getDescription(), taskFromHistory.getDescription());
        Assertions.assertEquals(expectedTask.getTaskStatus(), taskFromHistory.getTaskStatus());
    }
}