package Framework.Data;

import Framework.Data.Json.ComponentSerializer;
import Framework.Data.Json.EntitySerializer;
import Framework.Data.Json.NodeSerializer;
import Framework.Data.Json.VectorDeserializer;
import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Rigidbodies.Node;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBuilder {
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
        File file = new File(filePath + ".csv");
        file.createNewFile();
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            List<String> dataFiles = new ArrayList<>();
            String[] header = new String[3];
            header[0] = "Name";
            header[1] = "Area";
            header[2] = "Perimeter";
            writer.writeNext(header);
            for (Entity e : entities) {
                if (e.getComponent(Mesh.class) != null) {
                    Mesh mesh = e.getComponent(Mesh.class);
                    String[] data = new String[3];
                    data[0] = e.name;
                    data[1] = String.valueOf(mesh.getArea());
                    data[2] = String.valueOf(mesh.getPerimeter());
                    writer.writeNext(data);
                }
            }
            writer.close();
        }catch (IOException e){
            throw new IOException("FAILED TO MAKE FILE");
        }
    }

    private static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(FileBuilder::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
