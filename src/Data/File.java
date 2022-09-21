package Data;

import Engine.Object.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class File {
    public static void save(Entity mono){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(mono);
    }
}
