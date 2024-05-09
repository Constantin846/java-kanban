package tracker.tasks;

import java.time.Duration;
import java.time.ZonedDateTime;

public abstract class AbstractTask {
    String name;
    String description;
    int id;
    TaskStatus taskStatus;
    TaskType taskType;
    ZonedDateTime startTime;
    Duration duration;

    protected AbstractTask(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected AbstractTask(String name, String description, TaskStatus taskStatus,
                           ZonedDateTime startTime, long durationOfMinutes) {
        this(name, description);
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(durationOfMinutes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((AbstractTask) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("\n%s:\n%s\nстатус - %s", name, description, taskStatus.toString());
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }
}
