package tracker.tasks;

import java.time.ZonedDateTime;

public class Task extends AbstractTask implements IntersectableTask {
    public Task(String name, String description, TaskStatus taskStatus,
                ZonedDateTime startTime, long durationOfMinutes) {
        super(name, description, taskStatus, startTime, durationOfMinutes);
        this.taskType = TaskType.TASK;
    }
}
