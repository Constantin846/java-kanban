package tracker.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tracker.service.TaskManager;
import tracker.tasks.AbstractTask;
import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.valueOf(exchange.getRequestMethod()) == HttpMethod.GET
                    && path.matches("^/history$")) {
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
        sendText(exchange, 200, gson.toJson(tasks));
    }
}
