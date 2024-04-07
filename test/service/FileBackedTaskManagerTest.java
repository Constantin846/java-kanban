package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.FileBackedTaskManager;
import tracker.service.InMemoryHistoryManager;
import tracker.service.TaskManager;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileBackedTaskManagerTest {
    @Test
    public void shouldLoadAndSaveEmptyFile() throws IOException {
        // copy the empty file to a new actual file
        Path expectedFile = Paths.get("test\\service\\test-resources\\empty.csv");
        Path actualFile = Paths.get("test\\service\\test-resources\\test-empty.csv");
        Files.copy(expectedFile, actualFile, StandardCopyOption.REPLACE_EXISTING);

        // create a fileBackedTaskManager and load data from the actual file
        TaskManager fileBackedTaskManager =
                new FileBackedTaskManager(new InMemoryHistoryManager(), actualFile.toString());

        // save data to the actual file by removeAllTask method
        fileBackedTaskManager.removeAllTasks();

        // check lines in the actual file equals lines in the expected one
        List<String> expectedLines = Files.readAllLines(expectedFile);
        List<String> actualLines = Files.readAllLines(actualFile);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());

        for (int i = 0; i < expectedLines.size(); i++) {
            Assertions.assertEquals(expectedLines.get(i), actualLines.get(i));
        }
    }

    @Test
    public void shouldLoadAndSaveFileWithTasks() throws IOException {
        // copy the expected file to a new actual file
        Path expectedFile = Paths.get("test\\service\\test-resources\\tasks.csv");
        Path actualFile = Paths.get("test\\service\\test-resources\\test-tasks.csv");
        Files.copy(expectedFile, actualFile, StandardCopyOption.REPLACE_EXISTING);

        // create a fileBackedTaskManager and load data from the actual file
        TaskManager fileBackedTaskManager =
                new FileBackedTaskManager(new InMemoryHistoryManager(), actualFile.toString());

        // save data to the actual file by createTask and removeTaskById methods
        Task task = new Task("name", "description", TaskStatus.IN_PROGRESS);
        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.removeTaskById(task.getId());

        // check lines in the actual file equals lines in the expected one
        List<String> expectedLines = Files.readAllLines(expectedFile);
        List<String> actualLines = Files.readAllLines(actualFile);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());

        for (int i = 0; i < expectedLines.size(); i++) {
            Assertions.assertEquals(expectedLines.get(i), actualLines.get(i));
        }
    }
}
