package tracker.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tracker.service.TaskManager;
import java.io.IOException;

abstract class BaseTaskHandler extends BaseHttpHandler {
    protected static final int ID_INDEX = 2;

    protected BaseTaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
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
            sendText(exchange, 200, "Task Deleted");
        }
    }
}
