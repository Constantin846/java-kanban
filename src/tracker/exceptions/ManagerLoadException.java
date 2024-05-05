package tracker.exceptions;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
        super("Error during file load!");
    }
}
