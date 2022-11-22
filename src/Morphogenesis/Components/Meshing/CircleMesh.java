package Morphogenesis.Components.Meshing;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Components.Render.DoNotEditInGUI;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;

import java.util.List;

@DoNotDestroyInGUI
public class CircleMesh extends Mesh{

    public CircleMesh build(List<Node2D> circleNodes, List<Edge> circleEdges) {
        nodes = circleNodes;
        edges = circleEdges;
        return this;
    }
}
