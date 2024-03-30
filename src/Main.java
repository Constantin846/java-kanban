import tracker.service.TaskManager;
import tracker.service.Managers;
import tracker.tasks.*;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS);
        Epic epicWithSubtask = new Epic("Эпик первый", "Эпик с подзадачами", TaskStatus.IN_PROGRESS);
        Epic epicWithoutSubtask = new Epic("Эпик второй", "Без подзадач", TaskStatus.DONE);
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.IN_PROGRESS);
        Subtask thirdSubtask = new Subtask("Подзадача 3", "3-я подзадача", TaskStatus.NEW);

        inMemoryTaskManager.createTask(firstTask);
        inMemoryTaskManager.createTask(secondTask);
        inMemoryTaskManager.createEpic(epicWithSubtask);
        inMemoryTaskManager.createEpic(epicWithoutSubtask);
        inMemoryTaskManager.createSubtask(firstSubtask, epicWithSubtask);
        inMemoryTaskManager.createSubtask(secondSubtask, epicWithSubtask);
        inMemoryTaskManager.createSubtask(thirdSubtask, epicWithSubtask);

        inMemoryTaskManager.getTaskById(firstTask.getId());
        inMemoryTaskManager.getTaskById(firstTask.getId());
        inMemoryTaskManager.getEpicById(epicWithSubtask.getId());
        inMemoryTaskManager.getSubtaskById(firstSubtask.getId());
        inMemoryTaskManager.getTaskById(firstTask.getId());
        inMemoryTaskManager.getSubtaskById(secondSubtask.getId());
        inMemoryTaskManager.getTaskById(firstTask.getId());

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("_______________________________________");

        inMemoryTaskManager.removeTaskById(firstTask.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("_______________________________________");

        inMemoryTaskManager.removeTaskById(epicWithSubtask.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("_______________________________________");
    }

    public static void printTaskHashMap(HashMap<Integer, AbstractTask> tasks) {
        for (Integer id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
        System.out.println("_______________________________________");
    }
}
