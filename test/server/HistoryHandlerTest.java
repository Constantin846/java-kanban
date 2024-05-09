package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryHandlerTest extends HttpTaskManagerTest {
    private Task firstTask;
    private Task secondTask;
    private Epic epic;
    private Subtask firstSubtask;
    private Subtask secondSubtask;

    @BeforeEach
    public void beforeEach() throws IOException {
        setUpServer();
        firstTask = new Task("first", "first task", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 10);
        taskManager.createTask(firstTask);

        secondTask = new Task("second", "second task", TaskStatus.DONE,
                ZonedDateTime.now(zoneId).plusMinutes(30), 10);
        taskManager.createTask(secondTask);

        epic = new Epic("epic", "epic for subtasks");
        taskManager.createEpic(epic);

        firstSubtask = new Subtask("first", "first task", TaskStatus.NEW,
                ZonedDateTime.now(zoneId).plusMinutes(60), 10);
        taskManager.createSubtask(firstSubtask, epic);

        secondSubtask = new Subtask("second", "second task", TaskStatus.DONE,
                ZonedDateTime.now(zoneId).plusMinutes(90), 10);
        taskManager.createSubtask(secondSubtask, epic);
    }

    @Test
    public void getTask_shouldReturnTaskById() throws IOException, InterruptedException {
        taskManager.getTaskById(firstTask.getId());
        taskManager.getTaskById(secondTask.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(firstSubtask.getId());
        taskManager.getSubtaskById(secondSubtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String actualBody = response.body();
        String expectedBody = gson.toJson(taskManager.getHistory());

        assertEquals(expectedBody, actualBody);
    }
}
