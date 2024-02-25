package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.AbstractTask;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;

class AbstractTaskTest {
    AbstractTask task;
    AbstractTask task1;

    @BeforeEach
    public void beforeEach() {
        task = new Task("name", "description", TaskStatus.NEW);
        task1 = new Task("name1", "description1", TaskStatus.NEW);
        task.setId(123);
    }
    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        task1.setId(task.getId());
        Assertions.assertEquals(task, task1);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        task1.setId(task.getId() + 1);
        Assertions.assertNotEquals(task, task1);
    }
}