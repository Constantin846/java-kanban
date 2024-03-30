package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.Managers;

class ManagersTest {
    @Test
    public void shouldNotBeGetDefaultReturnNull() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    public void shouldNotBeGetDefaultHistoryReturnNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}