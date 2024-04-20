package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.FileBackedTaskManager;
import tracker.service.InMemoryHistoryManager;
import tracker.service.ManagerLoadException;

public class ExceptionTest {
    @Test
    public void testManagerLoadException() {
        Assertions.assertThrows(ManagerLoadException.class, () ->
                new FileBackedTaskManager(new InMemoryHistoryManager(), "./unreal/path"));
    }
}
