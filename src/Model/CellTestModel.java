package Model;

import Model.Cells.ApicalConstrictingCell;
import Model.Cells.Cell;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2i;

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
        cell = ApicalConstrictingCell.build(nodes, lateralResolution, apicalResolution);
        cell.start();
    }

    @Override
    public void update() {
        cell.update();
        for(Node node: cell.getNodes()) node.Move();
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public CellTestModel() throws InstantiationException, IllegalAccessException {
    }
}
