package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import tracker.exceptions.TaskIntersectionException;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

public class SubtasksHandler extends BaseTaskHandler {
    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        if (path.matches("^/subtasks$")) {
            HashMap<Integer, Subtask> subtasks = taskManager.getOnlySubtasks();
            sendText(exchange, 200, gson.toJson(subtasks));

        } else if (path.matches("^/subtasks/(\\d+)$")) {
            String[] splitPath = path.split("/");
            Optional<Subtask> subtask = taskManager.getSubtaskById(Integer.parseInt(splitPath[ID_INDEX]));

            if (subtask.isPresent()) {
                sendText(exchange, 200, gson.toJson(subtask.get()));
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
        Subtask subtask = gson.fromJson(jsonObject, Subtask.class);

        try {
            if (path.matches("^/subtasks$")) {
                Optional<Epic> epic = taskManager.getTopEpicById(epicId);

                if (epic.isPresent()) {
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
