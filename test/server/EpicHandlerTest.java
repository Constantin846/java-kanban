package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.tasks.Epic;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest extends HttpTaskManagerTest {
    private Epic firstEpic;
    private Epic secondEpic;
    private String firstEpicJson;
    private String secondEpicJson;

    @BeforeEach
    public void beforeEach() throws IOException {
        setUpServer();
        firstEpic = new Epic("first", "first task");
        firstEpicJson = gson.toJson(firstEpic);

        secondEpic = new Epic("second", "second task");
        secondEpicJson = gson.toJson(secondEpic);
    }

    @Test
    public void createEpic_shouldAddNewEpicToTaskManager() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(firstEpicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Epic> tasksFromManager = taskManager.getOnlyEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("first", tasksFromManager.get(firstEpic.getId()).getName(),
                "Некорректное имя задачи");
    }

    @Test
     public void updateEpic_shouldUpdateEpicInTaskManager() throws IOException, InterruptedException {
        taskManager.createEpic(firstEpic);

        Epic expectedEpic = new Epic("expectedName", "excepted");
        String exceptedTaskJson = gson.toJson(expectedEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + firstEpic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(exceptedTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Epic> epicsFromManager = taskManager.getOnlyEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("expectedName", epicsFromManager.get(firstEpic.getId()).getName(),
                "Некорректное имя задачи");
    }

    @Test
    public void getEpic_shouldReturnEpicById() throws IOException, InterruptedException {
        taskManager.createEpic(firstEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + firstEpic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String body = response.body();

        Epic actualEpic = gson.fromJson(body, Epic.class);
        Epic expectedEpic = taskManager.getOnlyEpics().get(firstEpic.getId());

        assertNotNull(actualEpic, "Задачи не возвращаются");
        assertEquals(expectedEpic.getName(), actualEpic.getName());
        assertEquals(expectedEpic.getDescription(), actualEpic.getDescription());
        assertEquals(expectedEpic.getTaskStatus(), actualEpic.getTaskStatus());
        assertEquals(expectedEpic.getDuration(), actualEpic.getDuration());
    }

    @Test
    public void getEpics_shouldReturnEpics() throws IOException, InterruptedException {
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        String actualBody = response.body();
        String expectedBody = gson.toJson(taskManager.getOnlyEpics());

        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void deleteEpic_shouldDeleteEpicById() throws IOException, InterruptedException {
        taskManager.createEpic(firstEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + firstEpic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Map<Integer, Epic> tasks = taskManager.getOnlyEpics();

        assertTrue(tasks.isEmpty());
    }
}
