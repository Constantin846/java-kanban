package tracker.service.history;

public class HistoryManagerGetter {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
