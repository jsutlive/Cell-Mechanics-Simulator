package Component;

import Framework.Data.Json.Exclusion.LogData;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Annotations.DoNotEditInGUI;
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
        System.out.println(getLength());
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

    @Override
    public float getMaximumDistance() {
        return getLength();
    }

    @Override
    public float getMinimumDistance(){
        //return (edges.get(lateralResolution).getLength() + edges.get(edges.size()-1).getLength())/2;
        return edges.get(lateralResolution).getLength();
    }

    /**
     * Gets the length of each cell by calculating the length of its lateral edges
     */
    public float getLength(){
        Edge[][] edges = getLateralEdges();
        float firstEdgeLength = 0;
        float secondEdgeLength = 0;
        for(int i = 0; i<edges[0].length; i++){
            firstEdgeLength += edges[0][i].getLength();
            secondEdgeLength += edges[1][i].getLength();
        }
        return (firstEdgeLength+secondEdgeLength)/2;
    }

    public Edge[][] getLateralEdges(){
        Edge[] firstSide = new Edge[lateralResolution];
        Edge[] secondSide = new Edge[lateralResolution];
        for(int i = 0; i<lateralResolution; i++){
            firstSide[i] = edges.get(i);
            secondSide[i] = edges.get(i+lateralResolution+apicalResolution);
        }
        return(new Edge[][]{firstSide, secondSide});
    }

    public Edge[][] getApicalEdges(){
        Edge[] firstSide = new Edge[apicalResolution];
        Edge[] secondSide = new Edge[apicalResolution];
        for(int i = 0; i<apicalResolution; i++){
            firstSide[i] = edges.get(lateralResolution+i);
            secondSide[i] = edges.get(i+lateralResolution+apicalResolution);
        }
        return(new Edge[][]{firstSide, secondSide});
    }

    public ArrayList<Node2D> getNodes(){
        return nodes;
    }
}
