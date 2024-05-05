package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import tracker.adapters.EpicAdapter;
import tracker.adapters.SubtaskAdapter;
import tracker.adapters.TaskAdapter;
import tracker.server.HttpTaskServer;
import tracker.service.Managers;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.Task;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HttpTaskManagerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer httpTaskServer;
    protected Gson gson;
    protected ZoneId zoneId = ZoneId.of("UTC+4");

    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");

    public void setUpServer() throws IOException {
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskAdapter(dateTimeFormatter))
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter()).create();

        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDownServer() {
        httpTaskServer.stop();
    }
}
