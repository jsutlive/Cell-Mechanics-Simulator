package Model.Components.Physics.Collision;

import Data.LogOnce;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Meshing.RingMesh;
import Model.Components.Physics.Force;
import Utilities.Physics.ForceType;
import Physics.Rigidbodies.Edges.ApicalEdge;
import Physics.Rigidbodies.Edges.BasalEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

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
        forceVector.setType(ForceType.collision);
        forceVector.reset();
        bothRings = new ArrayList<>(getComponent(RingMesh.class).innerNodes);
        bothRings.addAll(getComponent(RingMesh.class).outerNodes);
        innerNodes = getComponent(RingMesh.class).innerNodes;
        outerNodes = getComponent(RingMesh.class).outerNodes;
        cells = getComponent(RingMesh.class).cellList;
        nodes = getComponent(RingMesh.class).nodes;
    }

    @Override
    public void lateUpdate() {
        checkCollision();
        forceVector.reset();
    }
    private void checkCollision() {
        for(Cell cell: cells){
            CellMesh mesh = cell.getComponent(CellMesh.class);
            for(Node2D node: innerNodes){
                Vector2f nodePosition = node.getPosition();
                if(mesh.collidesWithNode(node) && !mesh.contains(node)) {
                    for (Edge e : mesh.edges) {
                        if (!(e instanceof BasalEdge)) {
                            continue;
                        }
                        Vector2f closePoint = closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
                        float dist = CustomMath.sq(nodePosition.x - closePoint.x) + CustomMath.sq(nodePosition.y - closePoint.y);
                        forceVector.set(CustomMath.normal(e));
                        Node2D[] nodes = e.getNodes();
                        e.addForceVector(forceVector);
                        forceVector.mul(-2);
                        node.addForceVector(forceVector);
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
                        Vector2f closePoint = closestPointToSegmentFromPoint(node.getPosition(), e.getPositions());
                        float dist = CustomMath.sq(nodePosition.x - closePoint.x) + CustomMath.sq(nodePosition.y - closePoint.y);
                        forceVector.set(CustomMath.normal(e));
                        Node2D[] nodes = e.getNodes();
                        e.addForceVector(forceVector);
                        forceVector.mul(-2);
                        node.addForceVector(forceVector);
                    }
                }
            }
        }


    }

    /**
    * Find unit vector describing the angle of a given point from center given which segment it is and the distance
     * from the center
     * @param segment current segment
     * @param point total segments in the circle
     * @return an x,y vector2 (floating point) describing the unit vector.
    */

    public Vector2f closestPointToSegmentFromPoint(Vector2f point, Vector2f[] segment){
        Vector2f edgeVector = segment[1].copy().sub(segment[0]);
        Vector2f pointToEdgeP0 = point.copy().sub(segment[0]);

        float p0Dot = pointToEdgeP0.dot(edgeVector);
        float magnitudeSquared = edgeVector.dot(edgeVector);
        if (p0Dot <= 0){return segment[0].copy();}
        if (p0Dot > magnitudeSquared){return segment[1].copy();}

        Vector2f closestPoint = segment[0].copy();
        closestPoint.add(edgeVector.mul(p0Dot/magnitudeSquared));

        return closestPoint;
    }

}
