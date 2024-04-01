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
        // create a taskManager
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    public void removeTaskById_shouldRemoveIdFromRemovingTask() {
        // create a task in the taskManager and get the task id
        Task task = new Task("name","description", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task);
        int idExpected = task.getId();

        // remove the task from the taskManager and get the task id
        inMemoryTaskManager.removeTaskById(task.getId());
        int idActual = task.getId();

        // check that the task does not have an id like it had in the taskManager
        Assertions.assertNotEquals(idExpected, idActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddTaskAndReturnItById() {
        // make a task
        Task taskExpected = new Task("name","description", TaskStatus.NEW);

        // add the task to the taskManager and get the task form the taskManager by id
        inMemoryTaskManager.createTask(taskExpected);
        Task taskActual = inMemoryTaskManager.getTaskById(taskExpected.getId());

        // check that the made task equals the task gotten from the taskManager
        Assertions.assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddEpicAndReturnItById() {
        // make an epic
        Epic epicExpected = new Epic("name","description", TaskStatus.NEW);

        // add the epic to the taskManager and get the epic form the taskManager by id
        inMemoryTaskManager.createEpic(epicExpected);
        Epic epicActual = inMemoryTaskManager.getEpicById(epicExpected.getId());

        // check that the made epic equals the epic gotten from the taskManager
        Assertions.assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeInMemoryTaskManagerAddSubtaskAndReturnItById() {
        // make a subtask
        Subtask subtaskExpected = new Subtask("name","des", TaskStatus.NEW);

        // add the subtask to the taskManager and get the subtask form the taskManager by id
        inMemoryTaskManager.createSubtask(subtaskExpected, new Epic("n","d", TaskStatus.NEW));
        Subtask subtaskActual = inMemoryTaskManager.getSubtaskById(subtaskExpected.getId());

        // check that the made subtask equals the subtask gotten from the taskManager
        Assertions.assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldBeNoChangeInMemoryTaskManager_IfItChangeIdToAlreadyExistsInTask() {
        // make two tasks and add them to the taskManager
        Task firstTask = new Task("name","des", TaskStatus.NEW);
        Task secondTask = new Task("new name","new des", TaskStatus.NEW);
        inMemoryTaskManager.createTask(firstTask);
        inMemoryTaskManager.createTask(secondTask);

        // get a hashMap with the tasks in the taskManager
        HashMap<Integer, Task> tasksExpected = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        // set id of the first task to the second one and get a hashMap with actual tasks
        secondTask.setId(firstTask.getId());
        HashMap<Integer, Task> tasksActual = new HashMap<>(inMemoryTaskManager.getOnlyTasks());

        // check that the tasks in the different hashMaps equal each other
        for (Integer id : tasksExpected.keySet()) {
            Assertions.assertEquals(tasksExpected.get(id), tasksActual.get(id));
        }
    }

    @Test
    public void shouldNotChangeTaskFieldIfAddTaskTwiceToInMemoryTaskManager() {
        // make a task
        Task task = new Task("name","description", TaskStatus.NEW);

        // add the task to the taskManager twice and get the two task form the taskManager by id
        int firstId = inMemoryTaskManager.createTask(task);
        int secondId = inMemoryTaskManager.createTask(task);
        Task taskExpected = inMemoryTaskManager.getTaskById(firstId);
        Task taskActual = inMemoryTaskManager.getTaskById(secondId);

        // check that task fields do not change if task is added in the taskManager twice
        Assertions.assertEquals(taskExpected.getName(), taskActual.getName());
        Assertions.assertEquals(taskExpected.getDescription(), taskActual.getDescription());
        Assertions.assertEquals(taskExpected.getTaskStatus(), taskActual.getTaskStatus());
    }
}