import tasktypes.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskCount;
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;

    public TaskManager() {
        taskCount = 0;
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
    }

    public HashMap<Integer, Task> getAllTasks() {
        HashMap<Integer, Task> allTask = new HashMap<>(taskHashMap);
        allTask.putAll(epicHashMap);
        allTask.putAll(subtaskHashMap);
        return allTask;
    }

    public HashMap<Integer, Task> getOnlyTasks() { return taskHashMap; }

    public HashMap<Integer, Epic> getOnlyEpics() { return epicHashMap; }

    public HashMap<Integer, Subtask> getOnlySubtasks() { return subtaskHashMap; }

    public void removeAllTasks() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public Task getTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            return taskHashMap.get(id);
        } else if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id);
        } else if (subtaskHashMap.containsKey(id)) {
            return subtaskHashMap.get(id);
        }
        return null;
    }


    public void createTask(Task task) {
        task.setId(taskCount);
        taskHashMap.put(taskCount++, task);
    }

    public void createEpic(Epic epic) {
        epic.setId(taskCount);
        epicHashMap.put(taskCount++, epic);
    }

    public void createSubtask(Subtask subtask, Epic epic) {
        subtask.setTopEpic(epic);
        subtask.setId(taskCount);
        subtaskHashMap.put(taskCount++, subtask);
    }

    public void updateTask(Task task, Task newTask) {
        newTask.setId(task.getId());
        taskHashMap.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic epic, Epic newEpic) {
        newEpic.setSubtasks(epic.getSubtasks());
        newEpic.setId(epic.getId());
        epicHashMap.put(newEpic.getId(), newEpic);
    }

    public void updateSubtask(Subtask subtask, Subtask newSubtask) {
        subtask.removeFromTopEpic();
        newSubtask.setTopEpic(subtask.getTopEpic());
        newSubtask.setId(subtask.getId());
        subtaskHashMap.put(newSubtask.getId(), newSubtask);
    }

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
