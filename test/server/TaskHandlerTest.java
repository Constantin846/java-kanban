package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest extends HttpTaskManagerTest {
    private Task firstTask;
    private Task secondTask;
    private String firstTaskJson;
    private String secondTaskJson;

    @BeforeEach
    public void beforeEach() throws IOException {
        setUpServer();
        firstTask = new Task("first", "first task", TaskStatus.NEW,
                ZonedDateTime.now(zoneId), 10);
        firstTaskJson = gson.toJson(firstTask);

        secondTask = new Task("second", "second task", TaskStatus.DONE,
                ZonedDateTime.now(zoneId).plusMinutes(30), 10);
        secondTaskJson = gson.toJson(secondTask);
    }

    @Test
    public void createTask_shouldAddNewTaskToTaskManager() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(firstTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Task> tasksFromManager = taskManager.getOnlyTasks();

        assertNotNull(tasksFromManager, "Task is not returned");
        assertEquals(1, tasksFromManager.size(), "Incorrect amount of tasks");
        assertEquals("first", tasksFromManager.get(firstTask.getId()).getName(),
                "Incorrect task name");
    }

    @Test
     public void updateTask_shouldUpdateTaskInTaskManager() throws IOException, InterruptedException {
        taskManager.createTask(firstTask);

        Task expectedTask = new Task("expectedName", "excepted", TaskStatus.IN_PROGRESS,
                ZonedDateTime.now(zoneId).plusMinutes(80), 10);
        String exceptedTaskJson = gson.toJson(expectedTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + firstTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(exceptedTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Task> tasksFromManager = taskManager.getOnlyTasks();

        assertNotNull(tasksFromManager, "Task is not returned");
        assertEquals(1, tasksFromManager.size(), "Incorrect amount of tasks");
        assertEquals("expectedName", tasksFromManager.get(firstTask.getId()).getName(),
                "Incorrect task name");
    }

    @Test
    public void getTask_shouldReturnTaskById() throws IOException, InterruptedException {
        taskManager.createTask(firstTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + firstTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String body = response.body();

        Task actualTask = gson.fromJson(body, Task.class);
        Task expectedTask = taskManager.getOnlyTasks().get(firstTask.getId());

        assertNotNull(actualTask, "Task is not returned");
        assertEquals(expectedTask.getName(), actualTask.getName());
        assertEquals(expectedTask.getDescription(), actualTask.getDescription());
        assertEquals(expectedTask.getTaskStatus(), actualTask.getTaskStatus());
        assertEquals(expectedTask.getStartTime().format(dateTimeFormatter),
                actualTask.getStartTime().format(dateTimeFormatter));
        assertEquals(expectedTask.getDuration(), actualTask.getDuration());
    }

    @Test
    public void getTasks_shouldReturnTasks() throws IOException, InterruptedException {
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String actualBody = response.body();
        String expectedBody = gson.toJson(taskManager.getOnlyTasks());

        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void deleteTask_shouldDeleteTaskById() throws IOException, InterruptedException {
        taskManager.createTask(firstTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + firstTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Map<Integer, Task> tasks = taskManager.getOnlyTasks();

        assertTrue(tasks.isEmpty());
    }
}
