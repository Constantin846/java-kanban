package tracker.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tracker.tasks.AbstractTask;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

abstract class AbstractJsonConverter<T extends AbstractTask> {
    protected final ZoneId zoneId = ZoneId.of("UTC+4");
    protected final DateTimeFormatter defaultDateTimeFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm");
    protected final DateTimeFormatter dateTimeFormatter;

    protected AbstractJsonConverter(DateTimeFormatter dateTimeFormatter)  {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    protected AbstractJsonConverter() {
        this.dateTimeFormatter = defaultDateTimeFormatter;
    }

    protected abstract JsonObject toJson(T task);

    protected abstract T fromJson(JsonObject jsonObject);

    public JsonArray toJson(List<T> tasks) {
        JsonArray jsonArray = new JsonArray();

        for (T task : tasks) {
            jsonArray.add(toJson(task));
        }
        return jsonArray;
    }

    public JsonObject toJson(Map<Integer, T> tasks){
        JsonObject jsonObject = new JsonObject();

        for (Integer id : tasks.keySet()) {
            jsonObject.add(String.valueOf(id), toJson(tasks.get(id)));
        }
        return jsonObject;
    }
}
