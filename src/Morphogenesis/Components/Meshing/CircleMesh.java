package Morphogenesis.Components.Meshing;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;

import java.util.List;

@DoNotDestroyInGUI
public class CircleMesh extends Mesh{

    public CircleMesh build(List<Node2D> circleNodes, List<Edge> circleEdges) {
        nodes = circleNodes;
        edges = circleEdges;
        return this;
    }
}
