package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddTaskAndReturnItById() {
        Task taskExpected = new Task("name","description", TaskStatus.NEW);
        inMemoryTaskManager.createTask(taskExpected);
        Task taskActual = inMemoryTaskManager.getTaskById(taskExpected.getId());
        assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddEpicAndReturnItById() {
        Epic epicExpected = new Epic("name","description", TaskStatus.NEW);
        inMemoryTaskManager.createEpic(epicExpected);
        Epic epicActual = inMemoryTaskManager.getEpicById(epicExpected.getId());
        assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddSubtaskAndReturnItById() {
        Subtask subtaskExpected = new Subtask("name","des", TaskStatus.NEW);
        inMemoryTaskManager.createSubtask(subtaskExpected, new Epic("n","d", TaskStatus.NEW));
        Subtask subtaskActual = inMemoryTaskManager.getSubtaskById(subtaskExpected.getId());
        assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldNotChangeInMemoryTaskManagerIfChangeIdToAlreadyExistsInTask() {
        Task firstTask = new Task("name","des", TaskStatus.NEW);
        Task secondTask = new Task("new name","new des", TaskStatus.NEW);
        inMemoryTaskManager.createTask(firstTask);
        inMemoryTaskManager.createTask(secondTask);
        HashMap<Integer, Task> tasksExpected = new HashMap<>(inMemoryTaskManager.getOnlyTasks());
        secondTask.setId(firstTask.getId());
        HashMap<Integer, Task> tasksActual = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        for (Integer id : tasksExpected.keySet()) {
            assertEquals(tasksExpected.get(id), tasksActual.get(id));
        }
    }

    @Test
    public void shouldNotChangeTaskFieldIfAddTaskTwiceToInMemoryTaskManager() {
        Task task = new Task("name","description", TaskStatus.NEW);
        int firstId = inMemoryTaskManager.createTask(task);
        int secondId = inMemoryTaskManager.createTask(task);
        Task taskExpected = inMemoryTaskManager.getTaskById(firstId);
        Task taskActual = inMemoryTaskManager.getTaskById(secondId);

        assertEquals(taskExpected.getName(), taskActual.getName());
        assertEquals(taskExpected.getDescription(), taskActual.getDescription());
        assertEquals(taskExpected.getTaskStatus(), taskActual.getTaskStatus());
    }
}