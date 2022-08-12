package GUI;

import Engine.Renderer;
import Model.Cells.ApicalConstrictingCell;
import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;

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
            drawEdgeNormal(edge);
        }
        /*for(Edge edge: cell.getInternalEdges())
        {
            Vector2f[] positions = edge.getPositions();
            drawLine(positions[0].asInt(), positions[1].asInt(), edge.getColor());
        }*/
    }

    public static void drawForce(Node node, Vector2f forceVector){
        Vector2f nodePosition = node.getPosition();
        forceVector.mul(50);
        forceVector.add(nodePosition);

        drawLine(nodePosition.asInt(), forceVector.asInt(), Color.GREEN);
    }

    public static void drawEdgeNormal(Edge edge){
        Vector2f center = edge.getCenter();
        Vector2f normal = CustomMath.normal(edge);
        normal.mul(7);
        normal.add(center);

        drawLine(center.asInt(), normal.asInt());
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB)
    {
        Renderer.getInstance().drawLine(pointA, pointB);
    }

    public static void drawLine(Vector2i pointA, Vector2i pointB, Color color)
    {
        setColor(color);
        drawLine(pointA, pointB);

    }

    public static void drawPoint(Vector2i point)
    {
        Renderer.getInstance().drawCircle(point, 2);
    }

    public static void drawPoint(Vector2i point, Color color)
    {
        setColor(color);
        drawPoint(point);
    }

    public static void drawCircle(Vector2i center, int diameter){
        Renderer.getInstance().drawCircle(center, diameter);
    }

    public static void drawCircle(Vector2i center, int diameter, Color color){
        setColor(color);
        drawCircle(center, diameter);
    }

    public static void setColor(Color color)
    {
        Renderer.getInstance().setColor(color);
    }

}
