package tracker.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.tasks.Epic;

import java.io.IOException;

public class EpicToIdAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.value(epic.getId());
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
