package Data;

import Engine.Object.MonoBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class File {
    public static void save(MonoBehavior mono){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(mono);
    }
}
