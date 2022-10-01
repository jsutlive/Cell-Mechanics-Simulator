package Morphogenesis.Components.Meshing;

import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;

import java.util.List;

public class CircleMesh extends Mesh{

    public CircleMesh build(List<Node2D> nodes, List<Edge> edges){
        this.nodes = nodes;
        this.edges = edges;
        return this;
    }
}
