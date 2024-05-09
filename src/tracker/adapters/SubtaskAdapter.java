package tracker.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.tasks.Subtask;
import tracker.tasks.TaskBuilder;
import tracker.tasks.TaskStatus;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    private static final ZoneId ZONE_ID = ZoneId.of("UTC+4");
    private final DateTimeFormatter dateTimeFormatter;
    private final ZoneId zoneId;

    public SubtaskAdapter() {
        this(DATE_TIME_FORMATTER, ZONE_ID);
    }

    public SubtaskAdapter(DateTimeFormatter dateTimeFormatter) {
        this(dateTimeFormatter, ZONE_ID);
    }

    public SubtaskAdapter(ZoneId zoneId) {
        this(DATE_TIME_FORMATTER, zoneId);
    }

    public SubtaskAdapter(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.zoneId = zoneId;
    }

    @Override
    public void write(JsonWriter jsonWriter, Subtask subtask) throws IOException {
        jsonWriter.beginObject()
                .name("name").value(subtask.getName())
                .name("description").value(subtask.getDescription())
                .name("id").value(subtask.getId())
                .name("taskStatus").value(subtask.getTaskStatus().name())
                .name("taskType").value(subtask.getTaskType().name())
                .name("startTime").value(subtask.getStartTime().format(dateTimeFormatter))
                .name("durationOfMinutes").value(subtask.getDuration().toMinutes())
                .name("topEpic").value(subtask.getTopEpic().getId())
                .endObject();
    }

    @Override
    public Subtask read(JsonReader jsonReader) throws IOException {
        TaskBuilder taskBuilder = new TaskBuilder();
        jsonReader.beginObject();

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
                default:
                    jsonReader.nextString();
            }
        }
        jsonReader.endObject();
        return taskBuilder.buildSubtask();
    }
}
