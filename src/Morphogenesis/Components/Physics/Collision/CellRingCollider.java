package Morphogenesis.Components.Physics.Collision;

import Framework.Data.LogOnce;
import Framework.Object.DoNotExposeInGUI;
import Morphogenesis.Entities.Cell;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Force;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Physics.Collision2D;

import java.util.ArrayList;
import java.util.List;

@LogOnce
@DoNotExposeInGUI
public class CellRingCollider extends Force {
    transient List<Cell> cells;
    transient List<Node2D> nodes;
    transient List<Node2D> bothRings;
    transient List<Node2D> innerNodes;
    transient  List<Node2D> outerNodes;

    @Override
    public void awake() {
        innerNodes = getComponent(RingMesh.class).innerNodes;
        outerNodes = getComponent(RingMesh.class).outerNodes;
        bothRings = new ArrayList<>(innerNodes);
        bothRings.addAll(outerNodes);
        cells = getComponent(RingMesh.class).cellList;
        nodes = getComponent(RingMesh.class).nodes;
    }

    @Override
    public void lateUpdate() {
        checkCollision();
    }

    private void checkCollision() {
        for(Cell cell: cells){
            RingCellMesh mesh = cell.getComponent(RingCellMesh.class);
            for(Node2D node: nodes){
                if(mesh.contains(node)) continue;
                else if(mesh.collidesWithNode(node)) {
                    for (Edge e : mesh.edges) {
                        setNodePositionToClosestEdge(node, e);
                    }
                }
            }
        }
    }

    private void setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        if(node.getPosition().distanceTo(closePoint) > 0.5f){
            return;
        }
        node.moveTo(closePoint);
    }



}
