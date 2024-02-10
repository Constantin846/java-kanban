package tasktypes;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus taskStatus;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public TaskStatus getTaskStatus() { return taskStatus; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Task) o).id;
    }

    @Override
    public int hashCode() { return id; }

    @Override
    public String toString() {
        return String.format("\n%s:\n%s\nстатус - %s", name, description, taskStatus.toString());
    }
}
