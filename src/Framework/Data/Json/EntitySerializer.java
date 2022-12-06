package Framework.Data.Json;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.Tag;
import com.google.gson.*;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<Entity>, JsonDeserializer<Entity> {

    @Override
    public Entity deserialize(JsonElement json, Type typOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Tag tag = context.deserialize(jsonObject.get("tag"), Tag.class);
        int uniqueID = context.deserialize(jsonObject.get("uniqueID"), int.class);

        Entity entity = new Entity(name, uniqueID, tag);
        for(JsonElement e: components){
            Component c = context.deserialize(e, Component.class);
            System.out.println(c.getClass().getSimpleName());
            entity.addComponent(c);
        }
        return entity;
    }

    @Override
    public JsonElement serialize(Entity entity, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(entity.getClass().getName()));
        result.add("properties", context.serialize(entity, entity.getClass()));
        return result;
    }
}
