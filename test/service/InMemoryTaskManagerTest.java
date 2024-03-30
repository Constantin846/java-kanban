package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.InMemoryHistoryManager;
import tracker.service.InMemoryTaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.util.HashMap;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    public void removeTaskById_shouldRemoveIdFromRemovingTask() {
        // prepare
        Task task = new Task("name","description", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task);
        int idExpected = task.getId();

        // do
        inMemoryTaskManager.removeTaskById(task.getId());
        int idActual = task.getId();

        // check
        Assertions.assertNotEquals(idExpected, idActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddTaskAndReturnItById() {
        // prepare
        Task taskExpected = new Task("name","description", TaskStatus.NEW);

        // do
        inMemoryTaskManager.createTask(taskExpected);
        Task taskActual = inMemoryTaskManager.getTaskById(taskExpected.getId());

        // check
        Assertions.assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddEpicAndReturnItById() {
        // prepare
        Epic epicExpected = new Epic("name","description", TaskStatus.NEW);
        // do
        inMemoryTaskManager.createEpic(epicExpected);
        Epic epicActual = inMemoryTaskManager.getEpicById(epicExpected.getId());

        // check
        Assertions.assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddSubtaskAndReturnItById() {
        // prepare
        Subtask subtaskExpected = new Subtask("name","des", TaskStatus.NEW);

        // do
        inMemoryTaskManager.createSubtask(subtaskExpected, new Epic("n","d", TaskStatus.NEW));
        Subtask subtaskActual = inMemoryTaskManager.getSubtaskById(subtaskExpected.getId());

        // check
        Assertions.assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldBeNoChangeInMemoryTaskManager_IfChangeIdToAlreadyExistsInTask() {
        // prepare
        Task firstTask = new Task("name","des", TaskStatus.NEW);
        Task secondTask = new Task("new name","new des", TaskStatus.NEW);
        inMemoryTaskManager.createTask(firstTask);
        inMemoryTaskManager.createTask(secondTask);
        HashMap<Integer, Task> tasksExpected = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        // do
        secondTask.setId(firstTask.getId());
        HashMap<Integer, Task> tasksActual = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        // check
        for (Integer id : tasksExpected.keySet()) {
            Assertions.assertEquals(tasksExpected.get(id), tasksActual.get(id));
        }
    }

    @Test
    public void shouldNotChangeTaskFieldIfAddTaskTwiceToInMemoryTaskManager() {
        // prepare
        Task task = new Task("name","description", TaskStatus.NEW);

        // do
        int firstId = inMemoryTaskManager.createTask(task);
        int secondId = inMemoryTaskManager.createTask(task);
        Task taskExpected = inMemoryTaskManager.getTaskById(firstId);
        Task taskActual = inMemoryTaskManager.getTaskById(secondId);

        // check
        Assertions.assertEquals(taskExpected.getName(), taskActual.getName());
        Assertions.assertEquals(taskExpected.getDescription(), taskActual.getDescription());
        Assertions.assertEquals(taskExpected.getTaskStatus(), taskActual.getTaskStatus());
    }
}