package Model.Components.Meshing;

import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.List;

public class CircleMesh extends Mesh{

    public CircleMesh build(List<Node> nodes, List<Edge> edges){
        this.nodes = nodes;
        this.edges = edges;
        return this;
    }
}
