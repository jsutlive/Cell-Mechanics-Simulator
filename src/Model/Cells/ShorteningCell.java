package Model.Cells;

import Data.LogData;
import Engine.States.State;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ElasticForce;
import Model.Components.Physics.LateralShorteningSpringForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;
import Physics.Rigidbodies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShorteningCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new LateralShorteningSpringForce());
        //addComponent(new OsmosisForce());
        getComponent(CellRenderer.class).setColor(Color.BLUE);
    }

    public static Cell build(List<Node> nodes, int lateralResolution, int apicalResolution) throws IllegalAccessException, InstantiationException {
        Cell cell = State.create(ShorteningCell.class);
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
        cell.getComponent(CellMesh.class).nodes = nodes;
        cell.getComponent(CellMesh.class).edges = edges;
        return cell;
    }

}
