import tracker.tasks.*;
import java.util.HashMap;
import static tracker.service.Managers.getDefault;
import static tracker.service.Managers.getDefaultHistory;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS);
        Epic firstEpic = new Epic("Эпик первый", "Описание первого эпика", TaskStatus.IN_PROGRESS);
        Epic secondEpic = new Epic("Эпик второй", "Это второй эпик", TaskStatus.DONE);
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.IN_PROGRESS);
        Subtask singleSubtask = new Subtask("Подзадача 3", "Одна подзадача эпика 2", TaskStatus.NEW);

        getDefault().createTask(firstTask);
        getDefault().createTask(secondTask);
        getDefault().createEpic(firstEpic);
        getDefault().createEpic(secondEpic);
        getDefault().createSubtask(firstSubtask, firstEpic);
        getDefault().createSubtask(secondSubtask, firstEpic);
        getDefault().createSubtask(singleSubtask, secondEpic);
        printTaskHashMap(getDefault().getAllTasks());

        getDefault().getTaskById(firstTask.getId());
        getDefault().getTaskById(secondTask.getId());
        getDefault().getEpicById(firstEpic.getId());
        getDefault().getEpicById(secondEpic.getId());
        getDefault().getSubtaskById(firstSubtask.getId());
        getDefault().getSubtaskById(secondSubtask.getId());
        getDefault().getSubtaskById(singleSubtask.getId());
        System.out.println(getDefaultHistory().getHistory());
        System.out.println("_______________________________________");

        Task newFirstTask = new Task(firstTask.getName(), firstTask.getDescription(), TaskStatus.DONE);
        Task newSecondTask = new Task(secondTask.getName(), secondTask.getDescription(), TaskStatus.DONE);
        Epic newFirstEpic = new Epic(firstEpic.getName(), firstEpic.getDescription(), TaskStatus.IN_PROGRESS);
        Epic newSecondEpic = new Epic(secondEpic.getName(), secondEpic.getDescription(), TaskStatus.NEW);
        Subtask newFirstSubtask = new Subtask(firstSubtask.getName(), firstSubtask.getDescription(), TaskStatus.DONE);
        Subtask newSecondSubtask = new Subtask(secondSubtask.getName(),secondSubtask.getDescription(),TaskStatus.DONE);
        Subtask newSingleSubtask = new Subtask(singleSubtask.getName(),singleSubtask.getDescription(),TaskStatus.DONE);

        getDefault().updateTask(firstTask, newFirstTask);
        getDefault().updateTask(secondTask, newSecondTask);
        getDefault().updateEpic(firstEpic, newFirstEpic);
        getDefault().updateEpic(secondEpic, newSecondEpic);
        getDefault().updateSubtask(firstSubtask, newFirstSubtask);
        getDefault().updateSubtask(secondSubtask, newSecondSubtask);
        getDefault().updateSubtask(singleSubtask, newSingleSubtask);
        printTaskHashMap(getDefault().getAllTasks());

        getDefault().getTaskById(newFirstEpic.getId());
        getDefault().getEpicById(newFirstEpic.getId());
        getDefault().getSubtaskById(singleSubtask.getId());
        getDefault().getSubtaskById(newSecondSubtask.getId());
        System.out.println(getDefaultHistory().getHistory());
        System.out.println("_______________________________________");

        getDefault().removeTaskById(newFirstTask.getId());
        getDefault().removeTaskById(newFirstEpic.getId());
        getDefault().removeTaskById(newSingleSubtask.getId());
        printTaskHashMap(getDefault().getAllTasks());

        getDefault().getTaskById(newSecondTask.getId());
        getDefault().getEpicById(newSecondEpic.getId());
        getDefault().getSubtaskById(singleSubtask.getId());
        getDefault().getSubtaskById(newSecondSubtask.getId());
        System.out.println(getDefaultHistory().getHistory());
        System.out.println("_______________________________________");

        getDefault().removeAllTasks();
        printTaskHashMap(getDefault().getAllTasks());
        System.out.println(getDefaultHistory().getHistory());
    }

    public static void printTaskHashMap(HashMap<Integer, AbstractTask> tasks) {
        for (Integer id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
        System.out.println("_______________________________________");
    }
}
