package tracker.service;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
        super("Ошибка при загрузке файла!");
    }
}
