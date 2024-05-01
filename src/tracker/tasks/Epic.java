package tracker.tasks;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Epic extends AbstractTask {
    private ArrayList<Subtask> subtasks;
    private ZonedDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
        this.duration = Duration.ZERO;
        determineEpicStatus();
    }

    public  Epic(String name, String description, int id, TaskStatus taskStatus, TaskType taskType,
                 ZonedDateTime startTime, Duration duration) {
        super(name, description, id, taskStatus, taskType, startTime, duration);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
        determineEpicStatus();
        determineEpicDurationAndStartEndTime();
    }

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    private void determineEpicStatus() {
        if (subtasks.isEmpty()) {
            taskStatus = TaskStatus.NEW;
        } else {
            for (int i = 1; i < subtasks.size(); i++) {
                if (subtasks.get(i).getTaskStatus() != subtasks.get(i - 1).getTaskStatus()) {
                    taskStatus = TaskStatus.IN_PROGRESS;
                    return;
                }
            }
            taskStatus = subtasks.get(0).getTaskStatus();
        }
    }

    private void determineEpicDurationAndStartEndTime() {
        if (!subtasks.isEmpty()) {
            Subtask subtask = subtasks.get(0);
            startTime = subtask.getStartTime();
            endTime = subtask.getEndTime();
            duration = subtask.getDuration();

            for (int i = 1; i < subtasks.size(); i++) {
                subtask = subtasks.get(i);

                if (startTime.isAfter(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }

                if (endTime.isBefore(subtask.getEndTime())) {
                    endTime = subtask.getEndTime();
                }
                duration = duration.plus(subtask.getDuration());
            }
        }
    }
}
