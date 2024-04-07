package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.AbstractTask;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;

class AbstractTaskTest {
    AbstractTask taskExpected;
    AbstractTask taskActual;

    @BeforeEach
    public void beforeEach() {
        // prepare
        taskExpected = new Task("name", "description", TaskStatus.NEW);
        taskActual = new Task("new name", "new description", TaskStatus.NEW);
        taskExpected.setId(123);
    }

    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        // do
        taskActual.setId(taskExpected.getId());

        // check
        Assertions.assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        // do
        taskActual.setId(taskExpected.getId() + 1);

        // check
        Assertions.assertNotEquals(taskExpected, taskActual);
    }
}