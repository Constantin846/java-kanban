package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;

class SubtaskTest {
    Subtask subtaskExpected;
    Subtask subtaskActual;

    @BeforeEach
    public void beforeEach() {
        subtaskExpected = new Subtask("name", "description", TaskStatus.NEW);
        subtaskActual = new Subtask("name1", "description1", TaskStatus.NEW);
        subtaskExpected.setId(123);
    }

    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        subtaskActual.setId(subtaskExpected.getId());
        Assertions.assertEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        subtaskActual.setId(subtaskExpected.getId() + 1);
        Assertions.assertNotEquals(subtaskExpected, subtaskActual);
    }

    @Test
    public void shouldNotAddSubtaskAsEpicOfItself() {
        // It is not possible to add the Subtask as an Epic to any Subtask
    }
}