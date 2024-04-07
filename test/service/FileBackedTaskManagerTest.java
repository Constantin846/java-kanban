package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.service.FileBackedTaskManager;
import tracker.service.InMemoryHistoryManager;
import tracker.service.TaskManager;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FileBackedTaskManagerTest {
    @Test
    public void shouldLoadAndSaveEmptyFile() throws IOException {
        // make expected and actual empty file with a title string
        String[] inFile = {"id,type,name,status,description,epic\n", "\n"};
        Path expectedFile = Files.createTempFile("expected-empty",".csv");
        Path actualFile = Files.createTempFile("actual-empty",".csv");

        try (FileWriter expectedFW = new FileWriter(expectedFile.toFile());
             FileWriter actualFW = new FileWriter(actualFile.toFile())) {

            for (String str : inFile) {
                expectedFW.write(str);
                actualFW.write(str);
            }
        }

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
        // make expected and actual file with tasks
        String[] inFile = {
                "id,type,name,status,description,epic\n",
                "0,TASK,Первая задача,NEW,Описание для первой задачи,\n",
                "1,TASK,Вторая задача,IN_PROGRESS,Описание второй задачи,\n",
                "2,EPIC,Эпик первый,IN_PROGRESS,Эпик с подзадачами,\n",
                "3,EPIC,Эпик второй,NEW,Без подзадач,\n",
                "4,SUBTASK,Подзадача 1,NEW,Подзадача первого эпика,2\n",
                "5,SUBTASK,Подзадача 2,IN_PROGRESS,Задача 1го эпика,2\n",
                "6,SUBTASK,Подзадача 3,NEW,3-я подзадача,2\n",
                "\n",
                "0,5,4,2\n"
        };

        Path expectedFile = Files.createTempFile("expected-test",".csv");
        Path actualFile = Files.createTempFile("actual-test",".csv");

        try (FileWriter expectedFW = new FileWriter(expectedFile.toFile());
             FileWriter actualFW = new FileWriter(actualFile.toFile())) {

            for (String str : inFile) {
                expectedFW.write(str);
                actualFW.write(str);
            }
        }

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
