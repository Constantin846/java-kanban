package tracker.service;

import tracker.service.history.HistoryManager;
import tracker.service.history.HistoryManagerGetter;

public class Managers {
    private Managers() {
    }

    public static TaskManager getFileBacked() {
        return new FileBackedTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileBacked(String filePath) {
        return new FileBackedTaskManager(getDefaultHistory(), filePath);
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return HistoryManagerGetter.getDefaultHistory();
    }
}
