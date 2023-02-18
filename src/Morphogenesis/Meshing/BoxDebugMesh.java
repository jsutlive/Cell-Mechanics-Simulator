package Morphogenesis.Meshing;

import Framework.Data.Json.Exclusion.LogData;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Render.DoNotEditInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;

@LogData
@DoNotDestroyInGUI
public class BoxDebugMesh extends Mesh implements IBoxMesh {
    @DoNotEditInGUI
    public int lateralResolution = 4;
    @DoNotEditInGUI
    public int apicalResolution = 1;

    public ArrayList<Node2D> getNodes(){
        return nodes;
    }

    @Override
    public void earlyUpdate() {
        for(Node2D n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        for (Node2D n : nodes) {
            n.move();
        }
    }
    public Mesh build() {
        //curved cell
        Node2D node1 = new Node2D(new Vector2f(30, 0));
        Node2D node2 = new Node2D(new Vector2f(20, 20));
        Node2D node3 = new Node2D(new Vector2f(10, 40));
        Node2D node4 = new Node2D(new Vector2f(0, 60));
        Node2D node5 = new Node2D(new Vector2f(20, 60));
        Node2D node6 = new Node2D(new Vector2f(30, 40));
        Node2D node7 = new Node2D(new Vector2f(40, 20));
        Node2D node8 = new Node2D(new Vector2f(50, 0));

//        Node2D node1 = new Node2D(new Vector2f(0, 0));
//        Node2D node2 = new Node2D(new Vector2f(0, 20));
//        Node2D node3 = new Node2D(new Vector2f(0, 40));
//        Node2D node4 = new Node2D(new Vector2f(0, 60));
//        Node2D node5 = new Node2D(new Vector2f(20, 60));
//        Node2D node6 = new Node2D(new Vector2f(20, 40));
//        Node2D node7 = new Node2D(new Vector2f(20, 20));
//        Node2D node8 = new Node2D(new Vector2f(20, 0));

        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);
        nodes.add(node7);
        nodes.add(node8);
        edges.add(new Edge(node1,node2));
        edges.add(new Edge(node2,node3));
        edges.add(new Edge(node3,node4));
        edges.add(new Edge(node4,node5));
        edges.add(new Edge(node5,node6));
        edges.add(new Edge(node6,node7));
        edges.add(new Edge(node7,node8));
        edges.add(new Edge(node8,node1));
        return this;
    }


    @Override
    public int getLengthResolution() {
        return lateralResolution;
    }

    @Override
    public int getWidthResolution() {
        return apicalResolution;
    }
}
