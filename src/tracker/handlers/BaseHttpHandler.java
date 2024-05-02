package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.adapters.TaskAdapter;
import tracker.service.TaskManager;
import tracker.tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

abstract class BaseHttpHandler implements HttpHandler {
    protected static final int ID_INDEX = 2;
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");

    protected final Gson gson;
    protected final TaskManager taskManager;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            switch (HttpMethod.valueOf(exchange.getRequestMethod())) {
                case GET:
                    handleGetRequest(exchange, path);
                    break;
                case POST:
                    handlePostRequest(exchange, path);
                    break;
                case DELETE:
                    handleDeleteRequest(exchange, path);
                    break;
                default:
                    sendText(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            sendText(exchange, 500, "Internal Server Error");
        }
    }

    protected abstract void handleGetRequest(HttpExchange exchange, String path) throws IOException;

    protected abstract void handlePostRequest(HttpExchange exchange, String path) throws IOException;

    protected void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        if (path.matches("^/(tasks|epics|subtasks)/(\\d+)$")) {
            String[] splitPath = path.split("/");

            taskManager.removeTaskById(Integer.parseInt(splitPath[ID_INDEX]));
            sendText(exchange, 200, "Задача удалена");
        }
    }

    protected void sendText(HttpExchange exchange, int rCode, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
