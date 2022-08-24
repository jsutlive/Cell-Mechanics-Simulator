package Model;


import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Model.Components.Physics.ElasticForce;
import Physics.Rigidbodies.Node;
import java.util.ArrayList;
import java.util.List;

public class CellTestModel extends Model{

    int apicalResolution = 2;
    int lateralResolution = 3;
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
        cell.getComponent(ElasticForce.class).update();

    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public CellTestModel() throws InstantiationException, IllegalAccessException {
    }
}
