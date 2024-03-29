package component;

import framework.object.annotations.DoNotExposeInGUI;
import framework.object.Entity;
import framework.rigidbodies.Edge;
import framework.rigidbodies.Node2D;
import utilities.geometry.Vector.Vector2f;
import utilities.physics.Collision2D;

import java.util.ArrayList;

@DoNotExposeInGUI
public class CellRingCollider extends Force {
    transient ArrayList<Entity> cells;
    transient ArrayList<Node2D> nodes;

    @Override
    public void awake() {
        cells = getChildren();
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
        if(closePoint.equals(node.getPosition()))return;
        if (closePoint.isNull() || (closePoint.x == 0 && closePoint.y == 0)) return;
        if (node.getPosition().distanceTo(closePoint) > 5f) return;
        node.moveTo(closePoint);
    }
}