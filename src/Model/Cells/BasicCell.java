package Model.Cells;

import Engine.States.State;
import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ElasticForce;
import Model.Components.Physics.InternalElasticForce;
import Model.Components.Physics.OsmosisForce;
import Physics.Rigidbodies.*;

import java.util.ArrayList;
import java.util.List;


public class BasicCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new OsmosisForce());
        addComponent(new InternalElasticForce());
    }

    public static Cell build(List<Node> nodes, int lateralResolution, int apicalResolution) {
        Cell cell = State.create(BasicCell.class);
        List<Edge> edges = new ArrayList<>();

        // Start from top left, move along til end of lateral resolution
        int nodeCount = 0;
        while (nodeCount < lateralResolution){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < lateralResolution + apicalResolution){
            nodeCount++;
            Edge e = new ApicalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while(nodeCount < (2*lateralResolution) + apicalResolution){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < nodes.size()){
            nodeCount++;
            if(nodeCount == nodes.size()){
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(0));
                e.setNodesReference(nodeCount-1, 0);
                edges.add(e);

            }
            else{
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
                e.setNodesReference(nodeCount-1, nodeCount);
                edges.add(e);            }
        }
        cell.getComponent(CellMesh.class).nodes = nodes;
        cell.getComponent(CellMesh.class).edges = edges;
        cell.getComponent(Lattice.class).buildLattice();
        return cell;
    }
}
