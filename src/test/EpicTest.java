package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import tracker.tasks.TaskStatus;

class EpicTest {
    Epic epic;
    Epic epic1;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("name", "description", TaskStatus.NEW);
        epic1 = new Epic("name1", "description1", TaskStatus.NEW);
        epic.setId(123);
    }

    @Test
    public void shouldBeEpicEqualsEpicIfTheirIdsEqual() {
        epic1.setId(epic.getId());
        Assertions.assertEquals(epic, epic1);
    }

    @Test
    public void shouldBeNotEpicEqualsEpicIfTheirIdsDoNotEqual() {
        epic1.setId(epic.getId() + 1);
        Assertions.assertNotEquals(epic, epic1);
    }

    @Test
    public void shouldNotAddEpicAsSubtaskOfItself() {
        // It is not possible to add the Epic as a Subtask to any Epic
    }
}