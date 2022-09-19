package Model;


import Data.LogData;
import Data.LogDataExclusionStrategy;
import Data.LogOnce;
import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ElasticForce;
import Model.Components.Render.CellRenderer;
import Physics.Rigidbodies.Node;
import java.util.ArrayList;
import java.util.List;
@LogOnce
public class CellTestModel extends Model{

    int apicalResolution = 2;
    int lateralResolution = 3;
    transient Cell cell;

    @Override
    public void awake() {
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
        //LogDataExclusionStrategy log = new LogDataExclusionStrategy();
        //System.out.println(log.shouldSkipClass(CellMesh.class));
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public CellTestModel() throws InstantiationException, IllegalAccessException {
    }
}
