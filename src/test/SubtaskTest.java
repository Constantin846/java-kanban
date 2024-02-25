package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;

class SubtaskTest {
    Subtask subtask;
    Subtask subtask1;

    @BeforeEach
    public void beforeEach() {
        subtask = new Subtask("name", "description", TaskStatus.NEW);
        subtask1 = new Subtask("name1", "description1", TaskStatus.NEW);
        subtask.setId(123);
    }

    @Test
    public void shouldBeAbstractTaskEqualsAbstractTaskIfTheirIdsEqual() {
        subtask1.setId(subtask.getId());
        Assertions.assertEquals(subtask, subtask1);
    }

    @Test
    public void shouldBeNotAbstractTaskEqualsAbstractTaskIfTheirIdsDoNotEqual() {
        subtask1.setId(subtask.getId() + 1);
        Assertions.assertNotEquals(subtask, subtask1);
    }

    @Test
    public void shouldNotAddSubtaskAsEpicOfItself() {
        // It is not possible to add the Subtask as an Epic to any Subtask
    }
}