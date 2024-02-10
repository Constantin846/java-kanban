package tasktypes;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        subtasks = new ArrayList<>();
        this.taskStatus = TaskStatus.NEW;
    }

    public ArrayList<Subtask> getSubtasks() { return subtasks; }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;

        for (Subtask subtask : subtasks) {
            subtask.changeTopEpic(this);
        }
        setEpicStatus();
    }

    void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        setEpicStatus();
    }

    void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        setEpicStatus();
    }

    private void setEpicStatus() {
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
}
