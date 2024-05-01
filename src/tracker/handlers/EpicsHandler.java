package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tracker.adapters.EpicToIdAdapter;
import tracker.adapters.ListSubtasksToIdsAdapter;
import tracker.handlers.BaseHttpHandler;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {
    private final ListSubtasksToIdsAdapter listSubtasksToIdsAdapter;
    private final EpicToIdAdapter epicToIdAdapter;

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
        listSubtasksToIdsAdapter = new ListSubtasksToIdsAdapter();
        epicToIdAdapter = new EpicToIdAdapter();
    }

    @Override
    public void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String[] splitPath = path.split("/");

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter)
                .registerTypeAdapter(Epic.class, listSubtasksToIdsAdapter)
                .registerTypeAdapter(Duration.class, durationAdapter).create();

        if (path.matches("^/(epics)$")) {
            HashMap<Integer, Epic> epics = taskManager.getOnlyEpics();
            sendText(exchange, 200, gson.toJson(epics));

        } else if (path.matches("^/(epics)/(\\d+)$")) {
            Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (epic.isPresent()) {
                sendText(exchange, 200, gson.toJson(epic.get()));
            } else {
                sendText(exchange, 404, "Epic Not Found");
            }

        } else if (path.matches("^/(epics)/(\\d+)/(subtasks)$")) {
            Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (epic.isPresent()) {
                Gson currentGson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                        .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter)
                        .registerTypeAdapter(Epic.class, epicToIdAdapter).create();

                HashMap<Integer, Subtask> subtasks = taskManager.getSubtasksOfEpic(epic.get());
                sendText(exchange, 200, currentGson.toJson(subtasks));
            } else {
                sendText(exchange, 404, "Epic Not Found");
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

        Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, zonedDateTimeAdapter).create();
        Epic epic = gson.fromJson(body, Epic.class);

        if (path.matches("^/(epics)$")) {
            taskManager.createEpic(epic);
            sendText(exchange, 201, "Epic Created");

        } else if (path.matches("^/(epics)/(\\d+)$")) {
            Optional<Epic> actualEpic = taskManager.getEpicById(Integer.parseInt(splitPath[ID_INDEX]));

            if (actualEpic.isPresent()) {
                taskManager.updateEpic(actualEpic.get(), epic);
                sendText(exchange, 201, "Epic Updated");
            } else {
                sendText(exchange, 404, "Epic Not Found");
            }
        }
    }
}
