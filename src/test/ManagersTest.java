package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import tracker.service.Managers;

class ManagersTest {
    @Test
    public void shouldNotBeGetDefaultReturnNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void shouldNotBeGetDefaultHistoryReturnNull() {
        assertNotNull(Managers.getDefaultHistory());
    }
}