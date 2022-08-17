package Model.Cells;

import Engine.States.State;
import Physics.Forces.Force;
import Physics.Rigidbodies.*;

import java.util.ArrayList;
import java.util.List;

public class ShorteningCell extends Cell{
    float lateralShorteningRatio = .75f;
    float lateralShorteningConstant = .10f;

    public ShorteningCell()
    {
     //   internalConstantOverride = .03f;
     //   elasticConstantOverride = .05f;
    }

    /*
    @Override
    public void overrideElasticConstants() {
        super.overrideElasticConstants();
        for(Edge edge: edges){
            edge.setElasticConstant(elasticConstantOverride);
        }
        for (Edge edge: internalEdges){
            edge.setElasticConstant(internalConstantOverride);
        }
    }*/

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
        /*for(Edge edge: edges)
        {
            if(edge instanceof LateralEdge) {
                Force.constrict(edge, lateralShorteningConstant, lateralShorteningRatio);
            }
        }*/
    }

    public static Cell build(List<Node> nodes, int lateralResolution, int apicalResolution) throws IllegalAccessException, InstantiationException {
        Cell cell = (Cell) State.create(ShorteningCell.class);
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
        //cell.setNodes(nodes);
        //cell.setEdges(edges);
        return cell;
    }

}
