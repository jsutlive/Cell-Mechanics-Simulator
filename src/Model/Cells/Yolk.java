package Model.Cells;

import Data.LogData;
import Engine.States.State;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.OsmosisForce;
import Physics.Forces.Force;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes, List<Edge> yolkEdges) throws IllegalAccessException, InstantiationException {
        Cell yolk = State.create(Yolk.class);
        yolk.getComponent(CellMesh.class).nodes = yolkNodes;
        yolk.getComponent(CellMesh.class).edges = yolkEdges;
        return yolk;
    }


    @Override
    public void awake() {
        addComponent(new CellMesh());
    }

    @Override
    public void start() {
        addComponent(new OsmosisForce());
        getComponent(OsmosisForce.class).osmosisConstant = .01f;
    }
}