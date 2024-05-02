package tracker.converters;

import com.google.gson.JsonObject;
import tracker.tasks.Task;
import tracker.tasks.TaskStatus;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TaskJsonConverter extends AbstractJsonConverter<Task> {
    public TaskJsonConverter(DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter);
    }

    public TaskJsonConverter() {
    }

    @Override
    public JsonObject toJson(Task task) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", task.getName());
        jsonObject.addProperty("description", task.getDescription());
        jsonObject.addProperty("id", task.getId());
        jsonObject.addProperty("taskStatus", task.getTaskStatus().name());
        jsonObject.addProperty("taskType", task.getTaskType().name());
        jsonObject.addProperty("startTime", task.getStartTime().format(dateTimeFormatter));
        jsonObject.addProperty("durationOfMinutes", task.getDuration().toMinutes());
        return jsonObject;
    }

    @Override
    public Task fromJson(JsonObject jsonObject) {
        return new Task(
                jsonObject.get("name").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString()),
                ZonedDateTime.of(LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                                dateTimeFormatter), zoneId),
                jsonObject.get("durationOfMinutes").getAsLong()
        );
    }
}
