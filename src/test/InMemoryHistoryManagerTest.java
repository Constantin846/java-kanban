package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.Managers;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;

class InMemoryHistoryManagerTest {
    private Task task;

    @BeforeEach
    public void beforeEach() {
        task = new Task("name", "description", TaskStatus.NEW);
        Managers.getDefault().createTask(task);
    }
    @Test
    public void shouldBeInMemoryHistoryManagerSavesOldFieldValuesOfTask() {
        Managers.getDefault().getTaskById(task.getId());
        Task newTask = new Task("new name", "new description", TaskStatus.IN_PROGRESS);
        Managers.getDefault().updateTask(task, newTask);
        Task historyTask = (Task) Managers.getDefaultHistory().getHistory().getFirst();

        Assertions.assertEquals(task.getName(), historyTask.getName());
        Assertions.assertEquals(task.getDescription(), historyTask.getDescription());
        Assertions.assertEquals(task.getTaskStatus(), historyTask.getTaskStatus());
    }

    @Test
    public void shouldBeMaxTaskCountOfInMemoryHistoryManagerEquals10() {
        for (int i = 0; i < 20; i++) {
            Managers.getDefault().getTaskById(task.getId());
        }
        Assertions.assertEquals(Managers.getDefaultHistory().getHistory().size(), 10);
    }
}