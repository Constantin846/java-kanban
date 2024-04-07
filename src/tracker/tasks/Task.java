package tracker.tasks;

public class Task extends AbstractTask {
    public Task(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.taskType = TaskType.TASK;
    }
}
