package Morphogenesis.Components.Meshing;

import Framework.States.State;
import Morphogenesis.Entities.ApicalConstrictingCell;
import Morphogenesis.Entities.Cell;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RingMesh extends Mesh{

    int lateralResolution = 4;


    int numberOfSegmentsInTotalCircle = 80;
    int numberOfConstrictingSegmentsInCircle = 18;

    int shorteningCellBegin = 16;
    int shorteningCellEnd = 28;

    float outerRadius = 300;
    float innerRadius = 200;

    public List<Node2D> outerNodes = new ArrayList<>();
    public List<Node2D> innerNodes = new ArrayList<>();
    public List<Cell> cellList = new ArrayList<>();
    private transient List<Edge> basalEdges = new ArrayList<>();
    private transient List<Edge> apicalEdges = new ArrayList<>();

    @Override
    protected void calculateArea(){
        area = Gauss.nShoelace(outerNodes) - Gauss.nShoelace(innerNodes);
    }

    //@Builder
    public void generateRingMesh() throws InstantiationException {
        generateRingStructure();
        nodes.clear();
        for(Cell cell: cellList)
        {
            for(Node2D node: cell.getComponent(CellMesh.class).nodes){
                if(!node.getPosition().isNull()) {
                    if (!this.contains(node)) this.nodes.add(node);
                }
            }
            for(Edge edge: cell.getComponent(CellMesh.class).edges){
                if(edge instanceof ApicalEdge) apicalEdges.add(edge);
                if(edge instanceof BasalEdge) basalEdges.add(edge);
            }
        }
        if(nodes.size() > 400){
            System.out.println(nodes.size());
            throw new InstantiationException("Should be only 400 nodes");
        }
        System.out.println("ALLNODES:" + nodes.size());


    }

    private void generateRingStructure(){
        Vector2f position, unitVector;
        ArrayList<Cell> mirroredCells = new ArrayList<>();

        ArrayList<Node2D> oldNodes = new ArrayList<>();
        ArrayList<Node2D> oldMirroredNodes = new ArrayList<>();

        ArrayList<Node2D> zeroEdgeNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (numberOfSegmentsInTotalCircle/2)+1; i++) {

            ArrayList<Node2D> nodes = new ArrayList<>();
            ArrayList<Node2D> mirroredNodes = new ArrayList<>();

            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);

            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, new Vector2i(800).asFloat());
                Node2D currentNode = new Node2D(position);
                Node2D mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();
                if (i == numberOfSegmentsInTotalCircle / 2) {
                    unitVector = CustomMath.GetUnitVectorOnCircle(0, numberOfSegmentsInTotalCircle);
                    unitVector.y = -unitVector.y;
                    position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, new Vector2i(800).asFloat());
                    zeroEdgeNodes.add(new Node2D(position));
                }
                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
                nodes.add(currentNode);
                nodes.add(mirroredNode);
            }
            if (i > 0) {
                Cell newCell;
                Cell mirroredCell;
                // Build the first set of cells in the cell ring
                if (i <= numberOfConstrictingSegmentsInCircle / 2) {
                    // copy list to prevent assignment issues between collections
                    List<Node2D> oldNodesZ = new ArrayList<>(oldNodes);
                    oldNodes.addAll(nodes);
                    newCell = State.create(ApicalConstrictingCell.class, new CellMesh().build(oldNodes));
                    Collections.reverse(mirroredNodes);
                    ArrayList<Node2D> constructionNodes = new ArrayList<>(mirroredNodes);
                    if (i == 1) {
                        Collections.reverse(oldNodesZ);
                        constructionNodes.addAll(oldNodesZ);
                    } else {
                        constructionNodes.addAll(oldMirroredNodes);
                    }
                    mirroredCell = State.create(ApicalConstrictingCell.class, new CellMesh().build(constructionNodes));
                    Collections.reverse(mirroredNodes);
                }
            }
        }
    }



    public void addCellToList(List<Cell> cellList, Cell cell, int ringLocation) {
        if(cell !=null) {
            cell.setRingLocation(ringLocation);
            cellList.add(cell);
        }else
        {
            throw new NullPointerException("New cell object not instantiated successfully");
        }
        if(cell.getComponent(CellMesh.class).nodes.size() == 0){
            throw new IllegalStateException("Nodes list not found at ring location " + ringLocation);
        }
    }

    private float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        return outerRadius +  radiusStep * j;
    }
}
