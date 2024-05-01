package tracker.tasks;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Subtask extends AbstractTask implements IntersectableTask {
    private Epic topEpic;

    public Subtask(String name, String description, TaskStatus taskStatus,
                   ZonedDateTime startTime, long durationOfMinutes) {
        super(name, description, taskStatus, startTime, durationOfMinutes);
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int topEpic, String name, String description, int id, TaskStatus taskStatus, TaskType taskType,
                 ZonedDateTime startTime, Duration duration) {
        super(name, description, id, taskStatus, taskType, startTime, duration);
        this.topEpic = null;
    }

    public Epic getTopEpic() {
        return topEpic;
    }

    public void setTopEpic(Epic epic) {
        topEpic = epic;
    }
}
