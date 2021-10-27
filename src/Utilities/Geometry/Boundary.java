package Utilities.Geometry;

import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Math.CustomMath;

public class Boundary {
    public static boolean ContainsNode(Node n, Vector2f center, float radius) {
        return ContainsPosition(n.getPosition(), center, radius);
    }

    public static boolean ContainsPosition(Vector2f v, Vector2f center, float radius){
        return (CustomMath.sq(v.x - center.x)+ CustomMath.sq(v.y - center.y) )
                <= CustomMath.sq(radius);
    }

    public static void clampNodeToBoundary(Node n, Vector2f center, float radius)
    {
        Vector2f pos = getClampedPosition(n.getPosition(), center, radius);
        //System.out.println("GETCLAMPEDPOS: " + pos.x + "," +pos.y);
        n.MoveTo(pos);
    }

    public static Vector2f getClampedPosition(Vector2f v, Vector2f center, float radius)
    {
        float dist = Vector2f.dist(center, v);
        float xUnit = (v.x - center.x)/dist;
        float yUnit = (v.y - center.y)/dist;
        Vector2f pos = new Vector2f(xUnit, yUnit);
        pos.mul(radius);
        pos.add(new Vector2f(400f));
        return pos;
    }
}
