package tracker.tasks;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TaskBuilder {
    private String name = "no name";
    private String description = "empty";
    private TaskStatus taskStatus = TaskStatus.NEW;
    private ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC+4"));
    private Duration duration = Duration.ZERO;

    public TaskBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder taskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public TaskBuilder startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public TaskBuilder duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public Task buildTask() {
        return new Task(name, description, taskStatus, startTime, duration.toMinutes());
    }

    public Epic buildEpic() {
        return new Epic(name, description);
    }

    public Subtask buildSubtask() {
        return new Subtask(name, description, taskStatus, startTime, duration.toMinutes());
    }
}
