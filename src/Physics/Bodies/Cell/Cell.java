package Physics.Bodies.Cell;

import Engine.States.State;
import GUI.Painter;
import Model.DrosophilaEmbryo;
import Model.IOrganism;
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

    public static Cell createCellStructure(Collection<Vertex> vertices, Collection<Edge> edges)
    {
        Cell cell = new Cell();
        cell.attachNodes(vertices);
        cell.attachEdges(edges);
        cell.cellID = NEXT_AVAILABLE_ID;
        NEXT_AVAILABLE_ID ++;
        cell.awake();
        return cell;
    }

    public Cell clone(Cell cell)
    {
        Cell clonedCell = new Cell();
        clonedCell.attachNodes(cell.getNodes());
        clonedCell.attachEdges(cell.getAllEdges());
        clonedCell.cellID = this.cellID;
        cell.awake();
        return cell;
    }



    public int getID(){return this.cellID;}

    public void attachForce(Force force)
    {
        attachedForces.add(force);
    }

    @Override
    protected void MovePosition(Vector2f deltaPosition)
    {

    }

    public Collection<Vertex> getNodes()
    {
        return vertices;
    }

    public Collection<Edge> getAllEdges()
    {
        return edges;
    }

    public void attachNodes(Collection<Vertex> vertices)
    {
        this.vertices = new ArrayList<>();
        for(Vertex v: vertices)
        {
            this.vertices.add(v);
            CellNode cn = (CellNode)v;
            cn.setCell(this);
        }
    }

    public void attachEdges(Collection<Edge> edges)
    {
        this.edges = new ArrayList<>();
        for(Edge e: edges)
        {
            CellEdge ce = (CellEdge)e;
            this.edges.add(e);
            ce.setCell(this);
        }
    }

    @Override
    public void setColor(Color color)
    {
        this.color = color;
        System.out.println("====" + vertices);
        for(Vertex vertex: vertices)
        {
            vertex.setColor(color);
        }
        for(Edge edge: edges)
        {
            edge.setColor(color);
        }
    }

    @Override
    public void start() {

    }

    public void update()
    {
        for(Vertex node: vertices)
        {
            node.update();
        }
        for(Edge edge: edges)
        {
            edge.update();
        }
    }

    @Override
    public void destroy() {

    }


}
