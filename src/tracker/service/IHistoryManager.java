package tracker.service;

import tracker.tasks.AbstractTask;
import java.util.ArrayDeque;



public interface IHistoryManager {
    void add(AbstractTask task);

    ArrayDeque<AbstractTask> getHistory();
}
