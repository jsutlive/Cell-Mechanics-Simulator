package Model;

import Physics.Rigidbodies.*;

public class ApicalConstrictingCell extends Cell
{
    public ApicalConstrictingCell()
    {
        internalConstant = .1f;
        elasticConstant = .1f;
    }

    /**
     * update physics on Apical Constricting Cells
     * overrides the update method as described in Cells
     */
    @Override
    public void update() {
        for(Edge edge: edges)
        {
            if(edge instanceof LateralEdge) {
                edge.constrict(.35f, elasticRatio);
            }
            else if (edge instanceof BasalEdge)
            {
                edge.constrict(.70f, elasticRatio);
            }
            else if(edge instanceof ApicalEdge)
            {
                edge.constrict(elasticConstant, elasticRatio);
                //if(getRingLocation()%2 == 0)
                edge.constrict(constant, ratio);
                //edge.constrict(constant * (1 - getRingLocation()/40), ratio);
            }
        }
        for(Edge edge: internalEdges) edge.constrict(internalConstant, elasticRatio);

    }
}
