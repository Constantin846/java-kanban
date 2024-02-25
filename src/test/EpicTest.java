package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import tracker.tasks.TaskStatus;

class EpicTest {
    Epic epicExpected;
    Epic epicActual;

    @BeforeEach
    public void beforeEach() {
        epicExpected = new Epic("name", "description", TaskStatus.NEW);
        epicActual = new Epic("new name", "new description", TaskStatus.NEW);
        epicExpected.setId(123);
    }

    @Test
    public void shouldBeEpicEqualsEpicIfTheirIdsEqual() {
        epicActual.setId(epicExpected.getId());
        Assertions.assertEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldBeNotEpicEqualsEpicIfTheirIdsDoNotEqual() {
        epicActual.setId(epicExpected.getId() + 1);
        Assertions.assertNotEquals(epicExpected, epicActual);
    }

    @Test
    public void shouldNotAddEpicAsSubtaskOfItself() {
        // It is not possible to add the Epic as a Subtask to any Epic
    }
}