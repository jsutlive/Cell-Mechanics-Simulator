package Model;

import Data.LogDataExclusionStrategy;
import Data.LogDataOnceExclusionStrategy;
import Data.LogOnce;
import Engine.Object.MonoBehavior;
import Engine.Simulation;
import Engine.States.State;
import GUI.Vector.CircleGraphic;
import Model.Cells.*;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.Collider;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@LogOnce
public class DrosophilaRingModel extends MonoBehavior {

    int lateralResolution = 4;


    int numberOfSegmentsInTotalCircle = 80;
    int numberOfConstrictingSegmentsInCircle = 18;

    int shorteningCellBegin = 16;
    int shorteningCellEnd = 28;

    float outerRadius = 300;
    float innerRadius = 200;

    public transient List<Cell> allCells = new ArrayList<>();
    public transient List<Node> allNodes = new ArrayList<>();
    public transient List<Edge> basalEdges = new ArrayList<>();
    public transient List<Edge> apicalEdges = new ArrayList<>();
    final Vector2i boundingBox = new Vector2i(800);


    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        State.addGraphicToScene(new CircleGraphic(new Vector2i(400), 602, Color.gray));
        generateOrganism();
        //addComponent(new Collider());
        List<Node> yolkNodes = new ArrayList<>();
        for(Edge edge: basalEdges){
            yolkNodes.add(edge.getNodes()[0]);
        }
        Yolk.build(yolkNodes, basalEdges);
        LogDataOnceExclusionStrategy log = new LogDataOnceExclusionStrategy();
        System.out.println(log.shouldSkipClass(DrosophilaRingModel.class));
    }

    public void generateOrganism() throws InstantiationException, IllegalAccessException {
        generateTissueRing();
        if(Model.apicalGradient!=null) {
            Model.apicalGradient.calculate(numberOfConstrictingSegmentsInCircle,
                    100.5f, 0.001f,
                    40.1f, .3f);
        }
        allNodes.clear();
        for(Cell cell: allCells)
        {
            for(Node node: cell.getComponent(CellMesh.class).nodes){
                if(!node.getPosition().isNull()) {
                    if (!allNodes.contains(node)) allNodes.add(node);
                }
            }
            for(Edge edge: cell.getComponent(CellMesh.class).edges){
                if(edge instanceof ApicalEdge) apicalEdges.add(edge);
                if(edge instanceof BasalEdge) basalEdges.add(edge);
            }
        }
        if(allNodes.size() > 400){
            System.out.println(allNodes.size());
            throw new InstantiationException("Should be only 400 nodes");
        }
        System.out.println("ALLNODES:" + allNodes.size());


    }

    private void generateTissueRing() throws InstantiationException, IllegalAccessException {
        Vector2f position, unitVector;
        ArrayList<Cell> mirroredCells = new ArrayList<>();

        ArrayList<Node> oldNodes = new ArrayList<>();
        ArrayList<Node> oldMirroredNodes = new ArrayList<>();

        ArrayList<Node> zeroEdgeNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (numberOfSegmentsInTotalCircle/2)+1; i++) {

            ArrayList<Node> nodes= new ArrayList<>();
            ArrayList<Node> mirroredNodes = new ArrayList<>();
            ArrayList<Node> constructionNodes = new ArrayList<>();

            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);

            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, boundingBox.asFloat());
                Node currentNode = new Node(position);
                Node mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();
                if(i == numberOfSegmentsInTotalCircle/2)
                {
                    unitVector = CustomMath.GetUnitVectorOnCircle(0, numberOfSegmentsInTotalCircle);
                    unitVector.y = - unitVector.y;
                    position = CustomMath.TransformToWorldSpace(unitVector,radiusToNode, boundingBox.asFloat());
                    zeroEdgeNodes.add(new Node(position));
                }
                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
                allNodes.add(currentNode);
                allNodes.add(mirroredNode);
            }

            if (i > 0) {
                Cell newCell;
                Cell mirroredCell;
                // Build the first set of cells in the cell ring
                if(i<=numberOfConstrictingSegmentsInCircle/2)
                {
                    // copy list to prevent assignment issues between collections
                    List<Node> oldNodesZ = new ArrayList<>(oldNodes);
                    oldNodes.addAll(nodes);
                    newCell = ApicalConstrictingCell.build(oldNodes,lateralResolution, 1);
                    Collections.reverse(mirroredNodes);
                    constructionNodes.addAll(mirroredNodes);
                    if(i == 1)
                    {
                        Collections.reverse(oldNodesZ);
                        constructionNodes.addAll(oldNodesZ);
                    }
                    else
                    {
                        constructionNodes.addAll(oldMirroredNodes);
                    }
                    mirroredCell = ApicalConstrictingCell.build(constructionNodes,lateralResolution, 1);
                    Collections.reverse(mirroredNodes);
                }
                else
                {
                    if(i>= shorteningCellBegin && i <= shorteningCellEnd)
                    {
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            newCell = ShorteningCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            oldNodes.addAll(zeroEdgeNodes);
                            newCell = ShorteningCell.build(oldNodes, lateralResolution, 1);

                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);

                        }
                    }
                    else{
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            oldNodes.addAll(zeroEdgeNodes);
                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);

                        }
                    }

                }
                newCell.setId(i-1);
                mirroredCell.setId(numberOfSegmentsInTotalCircle-i);
                addCellToList(mirroredCells, mirroredCell, i);
                addCellToList(allCells, newCell, i);

            }
            Collections.reverse(nodes);
            oldMirroredNodes = mirroredNodes;
            oldNodes = nodes;
        }
        allCells.addAll(mirroredCells);

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
