package tracker.service;

import tracker.tasks.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface TaskManager {
    HashMap<Integer, AbstractTask> getAllTasks();

    HashMap<Integer, Task> getOnlyTasks();

    HashMap<Integer, Epic> getOnlyEpics();

    HashMap<Integer, Subtask> getOnlySubtasks();

    void removeAllTasks();

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<Subtask> getSubtaskById(int id);

    TreeSet<IntersectableTask> getPrioritizedTasks();

    List<AbstractTask> getHistory();

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask, Epic epic);

    int updateTask(Task task, Task newTask);

    int updateEpic(Epic epic, Epic newEpic);

    int updateSubtask(Subtask subtask, Subtask newSubtask);

    void removeTaskById(int id);

    HashMap<Integer, Subtask> getSubtasksOfEpic(Epic epic);

    Optional<Epic> getTopEpicById(int id);
}
