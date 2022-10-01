package Morphogenesis.Entities;

import Framework.States.State;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.CellMesh;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;

import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node2D> yolkNodes, List<Edge> yolkEdges) {
        Cell yolk = State.create(Yolk.class);
        yolk.getComponent(Mesh.class).nodes = yolkNodes;
        yolk.getComponent(Mesh.class).edges = yolkEdges;
        return yolk;
    }


    @Override
    public void awake() {
        addComponent(new CellMesh());
        //addComponent(new CellRenderer());
        //getComponent(CellRenderer.class).setColor(Color.GREEN);
    }

    @Override
    public void start() {
        addComponent(new OsmosisForce());
        getComponent(OsmosisForce.class).osmosisConstant = -0.001f;
        System.out.println("hello"+getComponent(Mesh.class).getArea() );
    }
}