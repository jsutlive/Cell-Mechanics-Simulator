package Framework.Data.Json;

import Utilities.Geometry.Vector.Vector;
import com.google.gson.*;

import java.lang.reflect.Type;

public class VectorDeserializer implements JsonSerializer<Vector>, JsonDeserializer<Vector>{
    @Override
    public Vector deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = "";
        try{
            type = jsonObject.get("type").getAsString();
        }catch(NullPointerException e){
            System.out.println("HI");
        }
        JsonElement element = jsonObject.get("properties");

        try{
            return context.deserialize(element, Class.forName(type));
        }catch(ClassNotFoundException e){
            throw new JsonParseException("Unknown element type " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Vector vector, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(vector.getClass().getName()));
        result.add("properties", context.serialize(vector, vector.getClass()));
        return result;
    }
}
