package Utilities.Geometry;

import Morphogenesis.Entities.Cell;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Edges.BasicEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.List;

public class Geometry {

    public static Vector2f APPROX_INF = new Vector2f(1e15f);
    public static float ninetyDegreesAsRadians = (float)Math.PI/2;

    public static boolean polygonContainsPoint(Cell cell, Node2D point){
        int count = 0;

        Vector2f p3 = point.getPosition();
        Vector2f p4 = APPROX_INF;
        Mesh mesh = cell.getComponent(Mesh.class);
        for (Edge edge: mesh.edges) {
            Vector2f p1 = edge.getPositions()[0];
            Vector2f p2 = edge.getPositions()[1];
            if(doEdgesIntersect(p1, p2, p3, p4)){
                count++;
            }
        }
        return count % 2 != 0;


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

    public static Vector2f intersectingPointOfTwoLines(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4)
    {
        // Line AB represented as a1x + b1y = c1
        float a1 = p2.y - p1.y;
        float b1 = p1.x - p2.x;
        float c1 = a1*(p1.x) + b1*(p1.y);

        // Line CD represented as a2x + b2y = c2
        float a2 = p4.y - p3.y;
        float b2 = p3.x - p4.x;
        float c2 = a2*(p3.x)+ b2*(p3.y);

        float determinant = a1*b2 - a2*b1;

        if(determinant > 0){
            float x = (b2*c1 - b1*c2)/determinant;
            float y = (a1*c2 - a2*c1)/determinant;
            return new Vector2f(x, y);
        }
        else return APPROX_INF;
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

    public static Edge getClosestEdgeToPoint(Cell cell, Node2D n)
    {
        float shortestDistance = Float.POSITIVE_INFINITY;
        Edge currentEdge = new BasicEdge();
        Mesh mesh = cell.getComponent(Mesh.class);

        for(Edge edge: mesh.edges){
            Vector2f start = n.getPosition();
            Vector2f end = getNearestPointOnLine(edge, start);
            float currentDistance = Vector2f.dist(start, end);
            if(currentDistance < shortestDistance){
                shortestDistance = currentDistance;
                currentEdge = edge;
            }
        }
        return currentEdge;
    }

    public static float calculateAngleBetweenEdges(Edge a, Edge b){
        Node[] edgeANodes = a.getNodes();
        Node[] edgeBNodes = b.getNodes();
        List<Node> cornerNodes = Node.getAllUnique(edgeANodes, edgeBNodes);
        if(cornerNodes.size()!=3){
            throw new IllegalArgumentException("Corners should only consist of three nodes");
        }

        Vector p1 = cornerNodes.get(0).getPosition();
        Vector p2 = cornerNodes.get(1).getPosition();
        Vector p3 = cornerNodes.get(2).getPosition();
        return  calculateAngleBetweenPoints(p1, p2, p3);
    }

    public static float calculateAngleBetweenPoints(Corner corner){
        return calculateAngleBetweenPoints(corner._a.getPosition(), corner._b.getPosition(), corner._c.getPosition());
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

    public static Vector2f getHalfAngleForceFromCorner(Corner corner){
        return getHalfAngleForceFromCorner(corner._a.getPosition(), corner._b.getPosition(), corner._c.getPosition());
    }

    public static Vector2f getHalfAngleForceFromCorner(Vector2f a, Vector2f b, Vector2f c){
        float theta;
        Vector2f forceVector;

        float length = Vector2f.dist(a, c);
        float cosTheta = ((a.x-b.x)*(c.x-b.x)+(a.y-b.y)*(c.y-b.y))/Vector2f.dist(b, a)/Vector2f.dist(b, c);
        boolean isClockwise = isClockwise(a,b,c);
        if(isClockwise) {
            theta = (3 * ninetyDegreesAsRadians) - (float) Math.acos(cosTheta);
            forceVector = new Vector2f(theta * (a.y-c.y)/length,
                                       theta * (c.x - a.x/length));
            if(cornerForceDirectionIsInverted(a,b,c)) forceVector.mul(-1);
        }
        else {
            theta = (float)Math.acos(cosTheta) - ninetyDegreesAsRadians;
            forceVector = new Vector2f(theta * (a.y-c.y)/length,
                                       theta * (c.x - a.x/length));
            if(!cornerForceDirectionIsInverted(a,b,c))forceVector.mul(-1);
        }
        if(forceVector.isNull()) return Vector2f.zero;
        return forceVector;
    }

    private static boolean cornerForceDirectionIsInverted(Vector2f a, Vector2f b, Vector2f c){
        float value = (a.x-b.x) * (a.y - c.y) + (b.y - a.y) + (c.x - a.x);
        return value < 0;
    }

    public static boolean isClockwise(Corner corner){
        return isClockwise(corner._a.getPosition(), corner._b.getPosition(), corner._c.getPosition());
    }

    public static boolean isClockwise(Vector a, Vector b, Vector c){
        float value = a.get(0) * (b.get(1)-c.get(1)) + a.get(1) * (c.get(0)-b.get(0)) + b.get(0) * c.get(1) - c.get(0) * b.get(1);
        return value > 0;
    }

}
