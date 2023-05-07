package framework.data.json;

import framework.rigidbodies.Node;
import com.google.gson.*;

import java.lang.reflect.Type;

public class NodeSerializer implements JsonSerializer<Node>, JsonDeserializer<Node>{
    @Override
    public Node deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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
    public JsonElement serialize(Node node, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(node.getClass().getName()));
        result.add("properties", context.serialize(node, node.getClass()));
        return result;
    }
}
