package tracker.service;

import tracker.tasks.AbstractTask;
import java.util.ArrayDeque;

public class InMemoryHistoryManager implements IHistoryManager {
    private static final int MAX_TASK_COUNT_IN_HISTORY = 10;
    private final ArrayDeque<AbstractTask> taskRequestHistory;

    public InMemoryHistoryManager() {
        taskRequestHistory = new ArrayDeque<>();
    }

    @Override
    public void add(AbstractTask task) {
        if (taskRequestHistory.size() == MAX_TASK_COUNT_IN_HISTORY) {
            taskRequestHistory.pop();
        }
        taskRequestHistory.addLast(task);
    }

    @Override
    public ArrayDeque<AbstractTask> getHistory() {
        return taskRequestHistory;
    }
}
