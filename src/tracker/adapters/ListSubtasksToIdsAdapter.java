package tracker.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.tasks.AbstractTask;
import tracker.tasks.Epic;
import tracker.tasks.Subtask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListSubtasksToIdsAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject jsonObject = new JsonObject();

        String result = gson.toJson(epic.getId());

        jsonWriter.value(result);

    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
