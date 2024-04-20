package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

class EpicTest {
    Epic epicExpected;
    Epic epicActual;
    static ZoneId UTC_PLUS_4 = ZoneId.of("UTC+4");

    @BeforeEach
    public void beforeEach() {
        // prepare
        epicExpected = new Epic("name", "description");
        epicActual = new Epic("new name", "new description");
        epicExpected.setId(123);
    }

    @Test
    public void determineEpicStatus_asNew_ifAddAllNewSubtasks() {
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(2), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(10), 120);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).plusMinutes(20), 180);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(firstSubtask);
        subtasks.add(secondSubtask);
        subtasks.add(thirdSubtask);
        epicActual.setSubtasks(subtasks);

        Assertions.assertEquals(TaskStatus.NEW, epicActual.getTaskStatus());
    }

    @Test
    public void determineEpicStatus_asDone_ifAddAllDoneSubtasks() {
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.DONE,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(2), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.DONE,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(10), 120);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.DONE,
                ZonedDateTime.now(UTC_PLUS_4).plusMinutes(20), 180);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(firstSubtask);
        subtasks.add(secondSubtask);
        subtasks.add(thirdSubtask);
        epicActual.setSubtasks(subtasks);

        Assertions.assertEquals(TaskStatus.DONE, epicActual.getTaskStatus());
    }

    @Test
    public void determineEpicStatus_asInProgress_ifAddNewAndDoneSubtasks() {
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(2), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.DONE,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(10), 120);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.DONE,
                ZonedDateTime.now(UTC_PLUS_4).plusMinutes(20), 180);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(firstSubtask);
        subtasks.add(secondSubtask);
        subtasks.add(thirdSubtask);
        epicActual.setSubtasks(subtasks);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicActual.getTaskStatus());
    }

    @Test
    public void determineEpicStatus_asInProgress_ifAddInProgressSubtasks() {
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика",
                TaskStatus.IN_PROGRESS, ZonedDateTime.now(UTC_PLUS_4).minusMinutes(2), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика",
                TaskStatus.IN_PROGRESS, ZonedDateTime.now(UTC_PLUS_4).minusMinutes(10), 120);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача",
                TaskStatus.IN_PROGRESS, ZonedDateTime.now(UTC_PLUS_4).plusMinutes(20), 180);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(firstSubtask);
        subtasks.add(secondSubtask);
        subtasks.add(thirdSubtask);
        epicActual.setSubtasks(subtasks);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicActual.getTaskStatus());
    }

    @Test
    public void shouldBeEpicEqualsEpicIfTheirIdsEqual() {
        // do
        epicActual.setId(epicExpected.getId());

        // check
        Assertions.assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeNotEpicEqualsEpicIfTheirIdsDoNotEqual() {
        // do
        epicActual.setId(epicExpected.getId() + 1);

        // check
        Assertions.assertNotEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldNotAddEpicAsSubtaskOfItself() {
        // It is not possible to add the Epic as a Subtask to any Epic
    }
}