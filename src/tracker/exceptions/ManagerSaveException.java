package tracker.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Ошибка при записи файла!");
    }
}
