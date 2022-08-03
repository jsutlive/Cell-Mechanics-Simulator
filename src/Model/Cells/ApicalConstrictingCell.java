package Model.Cells;

import Engine.Timer.Time;
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
    public float apicalConstrictingConstant = 0f;
    public float apicalConstrictingRatio = .01f;

    private float internalConstantOverride = .035f;
    private float elasticConstantOverride = .15f;

    public ApicalConstrictingCell()
    {
    }

    @Override
    public void overrideElasticConstants() {
        super.overrideElasticConstants();
        for(Edge edge: edges){
            edge.setElasticConstant(elasticConstantOverride);
            if(edge instanceof ApicalEdge){
                edge.setElasticConstant(0.05f);
            }
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
        setNodePositions();
        for(Edge edge:  edges)
        {
            if(edge instanceof ApicalEdge)
            {

                //If an apical gradient is defined, we use this for apical constriction, else we use the default constants
                if(Model.apicalGradient != null && Model.apicalGradient.getConstants() != null){
                    Gradient gr = Model.apicalGradient;
                    //    Force.constrict(edge,  gr.getConstants()[getRingLocation() - 1] * Time.elapsedTime,
                    //         gr.getRatios()[gr.getRatios().length - getRingLocation()]);
                       Force.constrict(edge,  gr.getConstants()[getRingLocation() - 1] * Time.elapsedTime / 1000000000 / 1000,
                            gr.getRatios()[gr.getRatios().length - getRingLocation()] / 100);

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
