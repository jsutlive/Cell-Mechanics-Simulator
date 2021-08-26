package GUI;

import Engine.Renderer;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellNode;
import Physics.Bodies.Edge;
import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;

import java.awt.*;

public class Painter {
    public static Color DEFAULT_COLOR = Color.white;

    public static void drawCell(Cell cell)
    {
        for(Vertex node: cell.getNodes())
        {
            drawPoint(node.getPosition().asInt(), node.getColor());
        }
        for(Edge edge: cell.getAllEdges())
        {
            Vector2f[] positions = edge.getPositions();
            drawLine(positions[0].asInt(), positions[1].asInt(), cell.getColor());
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
        Renderer.g.drawOval(point.x, point.y, 2, 2);
    }

    public static void setColor(Color color)
    {
        if(Renderer.g.getColor()== color) return;
        Renderer.g.setColor(color);
    }

}
