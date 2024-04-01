package tracker.tasks;

public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    DONE;

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', ' ');
    }
}
