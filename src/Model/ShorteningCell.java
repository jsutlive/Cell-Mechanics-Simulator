package Model;

import Physics.Forces.Force;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.LateralEdge;

public class ShorteningCell extends Cell{
    float lateralShorteningRatio = .9f;
    float lateralShorteningConstant = .05f;
    public ShorteningCell()
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
            else if(edge instanceof LateralEdge)
            {
                Force.elastic(edge, elasticConstant);
                Force.constrict(edge, lateralShorteningConstant, lateralShorteningRatio);
            }
        }
        for(Edge edge: internalEdges) Force.elastic(edge, internalConstant);
        Force.restore(this, osmosisConstant) ;
    }


}
