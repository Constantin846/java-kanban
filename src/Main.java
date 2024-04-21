import tracker.service.FileBackedTaskManager;
import tracker.service.InMemoryHistoryManager;
import tracker.service.TaskManager;
import tracker.tasks.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class Main {
    private static final ZoneId UTC_PLUS_4 = ZoneId.of("UTC+4");

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());

        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4), 30);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(50), 48);

        Epic epicWithSubtask = new Epic("Эпик первый", "Эпик с подзадачами");
        Epic epicWithoutSubtask = new Epic("Эпик второй", "Без подзадач");

        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(200), 53);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(UTC_PLUS_4).minusMinutes(100), 12);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.NEW,
                ZonedDateTime.now(UTC_PLUS_4).plusMinutes(200), 180);

        fileBackedTaskManager.createTask(firstTask);
        fileBackedTaskManager.createTask(secondTask);
        fileBackedTaskManager.createEpic(epicWithSubtask);
        fileBackedTaskManager.createEpic(epicWithoutSubtask);
        fileBackedTaskManager.createSubtask(firstSubtask, epicWithSubtask);
        fileBackedTaskManager.createSubtask(secondSubtask, epicWithSubtask);
        fileBackedTaskManager.createSubtask(thirdSubtask, epicWithSubtask);

        fileBackedTaskManager.getTaskById(firstTask.getId());
        fileBackedTaskManager.getTaskById(firstTask.getId());
        fileBackedTaskManager.getEpicById(epicWithSubtask.getId());
        fileBackedTaskManager.getSubtaskById(firstSubtask.getId());
        fileBackedTaskManager.getTaskById(firstTask.getId());
        fileBackedTaskManager.getSubtaskById(secondSubtask.getId());
        fileBackedTaskManager.getTaskById(firstTask.getId());

        System.out.println(fileBackedTaskManager.getHistory());
        System.out.println("_______________________________________");

        TaskManager fbtm = new FileBackedTaskManager(new InMemoryHistoryManager());
        System.out.println(fbtm.getHistory());
        System.out.println("_______________________________________");

    }

    public static void printTaskHashMap(HashMap<Integer, AbstractTask> tasks) {
        for (Integer id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
        System.out.println("_______________________________________");
    }
}
