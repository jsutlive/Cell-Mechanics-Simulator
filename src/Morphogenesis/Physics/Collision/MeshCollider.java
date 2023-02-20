package Morphogenesis.Physics.Collision;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Framework.Rigidbodies.Node;
import Morphogenesis.Meshing.Mesh;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Physics.Collision2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DoNotDestroyInGUI
public class MeshCollider extends Collider{
    transient List<Entity> cells;
    transient List<Node2D> nodes;

    @Override
    public void awake() {
        cells = getChildren();
        nodes = getComponent(Mesh.class).nodes;
    }

    @Override
    public void lateUpdate() {
        Collections.shuffle(cells);
        List<Node2D>  collision = checkCollision(nodes);
        int count = 0;
        while(collision.size() > 0 && count < 3){
            count++;
            collision = checkCollision(collision);
        }
    }

    private List<Node2D> checkCollision(List<Node2D> nodes) {
        if(nodes.size() < 1) return new ArrayList<>();
        List<Node2D> collidingNodes = new ArrayList<>();
        for(Entity cell: cells){
            Mesh mesh = cell.getComponent(Mesh.class);
            for(Node2D node: nodes){
                if(!mesh.contains(node) && mesh.collidesWithNode(node)) {
                    Vector2f closestPoint = new Vector2f();
                    Vector2f secondClosestPoint = new Vector2f();
                    Vector2f thirdClosestPoint = new Vector2f();
                    float closestDistance = Float.POSITIVE_INFINITY;
                    float secondClosestDistance = Float.POSITIVE_INFINITY;
                    float thirdClosestDistance = Float.POSITIVE_INFINITY;
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
                    node.moveTo(closestPoint);
                    collidingNodes.add(node);

                }
            }
        }
        return collidingNodes;
    }

    private Vector2f setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        return closePoint;
    }
}
