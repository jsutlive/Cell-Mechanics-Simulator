package GUI;

import Engine.Renderer;
import Model.Cell;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;

import java.awt.*;

public class Painter {
    public static Color DEFAULT_COLOR = Color.white;

    public static void drawCell(Cell cell)
    {
        for(Node node: cell.getNodes())
        {
            drawPoint(node.getPosition().asInt(), node.getColor());
        }
        for(Edge edge: cell.getEdges())
        {
            Vector2f[] positions = edge.getPositions();
            drawLine(positions[0].asInt(), positions[1].asInt(), edge.getColor());
        }
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB)
    {
        setColor(DEFAULT_COLOR);
        Renderer.g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB, Color color)
    {
        setColor(color);
        Renderer.g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
    }

    public static void drawPoint(Vector2i point)
    {
        setColor(DEFAULT_COLOR);
        Renderer.g.drawOval(point.x, point.y, 2, 2);
    }

    public static void drawPoint(Vector2i point, Color color)
    {
        setColor(color);
        Renderer.g.drawOval(point.x-1, point.y-1, 2, 2);
    }

    public static void drawCircle(Vector2i center, int radius){
        setColor(DEFAULT_COLOR);
        Renderer.g.drawOval(center.x, center.y, radius, radius);

    }

    public static void drawCircle(Vector2i center, int radius, Color color){
        setColor(color);
        Renderer.g.drawOval(center.x - radius/2, center.y - radius/2, radius, radius);
    }

    public static void setColor(Color color)
    {
        if(Renderer.g.getColor()== color) return;
        Renderer.g.setColor(color);
    }

}
