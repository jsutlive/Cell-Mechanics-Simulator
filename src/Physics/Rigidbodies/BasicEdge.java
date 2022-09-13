package Physics.Rigidbodies;

import Engine.Simulation;

public class BasicEdge extends Edge{
    public BasicEdge(Node a, Node b)
    {
        elasticConstant = .25f;
        MakeNewEdge(a, b);
        isNull = false;
    }
    public BasicEdge(){}

    @Override
    public Edge clone(){
        return new BasicEdge(this.getNodes()[0].clone(), this.getNodes()[1].clone() );
    }
}
