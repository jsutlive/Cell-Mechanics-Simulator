package Utilities.Geometry;

import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

public class Boundary {
    public static boolean ContainsNode(Node2D n, Vector2f center, float radius) {
        return ContainsPosition(n.getPosition(), center, radius);
    }

    public static boolean ContainsPosition(Vector2f v, Vector2f center, float radius){
        return (CustomMath.sq(v.x - center.x)+ CustomMath.sq(v.y - center.y) )
                <= CustomMath.sq(radius);
    }

    public static void clampNodeToBoundary(Node2D n, Vector2f center, float radius)
    {
        Vector2f pos = getClampedPosition(n.getPosition(), center, radius);
        n.moveTo(pos);
    }

    public static Vector2f getClampedPosition(Vector2f v, Vector2f center, float radius)
    {
        float dist = Vector2f.dist(center, v);
        float xUnit = (v.x - center.x)/dist;
        float yUnit = (v.y - center.y)/dist;
        Vector2f pos = new Vector2f(xUnit, yUnit);
        pos.mul(radius);
        return pos;
    }
}
