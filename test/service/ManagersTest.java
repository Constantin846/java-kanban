package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.Managers;

class ManagersTest {
    @Test
    public void getDefault_shouldNotReturnNull() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    public void getDefaultHistory_shouldNotReturnNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}