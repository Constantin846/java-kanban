package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.adapters.ZonedDateTimeAdapter;
import tracker.handlers.HttpMethod;
import tracker.service.TaskManager;
import tracker.tasks.IntersectableTask;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

public class PrioritizedHandler implements HttpHandler {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    private final ZonedDateTimeAdapter zonedDateTimeAdapter;
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        zonedDateTimeAdapter = new ZonedDateTimeAdapter(DATE_TIME_FORMATTER);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (HttpMethod.valueOf(exchange.getRequestMethod()) == HttpMethod.GET) {
                handleGetRequest(exchange);
            } else {
                sendText(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            sendText(exchange, 500, "Internal Server Error");
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter).create();

        TreeSet<IntersectableTask> prioritizedTasks = taskManager.getPrioritizedTasks();
        sendText(exchange, 200, gson.toJson(prioritizedTasks));
    }

    public void sendText(HttpExchange exchange, int rCode, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
