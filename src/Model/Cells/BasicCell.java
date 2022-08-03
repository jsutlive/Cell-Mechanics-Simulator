package Model.Cells;

import Engine.States.State;
import Physics.Rigidbodies.*;

import java.util.ArrayList;
import java.util.List;

public class BasicCell extends Cell{

    public static Cell build(List<Node> nodes, int lateralResolution, int apicalResolution) throws IllegalAccessException, InstantiationException {
        Cell cell = (Cell) State.create(BasicCell.class);
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
        cell.setNodes(nodes);
        cell.setEdges(edges);
        return cell;
    }
}
