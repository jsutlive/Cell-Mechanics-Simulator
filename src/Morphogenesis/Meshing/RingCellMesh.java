package Morphogenesis.Meshing;

import Framework.Data.Json.Exclusion.LogData;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Morphogenesis.Render.DoNotEditInGUI;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;

import java.util.ArrayList;

/**
 * RingCellMesh retains the mesh information about a given cell, in particular its nodes, edges, and boundaries.
 * Can be used to determine collision boundaries, etc.
 */
@LogData
@DoNotDestroyInGUI
public class RingCellMesh extends Mesh implements IBoxMesh{

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

    public RingCellMesh build(ArrayList<Node2D> builderNodes){
        nodes = builderNodes;
        for(int i = 1; i < builderNodes.size(); i++){
            edges.add(new Edge(builderNodes.get(i-1), builderNodes.get(i)));
        }
        edges.add(new Edge(builderNodes.get(builderNodes.size()-1), builderNodes.get(0)));

        return this;
    }

    @Override
    public int getLengthResolution() {
        return lateralResolution;
    }

    @Override
    public int getWidthResolution() {
        return apicalResolution;
    }

    /**
     * Gets the length of each cell by calculating the length of it's lateral edges
     */
    public float getLength(){
        float firstEdgeLength = 0;
        float secondEdgeLength = 0;
        for(int i = 0; i<lateralResolution; i++){
            firstEdgeLength += edges.get(i).getLength();
            secondEdgeLength += edges.get(i+lateralResolution+1).getLength();
        }
        return (firstEdgeLength+secondEdgeLength)/2;
    }

    public ArrayList<Node2D> getNodes(){
        return nodes;
    }
}
