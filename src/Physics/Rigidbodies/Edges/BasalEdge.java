package Physics.Rigidbodies.Edges;

import Physics.Rigidbodies.Nodes.Node2D;

public class BasalEdge extends Edge{
    public BasalEdge(Node2D a, Node2D b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .55f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new BasalEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
