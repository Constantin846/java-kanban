package tracker.service;

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
        return new InMemoryHistoryManager();
    }
}
