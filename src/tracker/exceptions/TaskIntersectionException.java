package tracker.exceptions;

public class TaskIntersectionException extends RuntimeException {
    public TaskIntersectionException() {
        super("Error: task execution times intersection!");
    }
}
