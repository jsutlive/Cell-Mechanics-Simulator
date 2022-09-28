package Model.Cells;

import Engine.States.State;
import Model.Components.Meshing.Mesh;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;

import java.awt.*;
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