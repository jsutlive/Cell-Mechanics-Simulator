package component;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.rigidbodies.Edge;
import framework.rigidbodies.Node2D;

import java.util.ArrayList;

@DoNotDestroyInGUI
public class CircleMesh extends Mesh{

    public CircleMesh build(ArrayList<Node2D> circleNodes, ArrayList<Edge> circleEdges) {
        nodes = circleNodes;
        edges = circleEdges;
        return this;
    }
}
