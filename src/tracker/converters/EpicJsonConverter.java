package tracker.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EpicJsonConverter extends AbstractJsonConverter<Epic> {
    public EpicJsonConverter(DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter);
    }

    public EpicJsonConverter() {
    }

    @Override
    public JsonObject toJson(Epic epic) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", epic.getName());
        jsonObject.addProperty("description", epic.getDescription());
        jsonObject.addProperty("id", epic.getId());
        jsonObject.addProperty("taskStatus", epic.getTaskStatus().name());
        jsonObject.addProperty("taskType", epic.getTaskType().name());

        if (epic.getStartTime() != null) {
            jsonObject.addProperty("startTime", epic.getStartTime().format(dateTimeFormatter));
        }
        jsonObject.addProperty("durationOfMinutes", epic.getDuration().toMinutes());

        List<Subtask> subtasks = epic.getSubtasks();

        if (!subtasks.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            SubtaskJsonConverter subtaskJsonConverter = new SubtaskJsonConverter();

            for (Subtask subtask : subtasks) {
                jsonArray.add(subtaskJsonConverter.toJson(subtask));
            }
            jsonObject.add("subtasks", jsonArray);
        }
        return jsonObject;
    }

    @Override
    public Epic fromJson(JsonObject jsonObject) {
        return new Epic(
                jsonObject.get("name").getAsString(),
                jsonObject.get("description").getAsString()
        );
    }
}
