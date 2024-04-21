package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.TaskManager;
import tracker.service.Managers;
import tracker.tasks.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private Task taskActual;
    private TaskManager inMemoryTaskManager;
    private ZoneId zoneId;

    @BeforeEach
    public void beforeEach() {
        // create a task and add it to the taskManager
        zoneId = ZoneId.of("UTC+4");
        taskActual = new Task("name", "description", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 1);

        inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(taskActual);
    }

    @Test
    public void shouldBeEmptyHistoryIsNotNull() {
        List<AbstractTask> history = inMemoryTaskManager.getHistory();
        int historySizeExpected = 0;

        Assertions.assertEquals(historySizeExpected, history.size());
        Assertions.assertNotNull(history);
    }

    @Test
    public void add_shouldNotAddOneTaskTwice() {
        inMemoryTaskManager.getTaskById(taskActual.getId());
        inMemoryTaskManager.getTaskById(taskActual.getId());

        List<AbstractTask> history = inMemoryTaskManager.getHistory();
        int historySizeExpected = 1;

        Assertions.assertEquals(historySizeExpected, history.size());
    }

    @Test
    public void remove_shouldRemoveFirstTask() {
        List<AbstractTask> tasksExpected = createTaskListAndAddToTaskManager();

        inMemoryTaskManager.removeTaskById(tasksExpected.get(0).getId());
        tasksExpected.remove(0);
        List<AbstractTask> history = inMemoryTaskManager.getHistory();

        for (int i = 0; i < tasksExpected.size(); i++) {
            Assertions.assertEquals(tasksExpected.get(i), history.get(history.size() - 1 - i));
        }
    }

    @Test
    public void remove_shouldRemoveMiddleTask() {
        List<AbstractTask> tasksExpected = createTaskListAndAddToTaskManager();

        inMemoryTaskManager.removeTaskById(tasksExpected.get(1).getId());
        tasksExpected.remove(1);
        List<AbstractTask> history = inMemoryTaskManager.getHistory();

        for (int i = 0; i < tasksExpected.size(); i++) {
            Assertions.assertEquals(tasksExpected.get(i), history.get(history.size() - 1 - i));
        }
    }

    @Test
    public void remove_shouldRemoveEndTask() {
        List<AbstractTask> tasksExpected = createTaskListAndAddToTaskManager();

        inMemoryTaskManager.removeTaskById(tasksExpected.get(tasksExpected.size() - 1).getId());
        tasksExpected.remove(tasksExpected.size() - 1);
        List<AbstractTask> history = inMemoryTaskManager.getHistory();

        for (int i = 0; i < tasksExpected.size(); i++) {
            Assertions.assertEquals(tasksExpected.get(i), history.get(history.size() - 1 - i));
        }
    }

    @Test
    public void add_shouldSaveLastFieldsOfGetTask() {
        // get the task from the taskManager by id
        inMemoryTaskManager.getTaskById(taskActual.getId());

        // create a second task
        Task expectedTask = new Task("new name", "new description", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId), 15);

        // update the first task to the second task
        inMemoryTaskManager.updateTask(taskActual, expectedTask);
        // get the task from the taskManager
        inMemoryTaskManager.getTaskById(taskActual.getId());
        // get the task from the historyManager
        Task taskFromHistory = (Task) inMemoryTaskManager.getHistory().get(0);

        // check that fields of task, gotten from the historyManager, equal fields of second task
        Assertions.assertEquals(expectedTask.getName(), taskFromHistory.getName());
        Assertions.assertEquals(expectedTask.getDescription(), taskFromHistory.getDescription());
        Assertions.assertEquals(expectedTask.getTaskStatus(), taskFromHistory.getTaskStatus());
        Assertions.assertEquals(expectedTask.getStartTime(), taskFromHistory.getStartTime());
        Assertions.assertEquals(expectedTask.getDuration(), taskFromHistory.getDuration());
    }

    private List<AbstractTask> createTaskListAndAddToTaskManager() {
        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW,
                ZonedDateTime.now(zoneId).plusMinutes(5), 0);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).minusMinutes(5), 0);
        Epic epic = new Epic("Эпик первый", "Эпик с подзадачами");

        inMemoryTaskManager.createTask(firstTask);
        inMemoryTaskManager.createTask(secondTask);
        inMemoryTaskManager.createEpic(epic);

        inMemoryTaskManager.getTaskById(firstTask.getId());
        inMemoryTaskManager.getTaskById(secondTask.getId());
        inMemoryTaskManager.getEpicById(epic.getId());

        List<AbstractTask> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);
        tasks.add(epic);
        return tasks;
    }
}