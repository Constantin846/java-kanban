package tracker.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.tasks.Task;
import tracker.tasks.TaskBuilder;
import tracker.tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    private static final ZoneId ZONE_ID = ZoneId.of("UTC+4");
    private final DateTimeFormatter dateTimeFormatter;
    private final ZoneId zoneId;

    public TaskAdapter() {
        this(DATE_TIME_FORMATTER, ZONE_ID);
    }

    public TaskAdapter(DateTimeFormatter dateTimeFormatter) {
        this(dateTimeFormatter, ZONE_ID);
    }

    public TaskAdapter(ZoneId zoneId) {
        this(DATE_TIME_FORMATTER, zoneId);
    }

    public TaskAdapter (DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.zoneId = zoneId;
    }

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(task.getName());
        jsonWriter.name("description").value(task.getDescription());
        jsonWriter.name("id").value(task.getId());
        jsonWriter.name("taskStatus").value(task.getTaskStatus().name());
        jsonWriter.name("taskType").value(task.getTaskType().name());
        jsonWriter.name("startTime").value(task.getStartTime().format(dateTimeFormatter));
        jsonWriter.name("durationOfMinutes").value(task.getDuration().toMinutes());
        jsonWriter.endObject();
    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        TaskBuilder taskBuilder = new TaskBuilder();

        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "name":
                    taskBuilder.name(jsonReader.nextString());
                    break;
                case "description":
                    taskBuilder.description(jsonReader.nextString());
                    break;
                case "taskStatus":
                    taskBuilder.taskStatus(TaskStatus.valueOf(jsonReader.nextString()));
                    break;
                case "startTime":
                    taskBuilder.startTime(ZonedDateTime.of(
                            LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter), zoneId));
                    break;
                case "durationOfMinutes":
                    taskBuilder.duration(Duration.ofMinutes(jsonReader.nextLong()));
                    break;
            }
        }
        return taskBuilder.buildTask();
    }
}
