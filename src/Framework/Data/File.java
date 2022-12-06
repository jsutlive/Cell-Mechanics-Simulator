package Framework.Data;

import Framework.Data.Json.ComponentSerializer;
import Framework.Data.Json.EntitySerializer;
import Framework.Data.Json.NodeSerializer;
import Framework.Data.Json.VectorDeserializer;
import Framework.Object.Component;
import Framework.Object.Entity;
import Morphogenesis.Rigidbodies.Node;
import Utilities.Geometry.Vector.Vector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class File {
    public static void save(List<Entity> entities){
        Gson gson = new GsonBuilder().setPrettyPrinting().
                registerTypeAdapter(Component.class, new ComponentSerializer()).
                registerTypeAdapter(Node.class, new NodeSerializer()).
                registerTypeAdapter(Vector.class, new VectorDeserializer()).
                create();
        try {
            FileWriter filewriter = new FileWriter("scene.json");
            filewriter.write(gson.toJson(entities));
            filewriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void save(Entity entity){
        List<Entity> entities = new ArrayList<>();
        entities.add(entity);
        save(entities);
    }

    public static Entity[] load(){
        Gson gson = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(Component.class, new ComponentSerializer()).
                registerTypeAdapter(Entity.class, new EntitySerializer()).
                registerTypeAdapter(Node.class, new NodeSerializer()).
                registerTypeAdapter(Vector.class, new VectorDeserializer()).
                create();
        String inFile = "";
        try{
            inFile = new String(Files.readAllBytes(Paths.get("scene.json")));
        }catch(IOException e){
            e.printStackTrace();
        }

        if(!inFile.equals("")){
            return gson.fromJson(inFile, Entity[].class);
        }
        return null;
    }

}
