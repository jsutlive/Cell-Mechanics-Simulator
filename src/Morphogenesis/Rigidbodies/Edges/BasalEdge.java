package Morphogenesis.Rigidbodies.Edges;

import Morphogenesis.Rigidbodies.Nodes.Node;

public class BasalEdge extends Edge{
    public BasalEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = 5.55f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new BasalEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
