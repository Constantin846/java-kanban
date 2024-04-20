package tracker.tasks;

import java.time.ZonedDateTime;

public class Subtask extends AbstractTask implements IntersectableTask {
    private Epic topEpic;

    public Subtask(String name, String description, TaskStatus taskStatus,
                   ZonedDateTime startTime, int durationOfMinutes) {
        super(name, description, taskStatus, startTime, durationOfMinutes);
        this.taskType = TaskType.SUBTASK;
    }

    public Epic getTopEpic() {
        return topEpic;
    }

    public void setTopEpic(Epic epic) {
        topEpic = epic;
    }
}
