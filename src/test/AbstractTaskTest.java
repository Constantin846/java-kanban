package test;

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
        taskExpected = new Task("name", "description", TaskStatus.NEW);
        taskActual = new Task("new name", "new description", TaskStatus.NEW);
        taskExpected.setId(123);
    }

    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        taskActual.setId(taskExpected.getId());
        Assertions.assertEquals(taskExpected, taskActual);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        taskActual.setId(taskExpected.getId() + 1);
        Assertions.assertNotEquals(taskExpected, taskActual);
    }
}