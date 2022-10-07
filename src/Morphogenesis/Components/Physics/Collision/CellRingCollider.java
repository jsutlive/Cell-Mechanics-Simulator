package Morphogenesis.Components.Physics.Collision;

import Framework.Data.LogOnce;
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
    public void earlyUpdate() {
        checkCollision();
    }
    private void checkCollision() {
        for(Cell cell: cells){
            RingCellMesh mesh = cell.getComponent(RingCellMesh.class);
            for(Node2D node: innerNodes){
                Vector2f nodePosition = node.getPosition();
                if(mesh.collidesWithNode(node) && !mesh.contains(node)) {
                    for (Edge e : mesh.edges) {
                        if (!(e instanceof BasalEdge)) {
                            continue;
                        }
                        setNodePositionToClosestEdge(node, nodePosition, e);
                    }
                }
            }
            for(Node2D node: outerNodes){
                Vector2f nodePosition = node.getPosition();
                if(mesh.collidesWithNode(node) && !mesh.contains(node)) {
                    for (Edge e : mesh.edges) {
                        if (!(e instanceof ApicalEdge)) {
                            continue;
                        }
                        setNodePositionToClosestEdge(node, nodePosition, e);
                    }
                }
            }
        }


    }

    private void setNodePositionToClosestEdge(Node2D node, Vector2f nodePosition, Edge e) {
        Vector2f closePoint = Collision2D.closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
        float dist = CustomMath.sq(nodePosition.x - closePoint.x) + CustomMath.sq(nodePosition.y - closePoint.y);
        Vector v = CustomMath.normal(e).mul(dist * 5f);
        if(!v.isNull()) {
            Node[] nodes = e.getNodes();
            nodes[0].moveTo(nodes[0].getPosition().add(v));
            nodes[1].moveTo(nodes[1].getPosition().add(v));
            node.moveTo(node.getPosition().add(v.mul(-1)));
        }
    }



}
