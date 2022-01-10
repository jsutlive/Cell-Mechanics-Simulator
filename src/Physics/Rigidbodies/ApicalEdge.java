package Physics.Rigidbodies;

/**
 * Apical edges form the outer boundary of the cell that is facing towards the epithelium.
 */
public class ApicalEdge extends Edge{
    public ApicalEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .06f;
        isNull = false;

    }
}
