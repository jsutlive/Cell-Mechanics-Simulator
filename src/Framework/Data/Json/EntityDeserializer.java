package Framework.Data.Json;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.Tag;
import com.google.gson.*;

import java.lang.reflect.Type;

public class EntityDeserializer implements JsonDeserializer<Entity> {

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
            entity.addComponent(c);
        }
        return entity;
    }
}
