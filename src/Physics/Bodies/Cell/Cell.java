package Physics.Bodies.Cell;

import Physics.Bodies.Edge;
import Physics.Bodies.Polygon;
import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cell extends Polygon{
    private static int NEXT_AVAILABLE_ID = 0;
    private int uID;
    List<CellNode> nodes;
    List<CellEdge> allEdges;

    public static Cell create(Collection<Vertex> vertices, Collection<Edge> edges)
    {
        Cell cell = new Cell();
        cell.attachNodes(vertices);
        cell.attachEdges(edges);
        cell.uID = NEXT_AVAILABLE_ID;
        NEXT_AVAILABLE_ID ++;
        return cell;
    }

    private Cell(){}

    public int getID(){return this.uID;}

    @Override
    protected void MovePosition(Vector2f deltaPosition)
    {

    }

    public List<CellNode> getNodes()
    {
        return nodes;
    }

    public List<CellEdge> getAllEdges()
    {
        return allEdges;
    }



    public void attachNodes(Collection<Vertex> vertices)
    {
        nodes = new ArrayList<>();
        for(Vertex v: vertices)
        {
            CellNode cn = (CellNode)v;
            nodes.add(cn);
            cn.setCell(this);
        }
    }

    public void attachEdges(Collection<Edge> edges)
    {
        allEdges = new ArrayList<>();
        for(Edge e: edges)
        {
            CellEdge ce = (CellEdge)e;
            allEdges.add(ce);
            ce.setCell(this);
        }
    }

    @Override
    public void setColor(Color color)
    {
        this.color = color;
        for(CellNode node: nodes)
        {
            node.setColor(color);
        }
        for(CellEdge edge: allEdges)
        {
            edge.setColor(color);
        }
    }

    public void update()
    {
        for(CellNode node: nodes)
        {
            node.update();
        }
        for(CellEdge edge: allEdges)
        {
            edge.update();
        }
    }

}
