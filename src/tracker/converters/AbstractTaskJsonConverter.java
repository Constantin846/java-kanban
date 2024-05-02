package tracker.converters;

import com.google.gson.JsonObject;
import tracker.tasks.AbstractTask;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import tracker.tasks.Task;
import java.time.format.DateTimeFormatter;

public class AbstractTaskJsonConverter extends AbstractJsonConverter<AbstractTask> {
    private final TaskJsonConverter taskJsonConverter;
    private final EpicJsonConverter epicJsonConverter;
    private final SubtaskJsonConverter subtaskJsonConverter;

    public AbstractTaskJsonConverter(DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter);
        taskJsonConverter = new TaskJsonConverter();
        epicJsonConverter = new EpicJsonConverter();
        subtaskJsonConverter = new SubtaskJsonConverter();
    }

    public AbstractTaskJsonConverter() {
        taskJsonConverter = new TaskJsonConverter();
        epicJsonConverter = new EpicJsonConverter();
        subtaskJsonConverter = new SubtaskJsonConverter();
    }

    @Override
    protected JsonObject toJson(AbstractTask currentTask) {
        JsonObject jsonObject = null;

        if (currentTask instanceof Task) {
            Task task = (Task) currentTask;
            jsonObject = taskJsonConverter.toJson(task);
        } else if (currentTask instanceof Epic) {
            Epic epic = (Epic) currentTask;
            jsonObject = epicJsonConverter.toJson(epic);
        } else if (currentTask instanceof Subtask) {
            Subtask subtask = (Subtask) currentTask;
            jsonObject = subtaskJsonConverter.toJson(subtask);
        }
        return jsonObject;
    }

    @Override
    protected AbstractTask fromJson(JsonObject jsonObject) {
        return null;
    }
}
