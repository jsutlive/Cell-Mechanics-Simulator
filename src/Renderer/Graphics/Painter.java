package Renderer.Graphics;

import Renderer.Renderer;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;

import java.awt.*;

public class Painter {
    public static Color DEFAULT_COLOR = Color.white;

    public static void drawEdgeNormal(Edge edge){
        Vector2f center = edge.getCenter();
        Vector2f normal = (Vector2f)CustomMath.normal(edge);
        normal.mul(7);
        normal = normal.add(center);

        drawLine(center.asInt(), normal.asInt());
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB) {
        Renderer.instance.drawLine(pointA, pointB);
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB, Color color) {
        setColor(color);
        drawLine(pointA, pointB);
    }

    public static void drawCircle(Vector2i center, int diameter){
        Renderer.instance.drawCircle(center, diameter);
    }

    public static void drawCircle(Vector2i center, int diameter, Color color){
        setColor(color);
        drawCircle(center, diameter);
    }

    public static void setColor(Color color) {
        Renderer.instance.setColor(color);
    }


}
