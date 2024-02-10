import tasktypes.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Первая задача", "Описание для первой задачи", TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", TaskStatus.IN_PROGRESS);
        Epic firstEpic = new Epic("Эпик первый", "Описание первого эпика", TaskStatus.IN_PROGRESS);
        Epic secondEpic = new Epic("Эпик второй", "Это второй эпик", TaskStatus.DONE);
        Subtask firstSubtask = new Subtask("Подзадача 1", "Подзадача первого эпика", TaskStatus.NEW);
        Subtask secondSubtask = new Subtask("Подзадача 2", "Задача 1го эпика", TaskStatus.IN_PROGRESS);
        Subtask singleSubtask = new Subtask("Подзадача 3", "Одна подзадача эпика 2", TaskStatus.NEW);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubtask(firstSubtask, firstEpic);
        taskManager.createSubtask(secondSubtask, firstEpic);
        taskManager.createSubtask(singleSubtask, secondEpic);
        printTaskHashMap(taskManager.getAllTasks());

        Task newFirstTask = new Task(firstTask.getName(), firstTask.getDescription(), TaskStatus.DONE);
        Task newSecondTask = new Task(secondTask.getName(), secondTask.getDescription(), TaskStatus.DONE);
        Epic newFirstEpic = new Epic(firstEpic.getName(), firstEpic.getDescription(), TaskStatus.IN_PROGRESS);
        Epic newSecondEpic = new Epic(secondEpic.getName(), secondEpic.getDescription(), TaskStatus.NEW);
        Subtask newFirstSubtask = new Subtask(firstSubtask.getName(), firstSubtask.getDescription(), TaskStatus.DONE);
        Subtask newSecondSubtask = new Subtask(secondSubtask.getName(),secondSubtask.getDescription(),TaskStatus.DONE);
        Subtask newSingleSubtask = new Subtask(singleSubtask.getName(),singleSubtask.getDescription(),TaskStatus.DONE);

        taskManager.updateTask(firstTask, newFirstTask);
        taskManager.updateTask(secondTask, newSecondTask);
        taskManager.updateEpic(firstEpic, newFirstEpic);
        taskManager.updateEpic(secondEpic, newSecondEpic);
        taskManager.updateSubtask(firstSubtask, newFirstSubtask);
        taskManager.updateSubtask(secondSubtask, newSecondSubtask);
        taskManager.updateSubtask(singleSubtask, newSingleSubtask);
        printTaskHashMap(taskManager.getAllTasks());

        taskManager.removeTaskById(newFirstTask.getId());
        taskManager.removeTaskById(newFirstEpic.getId());
        taskManager.removeTaskById(newSingleSubtask.getId());
        printTaskHashMap(taskManager.getAllTasks());

        taskManager.removeAllTasks();
        printTaskHashMap(taskManager.getAllTasks());
    }

    public static void printTaskHashMap(HashMap<Integer, Task> tasks) {
        for (Integer id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
        System.out.println("_______________________________________");
    }
}
