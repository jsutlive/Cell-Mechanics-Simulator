package Model.Cells;

import Physics.Forces.Force;
import Physics.Rigidbodies.*;

public class ShorteningCell extends Cell{
    float lateralShorteningRatio = .7f;
    float lateralShorteningConstant = .10f;

    public ShorteningCell()
    {
        internalConstantOverride = .03f;
        elasticConstantOverride = .05f;
    }

    @Override
    public void overrideElasticConstants() {
        super.overrideElasticConstants();
        for(Edge edge: edges){
            edge.setElasticConstant(elasticConstantOverride);
        }
        for (Edge edge: internalEdges){
            edge.setElasticConstant(internalConstantOverride);
        }
    }

    /**
     * update physics on Apical Constricting Cells
     * overrides the update method as described in Cells
     */
    @Override
    public void update() {
        super.update();
        applyLateralShortening();
    }

    private void applyLateralShortening() {
        for(Edge edge: edges)
        {
            if(edge instanceof LateralEdge) {
                Force.constrict(edge, lateralShorteningConstant, lateralShorteningRatio);
            }
        }
    }


}
