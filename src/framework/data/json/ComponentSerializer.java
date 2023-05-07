package framework.data.json;

import component.Component;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try{
            return context.deserialize(element, Class.forName(type));
        }catch(ClassNotFoundException e){
            throw new JsonParseException("Unknown element type " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
