package component;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.object.Entity;
import framework.rigidbodies.Edge;
import framework.rigidbodies.Node2D;
import utilities.geometry.Vector.Vector2f;
import utilities.physics.Collision2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@DoNotDestroyInGUI
public class MeshCollider extends Collider{
    private transient List<Entity> cells;
    private transient List<Node2D> nodes;
    private List<Node2D> collision;

    @Override
    public void awake() {
        cells = getChildren();
        nodes = getComponent(Mesh.class).nodes;
    }

    @Override
    public void earlyUpdate() {
        Collections.shuffle(cells);
        collision = checkCollision(nodes);
        int count = 0;
        while(collision.size() > 0 && count < 3){
            count++;
            collision = checkCollision(collision);
        }
    }

    @Override
    public void lateUpdate() {
        collision = checkCollision(collision);
    }

    public List<Node2D> checkCollision(List<Node2D> nodes) {
        if(nodes.size() < 1) return new ArrayList<>();
        List<Node2D> collidingNodes = new ArrayList<>();
        for(Entity cell: cells){
            Mesh mesh = Objects.requireNonNull(cell.getComponent(Mesh.class));
            for(Node2D node: nodes){
                if(!mesh.contains(node) && mesh.collidesWithNode(node)) {
                    Vector2f closestPoint = new Vector2f();
                    Vector2f secondClosestPoint = new Vector2f();
                    float closestDistance = Float.POSITIVE_INFINITY;
                    float secondClosestDistance = Float.POSITIVE_INFINITY;
                    float thirdClosestDistance = Float.POSITIVE_INFINITY;
                    closestPoint = getClosestPoint(
                            mesh,
                            node,
                            closestPoint,
                            secondClosestPoint,
                            closestDistance,
                            secondClosestDistance,
                            thirdClosestDistance);
                    node.moveTo(closestPoint);
                    collidingNodes.add(node);

                }
            }
        }
        return collidingNodes;
    }

    private Vector2f getClosestPoint(Mesh mesh, Node2D node, Vector2f closestPoint, Vector2f secondClosestPoint, float closestDistance, float secondClosestDistance, float thirdClosestDistance) {
        Vector2f thirdClosestPoint;
        for (Edge e : mesh.edges) {
            Vector2f potentialPoint = setNodePositionToClosestEdge(node, e);
            float thisDistance = Vector2f.dist(node.getPosition(), potentialPoint);
            if ( thisDistance < closestDistance) {
                thirdClosestDistance = secondClosestDistance;
                secondClosestDistance = closestDistance;
                closestDistance = thisDistance;

                thirdClosestPoint = secondClosestPoint;
                secondClosestPoint = closestPoint;
                closestPoint = potentialPoint;

            }else if(thisDistance < secondClosestDistance){
                thirdClosestDistance = secondClosestDistance;
                secondClosestDistance = thisDistance;

                thirdClosestPoint = secondClosestPoint;
                secondClosestPoint = potentialPoint;

            } else if (thisDistance < thirdClosestDistance){
                thirdClosestDistance = thisDistance;
                thirdClosestPoint = potentialPoint;
            }
        }
        return closestPoint;
    }

    private Vector2f setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        return closePoint;
    }
}
