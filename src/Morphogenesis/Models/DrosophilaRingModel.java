package Morphogenesis.Models;

import Framework.States.State;
import Renderer.Graphics.Vector.CircleGraphic;
import Morphogenesis.Entities.*;
import Morphogenesis.Components.Meshing.CellMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Forces.GaussianGradient;
import Morphogenesis.Components.Physics.Forces.Gradient;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrosophilaRingModel extends Model {

    int lateralResolution = 4;


    public int numberOfSegmentsInTotalCircle = 80;
    public int numberOfConstrictingSegmentsInCircle = 18;

    public int shorteningCellBegin = 16;
    public int shorteningCellEnd = 28;

    public float outerRadius = 300;
    public float innerRadius = 200;

    public transient RingMesh ringMesh;
    public Gradient apicalGradient;
    public transient List<Edge> basalEdges = new ArrayList<>();
    public transient List<Edge> apicalEdges = new ArrayList<>();
    public final Vector2i boundingBox = new Vector2i(800);


    @Override
    public void awake() throws InstantiationException {
        State.addGraphicToScene(new CircleGraphic(new Vector2i(400), 602, Color.gray));
        apicalGradient = new GaussianGradient(0f, 1.2f);
        apicalGradient.calculate(numberOfConstrictingSegmentsInCircle,
                25.4f, .01f,
                .1f, .1f);
        ringMesh = addComponent(new RingMesh());
        generateOrganism();
        List<Node2D> yolkNodes = new ArrayList<>();
        for(Edge edge: basalEdges){
            yolkNodes.add((Node2D) edge.getNodes()[0]);
        }
        for(Edge edge: apicalEdges){
            ringMesh.outerNodes.add((Node2D) edge.getNodes()[0]);
        }


        Yolk.build(yolkNodes, basalEdges);
        ringMesh.innerNodes.addAll(yolkNodes);

    }

    @Override
    public void start() {
        //addComponent(new CellRingCollider());
    }

    public void generateOrganism() throws InstantiationException {
        generateTissueRing();
        ringMesh.nodes.clear();
        for(Cell cell: ringMesh.cellList)
        {
            for(Node2D node: cell.getComponent(CellMesh.class).nodes){
                if(!node.getPosition().isNull()) {
                    if (!ringMesh.contains(node)) ringMesh.nodes.add(node);
                }
            }
            for(Edge edge: cell.getComponent(CellMesh.class).edges){
                if(edge instanceof ApicalEdge) apicalEdges.add(edge);
                if(edge instanceof BasalEdge) basalEdges.add(edge);
            }

        }
        if(ringMesh.nodes.size() > 400){
            System.out.println(ringMesh.nodes.size());
            throw new InstantiationException("Should be only 400 nodes");
        }
        System.out.println("ALLNODES:" + ringMesh.nodes.size());


    }

    private void generateTissueRing() {
        Vector2f position, unitVector;
        ArrayList<Cell> mirroredCells = new ArrayList<>();

        ArrayList<Node2D> oldNodes = new ArrayList<>();
        ArrayList<Node2D> oldMirroredNodes = new ArrayList<>();

        ArrayList<Node2D> zeroEdgeNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (numberOfSegmentsInTotalCircle/2)+1; i++) {

            ArrayList<Node2D> nodes= new ArrayList<>();
            ArrayList<Node2D> mirroredNodes = new ArrayList<>();
            ArrayList<Node2D> constructionNodes = new ArrayList<>();

            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);

            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, boundingBox.asFloat());
                Node2D currentNode = new Node2D(position);
                Node2D mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();
                if(i == numberOfSegmentsInTotalCircle/2)
                {
                    unitVector = CustomMath.GetUnitVectorOnCircle(0, numberOfSegmentsInTotalCircle);
                    unitVector.y = - unitVector.y;
                    position = CustomMath.TransformToWorldSpace(unitVector,radiusToNode, boundingBox.asFloat());
                    zeroEdgeNodes.add(new Node2D(position));
                }
                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
                ringMesh.nodes.add(currentNode);
                ringMesh.nodes.add(mirroredNode);
            }

            if (i > 0) {
                Cell newCell;
                Cell mirroredCell;
                // Build the first set of cells in the cell ring
                if(i<=numberOfConstrictingSegmentsInCircle/2)
                {
                    // copy list to prevent assignment issues between collections
                    List<Node2D> oldNodesZ = new ArrayList<>(oldNodes);
                    oldNodes.addAll(nodes);
                    newCell = State.create(ApicalConstrictingCell.class, new CellMesh().build(oldNodes));
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
                    mirroredCell = State.create(ApicalConstrictingCell.class, new CellMesh().build(constructionNodes));
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
                            newCell = State.create(ShorteningCell.class, new CellMesh().build(oldNodes));
                            mirroredCell = State.create(ShorteningCell.class, new CellMesh().build(constructionNodes));
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            oldNodes.addAll(zeroEdgeNodes);
                            newCell = State.create(ShorteningCell.class, new CellMesh().build(oldNodes));

                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            mirroredCell = State.create(ShorteningCell.class, new CellMesh().build(constructionNodes));

                        }
                    }
                    else{
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            newCell = State.create(BasicCell.class, new CellMesh().build(oldNodes));
                            mirroredCell = State.create(BasicCell.class, new CellMesh().build(constructionNodes));
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            oldNodes.addAll(zeroEdgeNodes);
                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            newCell = State.create(BasicCell.class, new CellMesh().build(oldNodes));
                            mirroredCell = State.create(BasicCell.class, new CellMesh().build(constructionNodes));

                        }
                    }

                }
                assert newCell != null;
                newCell.setId(i-1);
                assert mirroredCell != null;
                mirroredCell.setId(numberOfSegmentsInTotalCircle-i);
                addCellToList(mirroredCells, mirroredCell, i);
                addCellToList(ringMesh.cellList, newCell, i);

            }
            Collections.reverse(nodes);
            oldMirroredNodes = mirroredNodes;
            oldNodes = nodes;
        }
        Collections.reverse(mirroredCells);
        ringMesh.cellList.addAll(mirroredCells);

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

    @Override
    public void earlyUpdate() {
       checkNodesWithinBoundary(getComponent(RingMesh.class).nodes);
    }

    private void checkNodesWithinBoundary(List<Node2D> allNodes) {
        for(Node2D node: allNodes)
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), outerRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), outerRadius);
            }

        }
    }
}
