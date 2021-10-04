package Model;

import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

public class ApicalConstrictingCell extends Cell
{
    public ApicalConstrictingCell()
    {
        internalConstant = .1f;
    }
    @Override
    public void update() {
        for(Edge edge: edges)
        {
            edge.constrict(edge.getElasticConstant(), elasticRatio);
            if(edge instanceof ApicalEdge)
            {
                edge.constrict(constant, ratio);
            }
        }
        for(Edge edge: internalEdges) edge.constrict(internalConstant, elasticRatio);
        for(Node node: nodes)
        {
            node.Move();
        }
    }
}
