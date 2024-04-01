package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.InMemoryHistoryManager;
import tracker.service.InMemoryTaskManager;
import tracker.service.TaskManager;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;

class SubtaskTest {
    Subtask subtaskExpected;
    Subtask subtaskActual;

    @BeforeEach
    public void beforeEach() {
        // prepare
        subtaskExpected = new Subtask("name", "description", TaskStatus.NEW);
        subtaskActual = new Subtask("name1", "description1", TaskStatus.NEW);
        TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        subtaskExpected.setTaskManager(taskManager);
        subtaskActual.setTaskManager(taskManager);
        subtaskExpected.setId(123);
    }

    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        // do
        subtaskActual.setId(subtaskExpected.getId());

        // check
        Assertions.assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        // do
        subtaskActual.setId(subtaskExpected.getId() + 1);

        // check
        Assertions.assertNotEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldNotAddSubtaskAsEpicOfItself() {
        // It is not possible to add the Subtask as an Epic to any Subtask
    }
}