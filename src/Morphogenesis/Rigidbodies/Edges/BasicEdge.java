package Morphogenesis.Rigidbodies.Edges;

import Morphogenesis.Rigidbodies.Nodes.Node;

public class BasicEdge extends Edge{
    public BasicEdge(Node a, Node b)
    {
        MakeNewEdge(a, b);
        isNull = false;
    }
    public BasicEdge(){}

    @Override
    public Edge clone(){
        return new BasicEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
