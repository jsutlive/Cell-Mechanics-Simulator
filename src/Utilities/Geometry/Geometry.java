package Utilities.Geometry;

import Model.Cell;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Math.CustomMath;

import java.util.List;

public class Geometry {

    public static Vector2f APPROX_INF = new Vector2f(1e15f);

    public static boolean polygonContainsPoint(Cell cell, Node point){
        int count = 0;

        Vector2f p3 = point.getPosition();
        Vector2f p4 = APPROX_INF;
        for (Edge edge: cell.getEdges()) {
            Vector2f p1 = edge.getPositions()[0];
            Vector2f p2 = edge.getPositions()[1];
            if(doEdgesIntersect(p1, p2, p3, p4)){
                count++;
            }
        }
        if(count%2==0)return false;
        else return true;


    }

    public static boolean lineSegmentContainsPoint(Vector2f point, Vector2f[] linePositions)
    {
        Vector2f lineA = linePositions[0];
        Vector2f lineB = linePositions[1];
        return lineSegmentContainsPoint(point, lineA, lineB);
    }

    public static boolean lineSegmentContainsPoint(Vector2f point, Vector2f lineA, Vector2f lineB) {
        if(Vector2f.dist(point, lineA) + Vector2f.dist(point, lineB) == Vector2f.dist(lineA, lineB)) return true;
        else return false;
    }

    public static boolean doEdgesIntersect(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4){
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p2.x - p1.x;
        s1_y = p2.y - p1.y;
        s2_x = p4.x - p3.x;
        s2_y = p4.y - p3.y;

        float s = (-s1_y * (p1.x - p3.x) + s1_x * (p1.y - p3.y)) / (-s2_x * s1_y + s1_x * s2_y);
        float t = (s2_x * (p1.y - p3.y) - s2_y * (p1.x - p3.x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) return true;
        else return false;
    }

    public static Vector2f[] getMinMaxBoundary(List<Node> nodes)
    {
        float xMin = 0;
        float yMin = 0;
        float xMax = 0;
        float yMax = 0;
        for( Node node: nodes){
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
        return getMinMaxBoundary(cell.getNodes());
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

        Vector2f v = n.copy();
        v.sub(p1);
        float d = v.dot(unit);
        CustomMath.clamp(d, 0, magnitude);

        unit.mul(d);
        Vector2f res = p1;
        res.add(unit);

        return res;
    }

    public static Edge getClosestEdgeToPoint(Cell cell, Node n)
    {
        float shortestDistance = Float.POSITIVE_INFINITY;
        Edge currentEdge = new BasicEdge();

        for(Edge edge: cell.getEdges()){
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

        Vector2f p1 = cornerNodes.get(0).getPosition();
        Vector2f p2 = cornerNodes.get(1).getPosition();
        Vector2f p3 = cornerNodes.get(2).getPosition();
        return  calculateAngleBetweenPoints(p1, p2, p3);
    }

    public static float calculateAngleBetweenPoints(Vector2f p1, Vector2f p2, Vector2f p3){
        Vector2f a = new Vector2f(p2.x - p1.x, p2.y - p1.y);
        Vector2f b = new Vector2f(p3.x - p2.x, p3.y - p2.y);

        return  (float)Math.acos(a.dot(b)/ (a.mag() * b.mag()));
    }


}
