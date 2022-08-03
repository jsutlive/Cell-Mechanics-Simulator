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
    public float apicalConstrictingConstant = 0f;
    public float apicalConstrictingRatio = .01f;

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
