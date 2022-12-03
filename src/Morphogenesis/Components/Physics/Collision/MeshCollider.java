package Morphogenesis.Components.Physics.Collision;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.HexMesh;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Physics.Collision2D;

import java.util.Collections;
import java.util.List;

@DoNotDestroyInGUI
public class MeshCollider extends Collider{
    transient List<Entity> cells;
    transient List<Node2D> nodes;

    @Override
    public void awake() {
        cells = getComponent(HexMesh.class).cellList;
        nodes = getComponent(Mesh.class).nodes;
    }

    @Override
    public void lateUpdate() {
        Collections.shuffle(cells);
        checkCollision();
    }

    private void checkCollision() {
        for(Entity cell: cells){
            Mesh mesh = cell.getComponent(Mesh.class);
            if (mesh.isStatic) continue;
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
        if(closePoint.isNull() || (closePoint.x == 0 && closePoint.y == 0)) return;
        if(node.getPosition().distanceTo(closePoint) > 5f) return;
        node.moveTo(closePoint);
    }
}
