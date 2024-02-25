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
        Task task = new Task("n","d", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task);
        Task taskById = inMemoryTaskManager.getTaskById(task.getId());
        assertEquals(task, taskById);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddEpicAndReturnItById() {
        Epic epic = new Epic("n","d", TaskStatus.NEW);
        inMemoryTaskManager.createEpic(epic);
        Epic epicById = inMemoryTaskManager.getEpicById(epic.getId());
        assertEquals(epic, epicById);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddSubtaskAndReturnItById() {
        Subtask subtask = new Subtask("n","d", TaskStatus.NEW);
        inMemoryTaskManager.createSubtask(subtask, new Epic("n","d", TaskStatus.NEW));
        Subtask subtaskById = inMemoryTaskManager.getSubtaskById(subtask.getId());
        assertEquals(subtask, subtaskById);
    }

    @Test
    public void shouldNotChangeInMemoryTaskManagerIfChangeIdToAlreadyExistsInTask() {
        Task task = new Task("name","des", TaskStatus.NEW);
        Task task1 = new Task("name1","des1", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task1);
        HashMap<Integer, Task> tasks = new HashMap<>(inMemoryTaskManager.getOnlyTasks());
        task1.setId(task.getId());
        HashMap<Integer, Task> newTasks = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        for (Integer id : tasks.keySet()) {
            assertEquals(tasks.get(id), newTasks.get(id));
        }
    }

    @Test
    public void shouldNotChangeTaskFieldIfAddTaskTwiceToInMemoryTaskManager() {
        Task task = new Task("n","d", TaskStatus.NEW);
        int id1 = inMemoryTaskManager.createTask(task);
        int id2 = inMemoryTaskManager.createTask(task);
        Task task1 = inMemoryTaskManager.getTaskById(id1);
        Task task2 = inMemoryTaskManager.getTaskById(id2);
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getDescription(), task2.getDescription());
        assertEquals(task1.getTaskStatus(), task2.getTaskStatus());
    }
}