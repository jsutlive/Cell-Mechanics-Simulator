package Physics.Rigidbodies;

/**
 * Apical edges form the outer boundary of the cell that is facing towards the epithelium.
 */
public class ApicalEdge extends Edge{
    public ApicalEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .15f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new ApicalEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
