package tracker.service;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Ошибка при записи файла!");
    }
}
