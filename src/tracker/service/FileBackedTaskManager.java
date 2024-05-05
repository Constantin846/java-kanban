package tracker.service;

import tracker.exceptions.ManagerLoadException;
import tracker.exceptions.ManagerSaveException;
import tracker.service.history.HistoryManager;
import tracker.tasks.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String TITLE_STRING_IN_FILE = "id,type,name,status,description,start_time,duration,epic\n";
    private static final int ID_INDEX = 0;
    private static final int TYPE_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int STATUS_INDEX = 3;
    private static final int DESCRIPTION_INDEX = 4;
    private static final int START_TIME_INDEX = 5;
    private static final int DURATION_INDEX = 6;
    private static final int EPIC_INDEX = 7;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    private static final ZoneId ZONE_ID = ZoneId.of("UTC+4");
    private static final String DELIMITER = ",";
    private static final String TASK_REGEX = "^\\d+,[A-Z]+,.+,[A-Z_]+,.+,(\\d+|)$";
    private static final String HISTORY_REGEX = "^\\d+(.+|)$";
    private static final String DEFAULT_PATH = "resources\\tasks.csv";
    private final String path;

    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, DEFAULT_PATH);
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
    public Optional<Task> getTaskById(int id) {
        Optional<Task> task = super.getTaskById(id);
        saveToFile();
        return task;
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Optional<Epic> epic = super.getEpicById(id);
        saveToFile();
        return epic;
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        Optional<Subtask> subtask = super.getSubtaskById(id);
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

        String startTime = "";

        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(DATE_TIME_FORMATTER);
        }

        return String.join(DELIMITER, Integer.toString(task.getId()), task.getTaskType().name(),
                task.getName(), task.getTaskStatus().name(), task.getDescription(),
                startTime, Long.toString(task.getDuration().toMinutes()), epic);
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
                    AbstractTask currentTask = taskFromString(item);
                    idGenerator.setIdSequence(currentTask.getId());

                    if (currentTask instanceof Task) {
                        Task task = (Task) currentTask;
                        taskHashMap.put(task.getId(), task);
                        prioritizedTasksTreeSet.add(task);

                    } else if (currentTask instanceof Epic) {
                        epicHashMap.put(currentTask.getId(), (Epic) currentTask);

                    } else if (currentTask instanceof Subtask) {
                        Subtask subtask = (Subtask) currentTask;
                        subtaskHashMap.put(subtask.getId(), subtask);
                        prioritizedTasksTreeSet.add(subtask);
                        String[] fields = item.split(DELIMITER);
                        epicIdOfSubtask.put(subtask, Integer.parseInt(fields[EPIC_INDEX]));

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

        if (fields[TYPE_INDEX].equals(TaskType.EPIC.name())) {
            Epic epic = new Epic(fields[NAME_INDEX], fields[DESCRIPTION_INDEX]);
            epic.setId(Integer.parseInt(fields[ID_INDEX]));
            return epic;

        } else {
            ZonedDateTime startTime = ZonedDateTime.of(
                    LocalDateTime.parse(fields[START_TIME_INDEX], DATE_TIME_FORMATTER), ZONE_ID);

            if (fields[TYPE_INDEX].equals(TaskType.TASK.name())) {
                Task task = new Task(fields[NAME_INDEX], fields[DESCRIPTION_INDEX],
                        TaskStatus.valueOf(fields[STATUS_INDEX]),
                        startTime, Integer.parseInt(fields[DURATION_INDEX]));

                task.setId(Integer.parseInt(fields[ID_INDEX]));
                return task;
            } else if (fields[TYPE_INDEX].equals(TaskType.SUBTASK.name())) {
                Subtask subtask = new Subtask(fields[NAME_INDEX], fields[DESCRIPTION_INDEX],
                        TaskStatus.valueOf(fields[STATUS_INDEX]),
                        startTime, Integer.parseInt(fields[DURATION_INDEX]));

                subtask.setId(Integer.parseInt(fields[ID_INDEX]));
                return subtask;
            }
        }
        return null;
    }

    private List<Integer> historyFromString(String value) {
        String[] ids = value.split(DELIMITER);

        return Arrays.stream(ids)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
