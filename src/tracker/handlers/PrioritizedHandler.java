package tracker.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tracker.service.TaskManager;
import tracker.tasks.IntersectableTask;
import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.valueOf(exchange.getRequestMethod()) == HttpMethod.GET
                    && path.matches("^/prioritized$")) {
                handleGetRequest(exchange);
            } else {
                sendText(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            sendText(exchange, 500, "Internal Server Error");
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws IOException {
        TreeSet<IntersectableTask> prioritizedTasks = taskManager.getPrioritizedTasks();
        sendText(exchange, 200, gson.toJson(prioritizedTasks));
    }
}
