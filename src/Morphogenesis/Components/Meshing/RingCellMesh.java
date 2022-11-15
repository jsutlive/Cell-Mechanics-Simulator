package Morphogenesis.Components.Meshing;

import Framework.Data.Json.Exclusion.LogData;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Components.Render.DoNotEditInGUI;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;

import java.util.List;

/**
 * RingCellMesh retains the mesh information about a given cell, in particular its nodes, edges, and boundaries.
 * Can be used to determine collision boundaries, etc.
 */
@LogData
@DoNotDestroyInGUI
public class RingCellMesh extends Mesh{

    @DoNotEditInGUI
    public int lateralResolution = 4;
    @DoNotEditInGUI
    public int apicalResolution = 1;
    @DoNotEditInGUI
    public int ringLocation;

    @Override
    public void start() {
        calculateArea();
    }

    @Override
    public void earlyUpdate() {
        for(Node2D n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        for (Node2D n : nodes) {
            n.move();
        }
        calculateArea();
    }

    public RingCellMesh build(List<Node2D> builderNodes){
        return build(builderNodes, apicalResolution, lateralResolution);
    }

    public RingCellMesh build(List<Node2D> builderNodes, int xRes, int yRes){
        // Start from top left, move along til end of lateral resolution
        nodes = builderNodes;
        int nodeCount = 0;
        while (nodeCount < yRes){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < yRes + xRes){
            nodeCount++;
            Edge e = new ApicalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while(nodeCount < (2*yRes) + xRes){
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
                edges.add(e);
            }
        }
        return this;
    }
}
