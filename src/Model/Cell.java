package Model;

import GUI.IColor;
import GUI.IRender;
import GUI.Painter;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cell implements IRender, IColor {

    private Color color;
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public List<Edge> getEdges(){
        return edges;
    }

    public List<Node> getNodes(){
        return nodes;
    }

    @Override
    public void render()
    {
        Painter.drawCell(this);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        // set color for all nodes in the cell
        for(Node node : nodes )
        {
            if(node instanceof IColor)
            {
                ((IColor) node).setColor(color);
            }
        }
        // set color for all edges in the cell
        for(Edge edge: edges)
        {
            if(edge instanceof  IColor)
            {
                ((IColor) edge).setColor(color);
            }
        }
    }
}
