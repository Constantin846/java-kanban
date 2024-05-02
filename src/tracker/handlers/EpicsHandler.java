package tracker.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import tracker.converters.EpicJsonConverter;
import tracker.converters.SubtaskJsonConverter;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {
    private final EpicJsonConverter epicJsonConverter;
    private final SubtaskJsonConverter subtaskJsonConverter;

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
        epicJsonConverter = new EpicJsonConverter();
        subtaskJsonConverter = new SubtaskJsonConverter();
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");

        if (path.matches("^/epics$")) {
            HashMap<Integer, Epic> epics = taskManager.getOnlyEpics();
            JsonObject jsonObject = epicJsonConverter.toJson(epics);
            sendText(exchange, 200, gson.toJson(jsonObject));

        } else if (path.matches("^/epics/(\\d+)$")) {
            Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (epic.isPresent()) {
                JsonObject jsonObject = epicJsonConverter.toJson(epic.get());
                sendText(exchange, 200, gson.toJson(jsonObject));
            } else {
                sendText(exchange, 404, "Epic Not Found");
            }

        } else if (path.matches("^/epics/(\\d+)/subtasks$")) {
            Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (epic.isPresent()) {
                HashMap<Integer, Subtask> subtasks = taskManager.getSubtasksOfEpic(epic.get());
                JsonObject jsonObject = subtaskJsonConverter.toJson(subtasks);
                sendText(exchange, 200, gson.toJson(subtasks));
            } else {
                sendText(exchange, 404, "Epic Not Found");
            }

        } else {
            sendText(exchange,404, "Not Found");
        }
    }

    @Override
    public void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        Epic epic = epicJsonConverter.fromJson(jsonObject);

        if (path.matches("^/epics$")) {
            taskManager.createEpic(epic);
            sendText(exchange, 201, "Epic Created");

        } else if (path.matches("^/epics/(\\d+)$")) {
            String[] splitPath = path.split("/");
            Optional<Epic> actualEpic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (actualEpic.isPresent()) {
                taskManager.updateEpic(actualEpic.get(), epic);
                sendText(exchange, 201, "Epic Updated");
            } else {
                sendText(exchange, 404, "Epic Not Found");
            }
        }
    }
}
