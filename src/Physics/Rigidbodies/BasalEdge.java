package Physics.Rigidbodies;

public class BasalEdge extends Edge{
    public BasalEdge(Node a, Node b)
    {
        MakeNewEdge(a,b);
        elasticConstant = .08f;
        isNull = false;

    }
}
