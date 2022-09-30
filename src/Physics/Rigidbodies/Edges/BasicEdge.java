package Physics.Rigidbodies.Edges;

import Physics.Rigidbodies.Nodes.Node2D;

public class BasicEdge extends Edge{
    public BasicEdge(Node2D a, Node2D b)
    {
        elasticConstant = 5.55f;
        MakeNewEdge(a, b);
        isNull = false;
    }
    public BasicEdge(){}

    @Override
    public Edge clone(){
        return new BasicEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
