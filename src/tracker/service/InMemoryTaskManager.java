package tracker.service;

import tracker.exceptions.TaskIntersectionException;
import tracker.tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import static tracker.service.IdGenerator.generateId;

class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskHashMap;
    protected final HashMap<Integer, Epic> epicHashMap;
    protected final HashMap<Integer, Subtask> subtaskHashMap;
    protected final TreeSet<IntersectableTask> prioritizedTasksTreeSet;
    protected HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();

        prioritizedTasksTreeSet = new TreeSet<>((task1, task2) -> {
            if (task1.getEndTime().isBefore(task2.getStartTime())) {
                return -1;
            } else if (task1.getStartTime().isAfter(task2.getEndTime())) {
                return 1;
            } else {
                return 0;
            }
        });
        this.historyManager = historyManager;
    }

    @Override
    public HashMap<Integer, AbstractTask> getAllTasks() {
        HashMap<Integer, AbstractTask> allTask = new HashMap<>(taskHashMap);
        allTask.putAll(epicHashMap);
        allTask.putAll(subtaskHashMap);
        return allTask;
    }

    @Override
    public HashMap<Integer, Task> getOnlyTasks() {
        return new HashMap<>(taskHashMap);
    }

    @Override
    public HashMap<Integer, Epic> getOnlyEpics() {
        return new HashMap<>(epicHashMap);
    }

    @Override
    public HashMap<Integer, Subtask> getOnlySubtasks() {
        return new HashMap<>(subtaskHashMap);
    }

    @Override
    public TreeSet<IntersectableTask> getPrioritizedTasks() {
        return new TreeSet<>(prioritizedTasksTreeSet);
    }

    @Override
    public List<AbstractTask> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeAllTasks() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        prioritizedTasksTreeSet.clear();
        historyManager = new InMemoryHistoryManager();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            Task task = taskHashMap.get(id);
            historyManager.add(task);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            historyManager.add(epic);
            return Optional.of(epic);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            historyManager.add(subtask);
            return Optional.of(subtask);
        }
        return Optional.empty();
    }

    @Override
    public int createTask(Task task) {
        if (prioritizedTasksTreeSet.contains(task)) {
            throw new TaskIntersectionException();
        }

        int id = generateId();
        task.setId(id);
        taskHashMap.put(id, task);
        prioritizedTasksTreeSet.add(task);
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
        if (prioritizedTasksTreeSet.contains(subtask)) {
            throw new TaskIntersectionException();
        }

        ArrayList<Subtask> subtasks = epic.getSubtasks();
        subtasks.add(subtask);
        epic.setSubtasks(subtasks);
        subtask.setTopEpic(epic);

        int id = generateId();
        subtask.setId(id);
        subtaskHashMap.put(id, subtask);
        prioritizedTasksTreeSet.add(subtask);
        return id;
    }

    @Override
    public int updateTask(Task task, Task newTask) {
        prioritizedTasksTreeSet.remove(task);

        if (prioritizedTasksTreeSet.contains(newTask)) {
            prioritizedTasksTreeSet.add(task);
            throw new TaskIntersectionException();
        }

        int id = task.getId();
        newTask.setId(id);
        taskHashMap.put(id, newTask);
        prioritizedTasksTreeSet.add(newTask);
        return id;
    }

    @Override
    public int updateEpic(Epic epic, Epic newEpic) {
        ArrayList<Subtask> subtasks = epic.getSubtasks();

        for (Subtask subtask : subtasks) {
            subtask.setTopEpic(newEpic);
        }
        newEpic.setSubtasks(subtasks);

        int id = epic.getId();
        newEpic.setId(id);
        epicHashMap.put(id, newEpic);
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask, Subtask newSubtask) {
        prioritizedTasksTreeSet.remove(subtask);

        if (prioritizedTasksTreeSet.contains(newSubtask)) {
            prioritizedTasksTreeSet.add(subtask);
            throw new TaskIntersectionException();
        }

        Epic epic = subtask.getTopEpic();
        newSubtask.setTopEpic(epic);

        ArrayList<Subtask> subtasks = epic.getSubtasks();
        subtasks.remove(subtask);
        subtasks.add(newSubtask);
        epic.setSubtasks(subtasks);

        int id = subtask.getId();
        newSubtask.setId(id);
        subtaskHashMap.put(id, newSubtask);
        prioritizedTasksTreeSet.add(newSubtask);
        return id;
    }

    @Override
    public void removeTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id).setId(-1);
            historyManager.remove(id);
        } else if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.remove(id);
            historyManager.remove(id);
            epic.setId(-1);

            ArrayList<Subtask> subtasks = epic.getSubtasks();

            for (Subtask subtask : subtasks) {
                subtaskHashMap.remove(subtask.getId());
                historyManager.remove(subtask.getId());
                subtask.setId(-1);
            }

        } else if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.remove(id);
            historyManager.remove(id);
            subtask.setId(-1);

            Epic epic = subtask.getTopEpic();
            ArrayList<Subtask> subtasks = epic.getSubtasks();
            subtasks.remove(subtask);
            epic.setSubtasks(subtasks);
        }
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getSubtasks();

        return  (HashMap<Integer, Subtask>) subtasks.stream()
                .collect(Collectors.toMap(AbstractTask::getId, subtask -> subtask));
    }
}
