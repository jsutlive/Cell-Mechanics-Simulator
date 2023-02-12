package Morphogenesis.Physics.Collision;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Framework.Rigidbodies.Node;
import Morphogenesis.Meshing.Mesh;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node2D;
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
                    for (Edge e : mesh.edges) {
                        setNodePositionToClosestEdge(node, e);
                        collidingNodes.add(node);
                    }
                }
            }
        }
        return collidingNodes;
    }

    private void setNodePositionToClosestEdge(Node2D node, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        if(closePoint.isNull() || (closePoint.x == 0 && closePoint.y == 0)) return;
        if(node.getPosition().distanceTo(closePoint) > 5f) return;
        node.moveTo(closePoint);
    }
}
