package Model;

import Physics.Rigidbodies.*;

public class ApicalConstrictingCell extends Cell
{
    public ApicalConstrictingCell()
    {
        internalConstant = .1f;
        elasticConstant = .1f;
    }
    @Override
    public void update() {
        for(Edge edge: edges)
        {
            if(edge.hasActed)continue;
            if(edge instanceof LateralEdge) {
                edge.constrict(.35f, elasticRatio);
                edge.hasActed = true;
            }
            else if (edge instanceof BasalEdge)
            {
                edge.constrict(.70f, elasticRatio);
                edge.hasActed = true;
            }
            else if(edge instanceof ApicalEdge)
            {
                edge.constrict(elasticConstant, elasticRatio);
                edge.constrict(constant * (1 - getRingLocation()/40), ratio);
                edge.hasActed = true;
            }
        }
        for(Edge edge: internalEdges) edge.constrict(internalConstant, elasticRatio);
        for(Node node: nodes)
        {
            node.Move();
        }
    }
}
