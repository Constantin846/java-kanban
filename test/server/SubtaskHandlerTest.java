package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest extends HttpTaskManagerTest {
    private Subtask firstSubtask;
    private Subtask secondSubtask;
    private Epic epic;
    private String firstSubtaskJson;
    private String secondSubtaskJson;

    @BeforeEach
    public void beforeEach() throws IOException {
        setUpServer();
        epic = new Epic("epic", "epic for subtasks");
        taskManager.createEpic(epic);

        firstSubtask = new Subtask("first", "first task", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 10);
        firstSubtask.setTopEpic(epic);
        firstSubtaskJson = gson.toJson(firstSubtask);

        secondSubtask = new Subtask("second", "second task", TaskStatus.DONE,
                ZonedDateTime.now(zoneId).plusMinutes(30), 10);
        secondSubtask.setTopEpic(epic);
        secondSubtaskJson = gson.toJson(secondSubtask);
    }

    @Test
    public void createSubtask_shouldAddNewSubtaskToTaskManager() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(firstSubtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Subtask> tasksFromManager = taskManager.getOnlySubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("first", tasksFromManager.get(1).getName(),
                "Некорректное имя задачи");
    }

    @Test
     public void updateSubtask_shouldUpdateSubtaskInTaskManager() throws IOException, InterruptedException {
        taskManager.createSubtask(firstSubtask, epic);

        Subtask expectedSubtaskTask = new Subtask("expectedName", "excepted", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).plusMinutes(80), 10);
        expectedSubtaskTask.setTopEpic(epic);
        String exceptedTaskJson = gson.toJson(expectedSubtaskTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + firstSubtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(exceptedTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Subtask> tasksFromManager = taskManager.getOnlySubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("expectedName", tasksFromManager.get(firstSubtask.getId()).getName(),
                "Некорректное имя задачи");
    }

    @Test
    public void getSubtask_shouldReturnSubtaskById() throws IOException, InterruptedException {
        taskManager.createSubtask(firstSubtask, epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + firstSubtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String body = response.body();

        Subtask actualSubtask = gson.fromJson(body, Subtask.class);
        Subtask expectedSubtask = taskManager.getOnlySubtasks().get(firstSubtask.getId());

        assertNotNull(actualSubtask, "Задачи не возвращаются");
        assertEquals(expectedSubtask.getName(), actualSubtask.getName());
        assertEquals(expectedSubtask.getDescription(), actualSubtask.getDescription());
        assertEquals(expectedSubtask.getTaskStatus(), actualSubtask.getTaskStatus());
        assertEquals(expectedSubtask.getStartTime().format(dateTimeFormatter),
                actualSubtask.getStartTime().format(dateTimeFormatter));
        assertEquals(expectedSubtask.getDuration(), actualSubtask.getDuration());
    }

    @Test
    public void getSubtasks_shouldReturnSubtasks() throws IOException, InterruptedException {
        taskManager.createSubtask(firstSubtask, epic);
        taskManager.createSubtask(secondSubtask,epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String actualBody = response.body();
        String expectedBody = gson.toJson(taskManager.getOnlySubtasks());

        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void deleteSubtask_shouldDeleteSubtaskById() throws IOException, InterruptedException {
        taskManager.createSubtask(firstSubtask, epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + firstSubtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Map<Integer, Subtask> subtasks = taskManager.getOnlySubtasks();

        assertTrue(subtasks.isEmpty());
    }
}
