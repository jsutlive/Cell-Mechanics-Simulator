package Model;

import Physics.Forces.Force;
import Physics.Rigidbodies.*;

public class ShorteningCell extends Cell{
    float lateralShorteningRatio = .7f;
    float lateralShorteningConstant = .10f;
    public ShorteningCell()
    {
        internalConstant = .03f;
        elasticConstant = .05f;
    }

    /**
     * update physics on Apical Constricting Cells
     * overrides the update method as described in Cells
     */
    @Override
    public void update() {
        setNodePositions();
        for(Edge edge: edges)
        {
            if(edge instanceof ApicalEdge) {
                Force.elastic(edge, elasticConstant);
            }
            else if (edge instanceof BasalEdge)
            {
                Force.elastic(edge, elasticConstant);
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
