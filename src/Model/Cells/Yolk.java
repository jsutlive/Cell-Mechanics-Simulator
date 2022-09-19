package Model.Cells;

import Engine.States.State;
import Model.Components.Meshing.Mesh;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.OsmosisForce;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes, List<Edge> yolkEdges) {
        Cell yolk = State.create(Yolk.class);
        yolk.getComponent(Mesh.class).nodes = yolkNodes;
        yolk.getComponent(Mesh.class).edges = yolkEdges;
        return yolk;
    }


    @Override
    public void awake() {
        addComponent(new CellMesh());
    }

    @Override
    public void start() {
        addComponent(new OsmosisForce());
        getComponent(OsmosisForce.class).osmosisConstant = 0.05f;
    }
}