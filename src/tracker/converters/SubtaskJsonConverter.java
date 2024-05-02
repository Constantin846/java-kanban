package tracker.converters;

import com.google.gson.JsonObject;
import tracker.tasks.Subtask;
import tracker.tasks.TaskStatus;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskJsonConverter extends AbstractJsonConverter<Subtask> {
    public SubtaskJsonConverter(DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter);
    }

    public SubtaskJsonConverter() {
    }

    @Override
    public JsonObject toJson(Subtask subtask) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", subtask.getName());
        jsonObject.addProperty("description", subtask.getDescription());
        jsonObject.addProperty("id", subtask.getId());
        jsonObject.addProperty("taskStatus", subtask.getTaskStatus().name());
        jsonObject.addProperty("taskType", subtask.getTaskType().name());
        jsonObject.addProperty("startTime", subtask.getStartTime().format(dateTimeFormatter));
        jsonObject.addProperty("durationOfMinutes", subtask.getDuration().toMinutes());
        jsonObject.addProperty("topEpic", subtask.getTopEpic().getId());
        return jsonObject;
    }

    @Override
    public Subtask fromJson(JsonObject jsonObject) {
        return new Subtask(
                jsonObject.get("name").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString()),
                ZonedDateTime.of(LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                                dateTimeFormatter), zoneId),
                jsonObject.get("durationOfMinutes").getAsLong()
        );
    }
}
