package Model;

import Engine.Simulation;
import Model.Cells.ApicalConstrictingCell;
import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2i;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CellTestModel extends Model{

    int apicalResolution = 2;
    int lateralResolution = 3;
    Vector2i offset = new Vector2i(200);
    int stepSizeApical = 35;
    int stepSizeLateral = 50;
    Cell cell;

    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        List<Node> nodes= new ArrayList<>();
        nodes.add(new Node(200,200));
        nodes.add(new Node(200,250));
        nodes.add(new Node(200,300));
        nodes.add(new Node(200, 350));
        nodes.add(new Node(235, 350));
        nodes.add(new Node(270, 350));
        nodes.add(new Node(270, 300));
        nodes.add(new Node(270, 250));
        nodes.add(new Node(270, 200));
        nodes.add(new Node(235, 200));
        cell = BasicCell.build(nodes, lateralResolution, apicalResolution);
        cell.start();
        CellMesh cellMesh = (CellMesh)cell.getComponent(CellMesh.class);
        System.out.println(Simulation.gson.toJson(cellMesh.nodes));
        try {
            FileWriter filewriter = new FileWriter("embryo.txt");
            filewriter.write(Simulation.gson.toJson(cellMesh.nodes));
            filewriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        cell.update();
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public CellTestModel() throws InstantiationException, IllegalAccessException {
    }
}
