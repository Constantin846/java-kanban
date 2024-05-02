package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.converters.AbstractTaskJsonConverter;
import tracker.service.TaskManager;
import tracker.tasks.AbstractTask;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private final AbstractTaskJsonConverter abstractTaskJsonConverter;
    private final Gson gson;
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        abstractTaskJsonConverter = new AbstractTaskJsonConverter();
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.valueOf(exchange.getRequestMethod()) == HttpMethod.GET
                    && path.matches("^/history$") ) {
                handleGetRequest(exchange);
            } else {
                sendText(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            sendText(exchange, 500, "Internal Server Error");
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws IOException {
        List<AbstractTask> tasks = taskManager.getHistory();
        JsonArray jsonArray = abstractTaskJsonConverter.toJson(tasks);
        sendText(exchange, 200, gson.toJson(jsonArray));
    }

    public void sendText(HttpExchange exchange, int rCode, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
