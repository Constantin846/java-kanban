package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.FileBackedTaskManager;
import tracker.service.InMemoryHistoryManager;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        Path actualFile;

        try {
            actualFile = Files.createTempFile("actual", ".csv");
        } catch (IOException ioe) {
            throw new RuntimeException();
        }
        return new FileBackedTaskManager(new InMemoryHistoryManager(), actualFile.toString());
    }
    @Test
    public void shouldLoadAndSaveEmptyFile() throws IOException {
        String[] inFile = {"id,type,name,status,description,start_time,duration,epic\n", "\n"};
        Path expectedFile = Files.createTempFile("expected-empty", ".csv");
        Path actualFile = Files.createTempFile("actual-empty", ".csv");

        try (FileWriter expectedFW = new FileWriter(expectedFile.toFile());
             FileWriter actualFW = new FileWriter(actualFile.toFile())) {

            for (String str : inFile) {
                expectedFW.write(str);
                actualFW.write(str);
            }
        }

        TaskManager fileBackedTaskManager =
                new FileBackedTaskManager(new InMemoryHistoryManager(), actualFile.toString());

        fileBackedTaskManager.removeAllTasks();

        List<String> expectedLines = Files.readAllLines(expectedFile);
        List<String> actualLines = Files.readAllLines(actualFile);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());

        for (int i = 0; i < expectedLines.size(); i++) {
            Assertions.assertEquals(expectedLines.get(i), actualLines.get(i));
        }
    }

    @Test
    public void shouldLoadAndSaveFileWithTasks() throws IOException {
        String[] inFile = {
                "id,type,name,status,description,start_time,duration,epic\n" +
                        "0,TASK,Первая задача,NEW,Описание для первой задачи,20.04.24 | 18:15,30,\n" +
                        "1,TASK,Вторая задача,IN_PROGRESS,Описание второй задачи,20.04.24 | 17:25,48,\n" +
                        "2,EPIC,Эпик первый,IN_PROGRESS,Эпик с подзадачами,20.04.24 | 14:55,245,\n" +
                        "3,EPIC,Эпик второй,NEW,Без подзадач,,0,\n" +
                        "4,SUBTASK,Подзадача 1,NEW,Подзадача первого эпика,20.04.24 | 14:55,53,2\n" +
                        "5,SUBTASK,Подзадача 2,IN_PROGRESS,Задача 1го эпика,20.04.24 | 16:35,12,2\n" +
                        "6,SUBTASK,Подзадача 3,NEW,3-я подзадача,20.04.24 | 21:35,180,2\n" +
                        "\n" +
                        "0,5,4,2\n"
        };

        Path expectedFile = Files.createTempFile("expected-test", ".csv");
        Path actualFile = Files.createTempFile("actual-test", ".csv");

        try (FileWriter expectedFW = new FileWriter(expectedFile.toFile());
             FileWriter actualFW = new FileWriter(actualFile.toFile())) {

            for (String str : inFile) {
                expectedFW.write(str);
                actualFW.write(str);
            }
        }

        TaskManager fileBackedTaskManager =
                new FileBackedTaskManager(new InMemoryHistoryManager(), actualFile.toString());

        Epic epic = new Epic("name", "description");
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.removeTaskById(epic.getId());

        List<String> expectedLines = Files.readAllLines(expectedFile);
        List<String> actualLines = Files.readAllLines(actualFile);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());

        for (int i = 0; i < expectedLines.size(); i++) {
            Assertions.assertEquals(expectedLines.get(i), actualLines.get(i));
        }
    }
}
