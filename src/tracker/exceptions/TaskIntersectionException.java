package tracker.exceptions;

public class TaskIntersectionException extends RuntimeException {
    public TaskIntersectionException() {
        super("Пересечение времени выполнения задач!");
    }
}
