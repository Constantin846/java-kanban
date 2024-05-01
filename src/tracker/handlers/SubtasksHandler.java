package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tracker.adapters.EpicToIdAdapter;
import tracker.exceptions.TaskIntersectionException;
import tracker.handlers.BaseHttpHandler;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler {
    private final EpicToIdAdapter epicToIdAdapter;

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
        epicToIdAdapter = new EpicToIdAdapter();
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter)
                .registerTypeAdapter(Duration.class, durationAdapter)
                .registerTypeAdapter(Epic.class, epicToIdAdapter).create();

        if (path.matches("^/(subtasks)$")) {
            HashMap<Integer, Subtask> subtasks = taskManager.getOnlySubtasks();
            String toJson = gson.toJson(subtasks);
            sendText(exchange, 200, toJson);

        } else if (path.matches("^/(subtasks)/(\\d+)$")) {
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
        String[] splitPath = path.split("/");

        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter)
                .registerTypeAdapter(Duration.class, durationAdapter).create();
        Subtask subtask = gson.fromJson(body, Subtask.class);

        try {
            if (path.matches("^/(subtasks)$")) {
                taskManager.createSubtask(subtask, subtask.getTopEpic());
                sendText(exchange, 201, "Subtask Created");
            } else if (path.matches("^/(subtasks)/(\\d+)$")) {
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
