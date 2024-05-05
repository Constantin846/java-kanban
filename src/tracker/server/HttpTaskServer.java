package tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import tracker.adapters.EpicAdapter;
import tracker.adapters.SubtaskAdapter;
import tracker.adapters.TaskAdapter;
import tracker.handlers.*;
import tracker.service.Managers;
import tracker.service.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.Task;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter()).create();

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler(this.taskManager, gson));
        server.createContext("/epics", new EpicsHandler(this.taskManager, gson));
        server.createContext("/subtasks", new SubtasksHandler(this.taskManager, gson));
        server.createContext("/history", new HistoryHandler(this.taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(this.taskManager, gson));
    }

    public void start() {
        server.start();
        System.out.println("Started HttpTaskServer");
    }

    public void stop() {
        server.stop(0);
        System.out.println("Stopped HttpTaskServer");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getFileBacked());
        httpTaskServer.start();
    }
}
