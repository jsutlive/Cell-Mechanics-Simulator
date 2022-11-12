package Utilities.Geometry;

import Morphogenesis.Entities.Cell;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.List;

public class Geometry {

    public static Vector2f APPROX_INF = new Vector2f(1e15f);

    public static float calculateAngleBetweenPoints(Vector2f p1, Vector2f p2, Vector2f p3){
        Vector2f a = new Vector2f(p2.x - p1.x, p2.y - p1.y);
        Vector2f b = new Vector2f(p2.x - p3.x, p2.y - p3.y);

        double angleP1P2 = Math.atan2(a.y, a.x);
        double angleP3P2 = Math.atan2(b.y, b.x);

        return normalizeToInteriorAngle(angleP3P2 - angleP1P2);
    }

    private static float normalizeToInteriorAngle(double angle){
        if(angle <0) angle+= (2*Math.PI);
        //else if (angle > Math.PI) angle = (2 * Math.PI - angle);
        return (float)Math.toDegrees(angle);
    }

    public static boolean lineSegmentContainsPoint(Vector2f point, Vector2f[] linePositions)
    {
        Vector2f lineA = linePositions[0];
        Vector2f lineB = linePositions[1];
        return lineSegmentContainsPoint(point, lineA, lineB);
    }

    public static boolean lineSegmentContainsPoint(Vector2f point, Vector2f lineA, Vector2f lineB) {
        return Vector2f.dist(point, lineA) + Vector2f.dist(point, lineB) == Vector2f.dist(lineA, lineB);
    }

    public static boolean doEdgesIntersect(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4){
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p2.x - p1.x;
        s1_y = p2.y - p1.y;
        s2_x = p4.x - p3.x;
        s2_y = p4.y - p3.y;

        float s = (-s1_y * (p1.x - p3.x) + s1_x * (p1.y - p3.y)) / (-s2_x * s1_y + s1_x * s2_y);
        float t = (s2_x * (p1.y - p3.y) - s2_y * (p1.x - p3.x)) / (-s2_x * s1_y + s1_x * s2_y);

        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }

    public static Vector2f[] getMinMaxBoundary(List<Node2D> nodes)
    {
        float xMin = 0;
        float yMin = 0;
        float xMax = 0;
        float yMax = 0;
        for( Node2D node: nodes){
            Vector2f pos = node.getPosition();
            if(pos.x > xMax) xMax = pos.x;
            if(pos.x < xMin) xMin = pos.x;
            if(pos.y > yMax) yMax = pos.y;
            if(pos.y < yMin) yMin = pos.y;
        }
        Vector2f min = new Vector2f(xMin, yMin);
        Vector2f max = new Vector2f(xMax, yMax);
        return new Vector2f[]{min, max};
    }

    public static Vector2f[] getMinMaxBoundary(Cell cell){
        Mesh mesh = cell.getComponent(Mesh.class);
        return getMinMaxBoundary(mesh.nodes);
    }

    public static Vector2f getNearestPointOnLine(Edge e, Vector2f n){
        Vector2f p1 = e.getPositions()[0].copy();
        Vector2f p2 = e.getPositions()[1].copy();

        Vector2f unit = Vector2f.unit(p1, p2);
        float magnitude = Vector2f.dist(p1,p2);

        n = n.sub(p1);
        float d = n.dot(unit);
        d = CustomMath.clamp(d, 0, magnitude);

        unit.mul(d);
        p1 = p1.add(unit);

        return p1;
    }

    public static float calculateAngleBetweenPoints(Vector p1, Vector p2, Vector p3){
        Vector2f a = new Vector2f(p2.get(0) - p1.get(0), p2.get(1) - p1.get(1));
        Vector2f b = new Vector2f(p3.get(0) - p2.get(0), p3.get(1) - p2.get(1));

        if(isClockwise(p1,p2,p3))
            return  (float)Math.PI - (float) Math.acos(a.dot(b)/ (a.mag() * b.mag()));
        else
            return (float)(2*Math.PI) - (float) Math.acos(a.dot(b)/ (a.mag() * b.mag()));
    }

    public static Vector2f movePointAlongArc(Vector2f origin, Vector2f pointToMove, float angle) {
        float dist = Vector2f.dist(origin, pointToMove);
        Vector2f newPosition = CustomMath.GetUnitVectorOnCircle(angle);
        newPosition.mul(dist);
        return newPosition;
    }

    public static Vector2f getForceToMovePointAlongArc(Vector2f origin, Vector2f pointToMove, float angle){
        if(angle == 0) return Vector2f.zero;
        Vector2f target = movePointAlongArc(origin, pointToMove, angle);
        Vector2f force = Vector2f.unit(pointToMove, target);
        if(force.isNull()) return Vector2f.zero;
        return CustomMath.round(force, 3);
    }

    public static boolean isClockwise(Vector a, Vector b, Vector c){
        float value = a.get(0) * (b.get(1)-c.get(1)) + a.get(1) * (c.get(0)-b.get(0)) + b.get(0) * c.get(1) - c.get(0) * b.get(1);
        return value > 0;
    }

}
