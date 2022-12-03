package Morphogenesis.Components.Physics.Collision;

import Framework.Data.Json.Exclusion.LogOnce;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.Force;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Physics.Collision2D;

import java.util.ArrayList;
import java.util.List;

@LogOnce
@DoNotExposeInGUI
public class CellRingCollider extends Force {
    transient List<Entity> cells;
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
        for(Entity cell: cells){
            Mesh mesh = cell.getComponent(Mesh.class);
            for(Node2D node: nodes){
                if(!mesh.contains(node) && mesh.collidesWithNode(node)) {
                    for (Edge e : mesh.edges) {
                        setNodePositionToClosestEdge(node, e);
                    }
                }
            }
        }
    }

    private void setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        //if(closePoint.isNull() || closePoint.x <= 0) return;
        if(node.getPosition().distanceTo(closePoint) > 5f) return;
        node.moveTo(closePoint);
    }
}