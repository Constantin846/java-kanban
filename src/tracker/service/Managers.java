package tracker.service;

public class Managers {
    private static final ITaskManager taskManager = new InMemoryTaskManager();
    private static final IHistoryManager historyManager = new InMemoryHistoryManager();

    public static ITaskManager getDefault() {
        return taskManager;
    }

    public static IHistoryManager getDefaultHistory() {
        return historyManager;
    }
}
