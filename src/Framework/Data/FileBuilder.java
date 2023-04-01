package Framework.Data;

import Framework.Data.Json.ComponentSerializer;
import Framework.Data.Json.EntitySerializer;
import Framework.Data.Json.NodeSerializer;
import Framework.Data.Json.VectorDeserializer;
import Component.Component;
import Framework.Utilities.Debug;
import Framework.Object.Entity;
import Framework.Rigidbodies.Node;
import Framework.Utilities.Time;
import Component.Mesh;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FileBuilder {

    private static final String BASEPATH = System.getProperty("user.dir") + "//export//";
    public static String fullPathName;
    public static float saveFrequency = Time.asNanoseconds(20);
    public static List<Entity> saveEntities = new ArrayList<>();
    public static HashMap<Entity, List<String[]>> saveDictionary = new HashMap<>();

    public static void setSaveFrequency(float seconds){
        saveFrequency = Time.asNanoseconds(seconds);
    }


    public static void setFullPathName(String endPath){
        fullPathName = BASEPATH + endPath + "//";
        File directory = new File(fullPathName);
        if(directory.exists())return;
        try {
            Files.createDirectory(Paths.get(fullPathName));
        } catch (IOException e) {
            Debug.LogError("Failed to print directory");
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
            Debug.LogError("Failed to save scene");
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
            Debug.LogError("Failed to load scene");
            e.printStackTrace();
        }

        if(!inFile.equals("")){
            return gson.fromJson(inFile, Entity[].class);
        }
        return null;
    }

    public static void cacheMeshData(String timeStamp) {
        for(Entity entity: saveEntities) {
            Mesh mesh = entity.getComponent(Mesh.class);
            if (!saveDictionary.containsKey(entity)) {
                List<String[]> currentData = new ArrayList<>();
                saveDictionary.put(entity, currentData);
                String[] time = new String[]{"Time", timeStamp};
                assert mesh != null;
                String[] area = new String[]{"Area", String.valueOf(mesh.getArea())};
                String[] perimeter = new String[]{"Perimeter", String.valueOf(mesh.getPerimeter())};
                String[] centroidX = new String[]{"CentroidX", String.valueOf(mesh.calculateCentroid().x)};
                String[] centroidY = new String[]{"CentroidY", String.valueOf(mesh.calculateCentroid().y)};
                String[] min = new String[]{"Minimum Distance", String.valueOf(mesh.getMinimumDistance())};
                String[] max = new String[]{"Maximum Distance", String.valueOf(mesh.getMaximumDistance())};
                String[] bounds = new String[]{"Distance from Bounds", String.valueOf(mesh.getDistanceToBoundary())};
                currentData.clear();
                currentData.add(time);
                currentData.add(area);
                currentData.add(perimeter);
                currentData.add(centroidX);
                currentData.add(centroidY);
                currentData.add(min);
                currentData.add(max);
                currentData.add(bounds);
            }else {
                List<String[]> currentData = saveDictionary.get(entity);
                String[] time = appendStringArray(currentData.get(0), timeStamp);
                assert mesh != null;
                String[] area = appendStringArray(currentData.get(1), String.valueOf(mesh.getArea()));
                String[] perimeter = appendStringArray(currentData.get(2), String.valueOf(mesh.getPerimeter()));
                String[] centroidX = appendStringArray(currentData.get(3), String.valueOf(mesh.calculateCentroid().x));
                String[] centroidY = appendStringArray(currentData.get(4), String.valueOf(mesh.calculateCentroid().y));
                String[] min = appendStringArray(currentData.get(5), String.valueOf(mesh.getMinimumDistance()));
                String[] max = appendStringArray(currentData.get(6), String.valueOf(mesh.getMaximumDistance()));
                String[] bounds = appendStringArray(currentData.get(7), String.valueOf(mesh.getDistanceToBoundary()));
                currentData.clear();
                currentData.add(time);
                currentData.add(area);
                currentData.add(perimeter);
                currentData.add(centroidX);
                currentData.add(centroidY);
                currentData.add(min);
                currentData.add(max);
                currentData.add(bounds);
            }
        }
    }

    public static void saveCSV() throws IOException {
        for(Entity e: saveEntities) {
            File file = new File(fullPathName + e.name + ".csv");
            file.createNewFile();
            try{
                FileWriter outputfile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputfile);
                for(String[] data : saveDictionary.get(e)){
                    writer.writeNext(data);
                }
                writer.close();
            }catch (IOException exception){
                Debug.LogError("Failed to create csv");
                throw new IOException("FAILED TO MAKE FILE");
            }
        }
    }

    public static String[] appendStringArray(String[] array, String s){
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = s;
        return array;
    }
}
