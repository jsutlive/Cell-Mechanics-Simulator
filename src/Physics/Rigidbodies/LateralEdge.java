package Physics.Rigidbodies;

/**
 * Lateral edges are the boundaries between cells on their lateral sides and in a tissue ring monolayer
 * are the only boundary layers
 */
public class LateralEdge extends Edge{
    /**
     * Make new lateral edge, an edge that establishes the boundary on the lateral membrane of a cell.
     * @param a first node in "nodes" array
     * @param b second node in "nodes" array
     */
    public LateralEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .35f;
        isNull = false;

    }

    @Override
    public Edge clone(){
        return new LateralEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
