package Model.Cells;

import Engine.Timer.Time;
import Engine.States.State;

import Model.Model;
import Physics.Forces.Force;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    public float apicalConstrictingConstant = 10f;
    public float apicalConstrictingRatio = .01f;

    private float constrictionOnsetTime = Time.asNanoseconds(1);
    private float constrictionRampedTime = Time.asNanoseconds(15);
    private float totalTimeToConstrict = constrictionRampedTime - constrictionOnsetTime;
                                    
    public ApicalConstrictingCell()
    {
        //internalConstantOverride = .05f;
        //elasticConstantOverride = .35f;
    }

    @Override
    public void overrideElasticConstants() {
        super.overrideElasticConstants();
        for(Edge edge: edges){
            edge.setElasticConstant(elasticConstantOverride);
            if(edge instanceof ApicalEdge){
                edge.setElasticConstant(0.15f);
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
        if(Time.elapsedTime < constrictionOnsetTime) return;
        for(Edge edge:  edges)
        {
            if(edge instanceof ApicalEdge)
            {

                //If an apical gradient is defined, we use this for apical constriction, else we use the default constants
                if(Model.apicalGradient != null && Model.apicalGradient.getConstants() != null){
                    Gradient gr = Model.apicalGradient;
                    float delayedConstant;
                    if(Time.elapsedTime < constrictionRampedTime)
                    {
                        delayedConstant = gr.getConstants()[getRingLocation() - 1] *
                                ((Time.elapsedTime - constrictionOnsetTime)/ totalTimeToConstrict);
                        Force.constrict(edge, delayedConstant, 1 - gr.getRatios()[getRingLocation() - 1] );
                    }
                    else
                    {
                        delayedConstant =  gr.getConstants()[getRingLocation() - 1];
                        Force.constrict(edge, delayedConstant, 1 - gr.getRatios()[getRingLocation() - 1] );
                    }
                    /*float delayedConstant = gr.getConstants()[getRingLocation() - 1] * Math.min(1f * Time.elapsedTime /Time.asNanoseconds(1f)/gr.delayFactor, 1);
                    Force.constantConstrict(
                        edge,
                        delayedConstant,
                        1 - gr.getRatios()[getRingLocation() - 1]  
                    );*/
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

    public static Cell build(List<Node> nodes, int lateralResolution, int apicalResolution) throws IllegalAccessException, InstantiationException {
        Cell cell = (Cell) State.create(ApicalConstrictingCell.class);
        cell.setNodes(nodes);
        List<Edge> edges = new ArrayList<>();

        // Start from top left, move along til end of lateral resolution
        int nodeCount = 0;
        while (nodeCount < lateralResolution){
            nodeCount++;
            edges.add(new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount)));
        }
        while (nodeCount < lateralResolution + apicalResolution){
            nodeCount++;
            edges.add(new ApicalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount)));
        }
        while(nodeCount < (2*lateralResolution) + apicalResolution){
            nodeCount++;
            edges.add(new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount)));
        }
        while (nodeCount < nodes.size()){
            nodeCount++;
            if(nodeCount == nodes.size()){
                edges.add(new BasalEdge(nodes.get(nodeCount-1), nodes.get(0)));
            }
            else{
                edges.add(new BasalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount)));
            }
        }
        cell.setEdges(edges);
        return cell;
    }
}
