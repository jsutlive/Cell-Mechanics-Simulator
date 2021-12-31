package Utilities.Geometry;

import Model.Cell;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.List;

public class Geometry {

    public static boolean polygonContainsPoint(Cell cell, Node point){
        int count = 0;

        Vector2f p3 = point.getPosition();
        Vector2f p4 = new Vector2f(Float.POSITIVE_INFINITY);
        for (Edge edge: cell.getEdges()) {
            Vector2f p1 = edge.getPositions()[0];
            Vector2f p2 = edge.getPositions()[1];
            float s1_x, s1_y, s2_x, s2_y;
            s1_x = p2.x - p1.x;
            s1_y = p2.y - p1.y;
            s2_x = p4.x - p3.x;
            s2_y = p4.y - p3.y;

            float s = (-s1_y * (p1.x - p3.x) + s1_x * (p1.y - p3.y)) / (-s2_x * s1_y + s1_x * s2_y);
            float t = (s2_x * (p1.y - p3.y) - s2_y * (p1.x - p3.x)) / (-s2_x * s1_y + s1_x * s2_y);

            if (s >= 0 && s <= 1 && t >= 0 && t <= 1) count++;
            System.out.println(count);
        }
            return !(count % 2 == 0);
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

}
