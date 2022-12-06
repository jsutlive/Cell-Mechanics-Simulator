package Morphogenesis.Components.Meshing;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;

import java.util.ArrayList;

@DoNotDestroyInGUI
public class CircleMesh extends Mesh{

    public CircleMesh build(ArrayList<Node2D> circleNodes, ArrayList<Edge> circleEdges) {
        nodes = circleNodes;
        edges = circleEdges;
        return this;
    }
}
