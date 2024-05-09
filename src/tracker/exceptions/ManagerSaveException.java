package tracker.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Error during file save!");
    }
}
