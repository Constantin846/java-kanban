package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.exceptions.ManagerLoadException;
import tracker.service.Managers;

class ExceptionTest {
    @Test
    public void testManagerLoadException() {
        Assertions.assertThrows(ManagerLoadException.class, () ->
                Managers.getFileBacked("./unreal/path"));
    }
}
