package tracker.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import tracker.converters.TaskJsonConverter;
import tracker.exceptions.TaskIntersectionException;
import tracker.service.TaskManager;
import tracker.tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {
    private final TaskJsonConverter taskJsonConverter;

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
        taskJsonConverter = new TaskJsonConverter();
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        if (path.matches("^/tasks$")) {
            HashMap<Integer, Task> tasks = taskManager.getOnlyTasks();
            JsonObject jsonObject = taskJsonConverter.toJson(tasks);
            sendText(exchange, 200, gson.toJson(jsonObject));

        } else if (path.matches("^/tasks/(\\d+)$")) {
            String[] splitPath = path.split("/");
            Optional<Task> task = taskManager.getTaskById(Integer.parseInt(splitPath[ID_INDEX]));

            if (task.isPresent()) {
                JsonObject jsonObject = taskJsonConverter.toJson(task.get());
                sendText(exchange, 200, gson.toJson(jsonObject));
            } else {
                sendText(exchange, 404, "Task Not Found");
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
        Task task = taskJsonConverter.fromJson(jsonObject);

        try {
            if (path.matches("^/tasks$")) {
                taskManager.createTask(task);
                sendText(exchange, 201, "Task Created");

            } else if (path.matches("^/tasks/(\\d+)$")) {
                String[] splitPath = path.split("/");
                Optional<Task> actualTask = taskManager.getTaskById(Integer.parseInt(splitPath[ID_INDEX]));

                if (actualTask.isPresent()) {
                    taskManager.updateTask(actualTask.get(), task);
                    sendText(exchange, 201, "Task Updated");
                } else {
                    sendText(exchange, 404, "Task Not Found");
                }}  else {
                sendText(exchange,404, "Not Found");
            }

        } catch (TaskIntersectionException e) {
            sendText(exchange, 406, "Not Acceptable");
        }
    }
}
