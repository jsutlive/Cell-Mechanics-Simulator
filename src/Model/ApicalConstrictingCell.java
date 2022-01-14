package Model;

import Physics.Forces.Force;
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
                Force.elastic(edge, .35f);
            }
            else if (edge instanceof BasalEdge)
            {
                Force.elastic(edge, .70f);
            }
            else if(edge instanceof ApicalEdge)
            {
                Force.elastic(edge, elasticConstant);
                Force.constrict(edge, constant, ratio);
            }
        }
        for(Edge edge: internalEdges) Force.elastic(edge, internalConstant);

    }

    public void constrictApicalEdge()
    {
        for(Edge edge:edges){
            if(edge instanceof ApicalEdge) Force.constrict(edge, constant, ratio);
        }
    }
}
