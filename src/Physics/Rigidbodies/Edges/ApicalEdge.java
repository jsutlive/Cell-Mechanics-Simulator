package Physics.Rigidbodies.Edges;

import Physics.Rigidbodies.Nodes.Node2D;

/**
 * Apical edges form the outer boundary of the cell that is facing towards the epithelium.
 */
public class ApicalEdge extends Edge{
    public ApicalEdge(Node2D a, Node2D b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .55f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new ApicalEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
