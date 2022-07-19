package Model.Cells;

import Model.Model;
import Physics.Forces.Force;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.*;

/**
 * An Apical Constricting Cell undergoes the following forces:
 *
 * Active:
 * Apical constriction
 *
 * Passive:
 * Elasticity
 * Osmosis
 */
public class ApicalConstrictingCell extends Cell
{
    public float apicalConstrictingConstant = .75f;
    public float apicalConstrictingRatio = .01f;

    private float internalConstantOverride;
    private float elasticConstantOverride;

    public ApicalConstrictingCell()
    {
        internalConstantOverride = .035f;
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
        setNodePositions();
        for(Edge edge: edges)
        {
            if(edge instanceof ApicalEdge)
            {

                //If an apical gradient is defined, we use this for apical constriction, else we use the default constants
                if(Model.apicalGradient != null){
                    Gradient gr = Model.apicalGradient;
                    Force.constrict(edge,  gr.getConstants()[getRingLocation() - 1],
                            gr.getRatios()[gr.getRatios().length - getRingLocation()]);

                }else {
                    Force.constrict(edge, apicalConstrictingConstant, apicalConstrictingRatio);
                }
            }

        }

    }

    public void constrictApicalEdge()
    {
        for(Edge edge:edges){
            if(edge instanceof ApicalEdge) Force.constrict(edge, apicalConstrictingConstant, apicalConstrictingRatio);
        }
    }
}
