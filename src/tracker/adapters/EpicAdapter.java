package tracker.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.TaskBuilder;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EpicAdapter extends TypeAdapter<Epic> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    private static final ZoneId ZONE_ID = ZoneId.of("UTC+4");
    private final DateTimeFormatter dateTimeFormatter;
    private final ZoneId zoneId;
    private final SubtaskAdapter subtaskAdapter;

    public EpicAdapter() {
        this(DATE_TIME_FORMATTER, ZONE_ID);
    }

    public EpicAdapter(DateTimeFormatter dateTimeFormatter) {
        this(dateTimeFormatter, ZONE_ID);
    }

    public EpicAdapter(ZoneId zoneId) {
        this(DATE_TIME_FORMATTER, zoneId);
    }

    public EpicAdapter(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.zoneId = zoneId;
        this.subtaskAdapter = new SubtaskAdapter(this.dateTimeFormatter, this.zoneId);
    }

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject()
                .name("name").value(epic.getName())
                .name("description").value(epic.getDescription())
                .name("id").value(epic.getId())
                .name("taskStatus").value(epic.getTaskStatus().name())
                .name("taskType").value(epic.getTaskType().name());

        if (epic.getStartTime() != null) {
            jsonWriter.name("startTime").value(epic.getStartTime().format(dateTimeFormatter));
        }

        jsonWriter.name("durationOfMinutes").value(epic.getDuration().toMinutes());

        List<Subtask> subtasks = epic.getSubtasks();

        if (!subtasks.isEmpty()) {
            jsonWriter.name("subtasks").beginArray();

            for (Subtask subtask : subtasks) {
                subtaskAdapter.write(jsonWriter, subtask);
            }
            jsonWriter.endArray();
        }
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
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
                default:
                    jsonReader.nextString();
            }
        }
        jsonReader.endObject();
        return taskBuilder.buildEpic();
    }
}
