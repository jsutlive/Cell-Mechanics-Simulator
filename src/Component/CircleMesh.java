package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;

import java.util.ArrayList;

@DoNotDestroyInGUI
public class CircleMesh extends Mesh{

    public CircleMesh build(ArrayList<Node2D> circleNodes, ArrayList<Edge> circleEdges) {
        nodes = circleNodes;
        edges = circleEdges;
        return this;
    }
}
