package Physics.Bodies.Cell;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import GUI.Painter;
import Physics.Bodies.Edge;
import Physics.Bodies.Polygon;
import Physics.Bodies.Vertex;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cell extends Polygon{
    private static int NEXT_AVAILABLE_ID = 0;
    private int cellID;
    List<CellNode> nodes;
    List<CellEdge> allEdges;
    List<Force> attachedForces;

    @Override
    public void awake() {
        State.setFlagToRender(this);
    }

    @Override
    public void render()
    {
        Painter.drawCell(this);
    }

    public static Cell create(Collection<Vertex> vertices, Collection<Edge> edges)
    {
        Cell cell = new Cell();
        cell.attachNodes(vertices);
        cell.attachEdges(edges);
        cell.cellID = NEXT_AVAILABLE_ID;
        NEXT_AVAILABLE_ID ++;
        cell.awake();
        return cell;
    }

    public Cell(){}

    public int getID(){return this.cellID;}

    public void attachForce(Force force)
    {
        attachedForces.add(force);
    }

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

    @Override
    public void start() {

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

    @Override
    public void destroy() {

    }


}
