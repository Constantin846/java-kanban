package tracker.service;

import tracker.tasks.*;
import java.util.HashMap;

public interface ITaskManager {
    HashMap<Integer, AbstractTask> getAllTasks();

    HashMap<Integer, Task> getOnlyTasks();

    HashMap<Integer, Epic> getOnlyEpics();

    HashMap<Integer, Subtask> getOnlySubtasks();

    void removeAllTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask, Epic epic);

    int updateTask(Task task, Task newTask);

    int updateEpic(Epic epic, Epic newEpic);

    int updateSubtask(Subtask subtask, Subtask newSubtask);

    void removeTaskById(int id);
}
