package Framework.Data;

import Framework.Data.Json.ComponentSerializer;
import Framework.Data.Json.EntitySerializer;
import Framework.Data.Json.NodeSerializer;
import Framework.Data.Json.VectorDeserializer;
import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Rigidbodies.Node;
import Framework.Timer.Time;
import Morphogenesis.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBuilder {

    private static String basePath = System.getProperty("user.dir") + "//export//";
    public static String fullPathName;
    public static float saveFrequency = Time.asNanoseconds(20);

    public static void setSaveFrequency(float seconds){
        saveFrequency = Time.asNanoseconds(seconds);
    }


    public static void setFullPathName(String endPath){
        fullPathName = basePath + endPath + "//";
        File directory = new File(fullPathName);
        if(directory.exists())return;
        try {
            Files.createDirectory(Paths.get(fullPathName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static void saveAbridged(List<Entity> entities, String filePath) throws IOException {
        File file = new File(fullPathName + filePath + ".csv");
        file.createNewFile();
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = new String[5];
            header[0] = "Name";
            header[1] = "Area";
            header[2] = "Perimeter";
            header[3] = "Centroid X";
            header[4] = "Centroid Y";
            writer.writeNext(header);
            for (Entity e : entities) {
                if (e.getComponent(Mesh.class) != null) {
                    Mesh mesh = e.getComponent(Mesh.class);
                    String[] data = new String[5];
                    data[0] = e.name;
                    data[1] = String.valueOf(mesh.getArea());
                    data[2] = String.valueOf(mesh.getPerimeter());
                    data[3] = String.valueOf(mesh.calculateCentroid().x);
                    data[4] = String.valueOf(mesh.calculateCentroid().y);
                    writer.writeNext(data);
                }
            }
            writer.close();
        }catch (IOException e){
            throw new IOException("FAILED TO MAKE FILE");
        }
    }
}
