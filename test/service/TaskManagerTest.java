package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.exceptions.TaskIntersectionException;
import tracker.service.Managers;
import tracker.service.TaskManager;
import tracker.tasks.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

abstract class TaskManagerTest {
    protected TaskManager taskManager;
    protected ZoneId zoneId;

    protected TaskManager createTaskManager() {
        return Managers.getDefault();
    }

    @BeforeEach
    public void beforeEach() {
        // create a taskManager
        taskManager = createTaskManager();
        zoneId = ZoneId.of("UTC+4");
    }

    @Test
    public void shouldCheckTaskIntersection() {
        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 30);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).minusMinutes(50), 48);
        Task newSecondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).plusMinutes(170), 60);

        Epic epicWithSubtask = new Epic("Эпик первый", "Эпик с подзадачами");

        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW,
                ZonedDateTime.now(zoneId).minusMinutes(2), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).minusMinutes(10), 120);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.NEW,
                ZonedDateTime.now(zoneId).plusMinutes(180), 180);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(epicWithSubtask);

        try {
            taskManager.createSubtask(firstSubtask, epicWithSubtask);
        } catch (TaskIntersectionException tie) {
            System.out.println("First subtask has not been added");
        }
        try {
            taskManager.createSubtask(secondSubtask, epicWithSubtask);
        } catch (TaskIntersectionException tie) {
            System.out.println("Second subtask has not been added");
        }
        taskManager.createSubtask(thirdSubtask, epicWithSubtask);

        try {
            taskManager.updateTask(secondTask, newSecondTask);
        } catch (TaskIntersectionException tie) {
            System.out.println("Second task has not been updated");
        }

        TreeSet<IntersectableTask> prioritizedTasks = taskManager.getPrioritizedTasks();
        Iterator<IntersectableTask> iterator = prioritizedTasks.iterator();

        Assertions.assertEquals(secondTask, iterator.next());
        Assertions.assertEquals(firstTask, iterator.next());
        Assertions.assertEquals(thirdSubtask, iterator.next());
    }

    @Test
    public void createSubtask_shouldBeSubtaskHasEpic() {
        Subtask subtask = new Subtask("subtask", "description", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 15);
        Epic epicExpected = new Epic("epic", "description");

        taskManager.createEpic(epicExpected);
        taskManager.createSubtask(subtask, epicExpected);

        Assertions.assertNotNull(subtask.getTopEpic());
        Assertions.assertEquals(epicExpected, subtask.getTopEpic());
    }

    @Test
    public void removeTaskById_shouldRemoveIdFromRemovingTask() {
        // create a task in the taskManager and get the task id
        Task task = new Task("name","description", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 12);
        taskManager.createTask(task);
        int idExpected = task.getId();

        // remove the task from the taskManager and get the task id
        taskManager.removeTaskById(task.getId());
        int idActual = task.getId();

        // check that the task does not have an id like it had in the taskManager
        Assertions.assertNotEquals(idExpected, idActual);
    }

    @Test
    public void shouldBeTaskManagerAddTaskAndReturnItById() {
        // make a task
        Task taskExpected = new Task("name","description", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 14);

        // add the task to the taskManager and get the task form the taskManager by id
        taskManager.createTask(taskExpected);
        Task taskActual = taskManager.getTaskById(taskExpected.getId()).get();

        // check that the made task equals the task gotten from the taskManager
        Assertions.assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeTaskManagerAddEpicAndReturnItById() {
        // make an epic
        Epic epicExpected = new Epic("name","description");

        // add the epic to the taskManager and get the epic form the taskManager by id
        taskManager.createEpic(epicExpected);
        Epic epicActual = taskManager.getEpicById(epicExpected.getId()).get();

        // check that the made epic equals the epic gotten from the taskManager
        Assertions.assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeTaskManagerAddSubtaskAndReturnItById() {
        // make a subtask
        Subtask subtaskExpected = new Subtask("name","des", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 16);

        // add the subtask to the taskManager and get the subtask form the taskManager by id
        taskManager.createSubtask(subtaskExpected, new Epic("n","d"));
        Subtask subtaskActual = taskManager.getSubtaskById(subtaskExpected.getId()).get();

        // check that the made subtask equals the subtask gotten from the taskManager
        Assertions.assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldBeNoChangeTaskManager_IfItChangeIdToAlreadyExistsInTask() {
        // make two tasks and add them to the taskManager
        Task firstTask = new Task("name","des", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 18);
        Task secondTask = new Task("new name","new des", TaskStatus.NEW,
                ZonedDateTime.now(zoneId).plusMinutes(20), 20);
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        // get a hashMap with the tasks in the taskManager
        HashMap<Integer, Task> tasksExpected = new HashMap<>(taskManager.getOnlyTasks());

        // set id of the first task to the second one and get a hashMap with actual tasks
        secondTask.setId(firstTask.getId());
        HashMap<Integer, Task> tasksActual = new HashMap<>(taskManager.getOnlyTasks());

        // check that the tasks in the different hashMaps equal each other
        for (Integer id : tasksExpected.keySet()) {
            Assertions.assertEquals(tasksExpected.get(id), tasksActual.get(id));
        }
    }

    @Test
    public void createTask_shouldNotAddTaskToTaskManagerTwice() {
        // make a task
        Task task = new Task("name","description", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 22);

        // add the task to the taskManager
        taskManager.createTask(task);

        // check that to add one task to taskManager twice throw exception
        Assertions.assertThrows(TaskIntersectionException.class, () -> taskManager.createTask(task));
    }
}