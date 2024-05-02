package tracker.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import tracker.converters.SubtaskJsonConverter;
import tracker.exceptions.TaskIntersectionException;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler {
    private final SubtaskJsonConverter subtaskJsonConverter;

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
        subtaskJsonConverter = new SubtaskJsonConverter();
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        if (path.matches("^/subtasks$")) {
            HashMap<Integer, Subtask> subtasks = taskManager.getOnlySubtasks();
            JsonObject jsonObject = subtaskJsonConverter.toJson(subtasks);
            sendText(exchange, 200, gson.toJson(jsonObject));

        } else if (path.matches("^/subtasks/(\\d+)$")) {
            String[] splitPath = path.split("/");
            Optional<Subtask> subtask = taskManager.getSubtaskById(Integer.parseInt(splitPath[ID_INDEX]));

            if (subtask.isPresent()) {
                JsonObject jsonObject = subtaskJsonConverter.toJson(subtask.get());
                sendText(exchange, 200, gson.toJson(jsonObject));
            } else {
                sendText(exchange, 404, "Subtask Not Found");
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
        int epicId = jsonObject.get("topEpic").getAsInt();
        Subtask subtask = subtaskJsonConverter.fromJson(jsonObject);

        try {
            if (path.matches("^/subtasks$")) {
                Optional<Epic> epic = taskManager.getEpicById(epicId);

                if (epic.isPresent()){
                    taskManager.createSubtask(subtask, epic.get());
                    sendText(exchange, 201, "Subtask Created");
                } else {
                    sendText(exchange, 404, "Subtask Not Found");
                }

            } else if (path.matches("^/subtasks/(\\d+)$")) {
                String[] splitPath = path.split("/");
                Optional<Subtask> actualSubtask = taskManager.getSubtaskById(Integer.parseInt(splitPath[ID_INDEX]));

                if (actualSubtask.isPresent()) {
                    taskManager.updateSubtask(actualSubtask.get(), subtask);
                    sendText(exchange, 201, "Subtask Updated");
                } else {
                    sendText(exchange, 404, "Subtask Not Found");
                }
            } else {
                sendText(exchange,404, "Not Found");
            }
        } catch (TaskIntersectionException e) {
            sendText(exchange, 406, "Not Acceptable");
        }
    }
}
