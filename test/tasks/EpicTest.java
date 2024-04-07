package tasks;

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
        // prepare
        epicExpected = new Epic("name", "description", TaskStatus.NEW);
        epicActual = new Epic("new name", "new description", TaskStatus.NEW);
        epicExpected.setId(123);
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