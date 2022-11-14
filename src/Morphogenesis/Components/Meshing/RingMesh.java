package Morphogenesis.Components.Meshing;

import Framework.Object.Entity;
import Framework.States.State;
import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Components.Physics.Collision.EdgeStiffness2D;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Components.Render.MeshRenderer;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Utilities.Math.CustomMath.TransformToWorldSpace;
import static Utilities.Math.CustomMath.GetUnitVectorOnCircle;

@ReloadComponentOnChange
public class RingMesh extends Mesh {

    public int lateralResolution = 4;
    public int segments = 80;
    public float outerRadius = 300;
    public float innerRadius = 200;

    public transient List<Node2D> outerNodes = new ArrayList<>();
    public transient List<Node2D> innerNodes = new ArrayList<>();
    public transient List<Entity> cellList = new ArrayList<>();
    public transient List<Edge> basalEdges = new ArrayList<>();
    public transient List<Edge> apicalEdges = new ArrayList<>();
    public final Vector2i boundingBox = new Vector2i(800);

    @Override
    public void awake() {
        resetCells();
        nodes.clear();
        generateTissueRing();
        setApicalAndBasalEdges();
        buildYolk();
    }

    private void resetCells() {
        for(Entity cell: cellList){
            cell.destroy();
        }
        cellList.clear();
        outerNodes.clear();
        innerNodes.clear();
        basalEdges.clear();
        apicalEdges.clear();
    }

    private void setApicalAndBasalEdges() {
        for(Entity cell: cellList)
        {
            for(Node2D node: cell.getComponent(RingCellMesh.class).nodes){
                if(!node.getPosition().isNull()) {
                    if (!contains(node)) nodes.add(node);
                }
            }
            for(Edge edge: cell.getComponent(RingCellMesh.class).edges){
                if(edge instanceof ApicalEdge) apicalEdges.add(edge);
                if(edge instanceof BasalEdge) basalEdges.add(edge);
            }

        }
    }

    private void buildYolk() {
        List<Node2D> yolkNodes = new ArrayList<>();
        for(Edge edge: basalEdges){
            yolkNodes.add((Node2D) edge.getNodes()[0]);
        }
        for(Edge edge: apicalEdges){
            outerNodes.add((Node2D) edge.getNodes()[0]);
        }

        Entity yolk = State.create(new Entity("Yolk").
                with(new CircleMesh().build(yolkNodes, basalEdges)).
                with(new OsmosisForce()));
        yolk.getComponent(MeshRenderer.class).enabled = false;
        yolk.getComponent(OsmosisForce.class).osmosisConstant = -0.0005f;
        innerNodes.addAll(yolkNodes);
    }

    @Override
    protected void calculateArea() {
        area = Gauss.nShoelace(outerNodes) - Gauss.nShoelace(innerNodes);
    }

    @Override
    public Entity returnCellContainingPoint(Vector2f vector2f) {
        for (Entity cell : cellList) {
            if (cell.getComponent(RingCellMesh.class).collidesWithPoint(vector2f)) {
                return cell;
            }
        }
        return parent;
    }

    public void addCellToList(List<Entity> cellList, Entity cell, int ringLocation) {
        if (cell.getComponent(RingCellMesh.class) != null) {
            cell.getComponent(RingCellMesh.class).ringLocation = ringLocation;
            cellList.add(cell);
        } else {
            throw new NullPointerException("New cell object not instantiated successfully");
        }
        if (cell.getComponent(RingCellMesh.class).nodes.size() == 0) {
            throw new IllegalStateException("Nodes list not found at ring location " + ringLocation);
        }
    }

    public float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        return outerRadius + radiusStep * j;
    }

    public void generateTissueRing() {
        Vector2f position, unitVector;
        ArrayList<Entity> mirroredCells = new ArrayList<>();
        ArrayList<Node2D> oldNodes = new ArrayList<>();
        ArrayList<Node2D> oldMirroredNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (segments / 2) + 1; i++) {

            ArrayList<Node2D> nodes = new ArrayList<>();
            ArrayList<Node2D> mirroredNodes = new ArrayList<>();

            unitVector = GetUnitVectorOnCircle(i, segments);

            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = TransformToWorldSpace(unitVector, radiusToNode, boundingBox.asFloat());
                Node2D currentNode = new Node2D(position);
                Node2D mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();

                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
            }
            Collections.reverse(mirroredNodes);
            Collections.reverse(oldNodes);

            // Build the first set of cells in the cell ring
            Entity newCell;
            if (i >= 1) {

                List<Node2D> cellNodes = new ArrayList<>(mirroredNodes);
                cellNodes.addAll(oldMirroredNodes);
                newCell = getNewCell(cellNodes);
                addCellToList(mirroredCells, newCell, i);
                if(i != 1) {
                    cellNodes = new ArrayList<>(oldNodes);
                }else {
                    Collections.reverse(oldMirroredNodes);
                    cellNodes = new ArrayList<>(oldMirroredNodes);
                }
                if(i == segments/2){
                    Collections.reverse(mirroredNodes);
                    cellNodes.addAll(mirroredNodes);
                }else{
                    cellNodes.addAll(nodes);
                }
                newCell = getNewCell(cellNodes);
                addCellToList(cellList, newCell, i);
            }
            Collections.reverse(mirroredNodes);
            Collections.reverse(oldNodes);
            oldMirroredNodes = new ArrayList<>(mirroredNodes);
            oldNodes = new ArrayList<>(nodes);
            nodes.clear();
            mirroredNodes.clear();
        }
        Collections.reverse(mirroredCells);
        cellList.addAll(mirroredCells);
    }

    private Entity getNewCell(List<Node2D> cellNodes) {
        return State.create(new Entity("Cell " + cellList.size()).
                with(new RingCellMesh().
                        build(cellNodes)).
                with(new EdgeStiffness2D()).
                with(new ElasticForce()).
                with(new CornerStiffness2D()).
                with(new OsmosisForce()));
    }
}
