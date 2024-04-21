package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SubtaskTest {
    Subtask subtaskExpected;
    Subtask subtaskActual;

    @BeforeEach
    public void beforeEach() {
        // prepare
        ZoneId utc_4 = ZoneId.of("UTC+4");
        subtaskExpected = new Subtask("name", "description", TaskStatus.NEW,
                ZonedDateTime.now(utc_4), 10);
        subtaskActual = new Subtask("name1", "description1", TaskStatus.NEW,
                ZonedDateTime.now(utc_4), 15);
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