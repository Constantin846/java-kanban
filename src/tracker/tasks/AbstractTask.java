package tracker.tasks;

import tracker.service.TaskManager;

public abstract class AbstractTask {
    String name;
    String description;
    int id;
    TaskStatus taskStatus;
    TaskManager taskManager;

    public AbstractTask(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (!taskManager.containsId(id)) {
            this.id = id;
        }
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

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
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
}
