package tracker.service;

import tracker.tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import static tracker.service.IdGenerator.generateId;

public class InMemoryTaskManager implements ITaskManager {
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;

    public InMemoryTaskManager() {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
    }

    @Override
    public HashMap<Integer, AbstractTask> getAllTasks() {
        HashMap<Integer, AbstractTask> allTask = new HashMap<>(taskHashMap);
        allTask.putAll(epicHashMap);
        allTask.putAll(subtaskHashMap);
        return allTask;
    }

    @Override
    public HashMap<Integer, Task> getOnlyTasks() { return taskHashMap; }

    @Override
    public HashMap<Integer, Epic> getOnlyEpics() { return epicHashMap; }

    @Override
    public HashMap<Integer, Subtask> getOnlySubtasks() { return subtaskHashMap; }

    @Override
    public void removeAllTasks() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            Task task = taskHashMap.get(id);
            Managers.getDefaultHistory().add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            Managers.getDefaultHistory().add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Managers.getDefaultHistory().add(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public int createTask(Task task) {
        int id = generateId();
        task.setId(id);
        taskHashMap.put(id, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epicHashMap.put(id, epic);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask, Epic epic) {
        int id = generateId();
        subtask.setTopEpic(epic);
        subtask.setId(id);
        subtaskHashMap.put(id, subtask);
        return id;
    }

    @Override
    public int updateTask(Task task, Task newTask) {
        int id = task.getId();
        newTask.setId(id);
        taskHashMap.put(id, newTask);
        return id;
    }

    @Override
    public int updateEpic(Epic epic, Epic newEpic) {
        int id = epic.getId();
        newEpic.setSubtasks(epic.getSubtasks());
        newEpic.setId(id);
        epicHashMap.put(id, newEpic);
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask, Subtask newSubtask) {
        int id = subtask.getId();
        subtask.removeFromTopEpic();
        newSubtask.setTopEpic(subtask.getTopEpic());
        newSubtask.setId(id);
        subtaskHashMap.put(id, newSubtask);
        return id;
    }

    @Override
    public void removeTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        } else if (epicHashMap.containsKey(id)) {
            ArrayList<Subtask> subtasks = epicHashMap.remove(id).getSubtasks();

            for (Subtask subtask : subtasks) {
                subtaskHashMap.remove(subtask.getId());
            }

        } else if (subtaskHashMap.containsKey(id)) {
            subtaskHashMap.remove(id).removeFromTopEpic();
        }
    }

    public HashMap<Integer, Subtask> getSubtasksOfEpic(Epic epic) {
        HashMap<Integer, Subtask> subtasksOfEpic = new HashMap<>();
        ArrayList<Subtask> subtasks = epic.getSubtasks();

        for (Subtask subtask : subtasks) {
            subtasksOfEpic.put(subtask.getId(), subtask);
        }
        return subtasksOfEpic;
    }
}
