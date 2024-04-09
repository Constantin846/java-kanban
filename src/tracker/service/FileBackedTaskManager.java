package tracker.service;

import tracker.tasks.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static tracker.service.IdGenerator.setIdSequence;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String TITLE_STRING_IN_FILE = "id,type,name,status,description,epic\n";
    private static final String DELIMITER = ",";
    private static final String TASK_REGEX = "^\\d+,[A-Z]+,.+,[A-Z_]+,.+,(\\d+|)$";
    private static final String HISTORY_REGEX = "^\\d+(.+|)$";
    private String path = "resources\\tasks.csv";

    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, "resources\\tasks.csv");
    }

    public FileBackedTaskManager(HistoryManager historyManager, String filePath) {
        super(historyManager);
        this.path = filePath;
        loadFromFile();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        saveToFile();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        saveToFile();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        saveToFile();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        saveToFile();
        return subtask;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        saveToFile();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        saveToFile();
        return  id;
    }

    @Override
    public int createSubtask(Subtask subtask, Epic epic) {
        int id = super.createSubtask(subtask, epic);
        saveToFile();
        return id;
    }

    @Override
    public int updateTask(Task task, Task newTask) {
        int id = super.updateTask(task, newTask);
        saveToFile();
        return id;
    }

    @Override
    public int updateEpic(Epic epic, Epic newEpic) {
        int id = super.updateEpic(epic, newEpic);
        saveToFile();
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask, Subtask newSubtask) {
        int id = super.updateSubtask(subtask, newSubtask);
        saveToFile();
        return id;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        saveToFile();
    }

    private void saveToFile() {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(TITLE_STRING_IN_FILE);

            for (Integer id : taskHashMap.keySet()) {
                fileWriter.write(taskToString(taskHashMap.get(id)));
            }

            for (Integer id : epicHashMap.keySet()) {
                fileWriter.write(taskToString(epicHashMap.get(id)));
            }

            for (Integer id : subtaskHashMap.keySet()) {
                fileWriter.write(taskToString(subtaskHashMap.get(id)));
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString());

        } catch (IOException ioe) {
            throw new ManagerSaveException();
        }
    }

    private String taskToString(AbstractTask task) {
        String epic = "\n";

        if (task instanceof Subtask) {
            int epicId = ((Subtask) task).getTopEpic().getId();
            epic = epicId + "\n";
        }

        return String.join(DELIMITER, Integer.toString(task.getId()), task.getTaskType().name(),
                task.getName(), task.getTaskStatus().name(), task.getDescription(), epic);
    }

    private String historyToString() {
        List<AbstractTask> history = historyManager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        if (!history.isEmpty()) {
            stringBuilder.append(history.get(0).getId());

            for (int i = 1; i < history.size(); i++) {
                stringBuilder.append(DELIMITER);
                stringBuilder.append(history.get(i).getId());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private void loadFromFile() {
        HashMap<Subtask, Integer> epicIdOfSubtask = new HashMap<>();
        List<Integer> history = null;

        try {
            List<String> input = Files.readAllLines(Path.of(path));

            for (String item : input) {
                if (item.matches(TASK_REGEX)) {
                    AbstractTask task = taskFromString(item);
                    setIdSequence(task.getId());

                    if (task instanceof Task) {
                        taskHashMap.put(task.getId(), (Task) task);
                    } else if (task instanceof Epic) {
                        epicHashMap.put(task.getId(), (Epic) task);

                    } else if (task instanceof Subtask) {
                        Subtask subtask = (Subtask) task;
                        subtaskHashMap.put(subtask.getId(), subtask);
                        String[] fields = item.split(DELIMITER);
                        epicIdOfSubtask.put(subtask, Integer.parseInt(fields[5]));
                    }
                } else if (item.matches(HISTORY_REGEX)) {
                    history = historyFromString(item);
                }
            }
        } catch (IOException ioe) {
            throw new ManagerLoadException();
        }

        ArrayList<Subtask> subtasks;

        for (Subtask subtask : epicIdOfSubtask.keySet()) {
            int epicId = epicIdOfSubtask.get(subtask);
            Epic epic = epicHashMap.get(epicId);
            subtasks = epic.getSubtasks();
            subtasks.add(subtask);
            epic.setSubtasks(subtasks);
            subtask.setTopEpic(epic);
        }

        if (history != null) {
            for (int i = history.size() - 1; i > -1; i--) {
                if (taskHashMap.containsKey(history.get(i))) {
                    historyManager.add(taskHashMap.get(history.get(i)));
                } else if (epicHashMap.containsKey(history.get(i))) {
                    historyManager.add(epicHashMap.get(history.get(i)));
                } else if (subtaskHashMap.containsKey(history.get(i))) {
                    historyManager.add(subtaskHashMap.get(history.get(i)));
                }
            }
        }
    }

    private AbstractTask taskFromString(String value) {
        String[] fields = value.split(DELIMITER);

        if (fields[1].equals(TaskType.TASK.name())) {
            Task task = new Task(fields[2], fields[4], TaskStatus.valueOf(fields[3]));
            task.setId(Integer.parseInt(fields[0]));
            return task;
        } else if (fields[1].equals(TaskType.EPIC.name())) {
            Epic epic = new Epic(fields[2], fields[4], TaskStatus.valueOf(fields[3]));
            epic.setId(Integer.parseInt(fields[0]));
            return epic;
        } else if (fields[1].equals(TaskType.SUBTASK.name())) {
            Subtask subtask = new Subtask(fields[2], fields[4], TaskStatus.valueOf(fields[3]));
            subtask.setId(Integer.parseInt(fields[0]));
            return subtask;
        }
        return null;
    }

    private List<Integer> historyFromString(String value) {
        String[] ids = value.split(DELIMITER);
        List<Integer> history = new ArrayList<>();

        for (String id : ids) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }
}
