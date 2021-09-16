package Model;

import Engine.Object.MonoBehavior;
import Engine.Renderer;
import GUI.IColor;
import GUI.IRender;
import GUI.Painter;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cell extends MonoBehavior implements IRender, IColor {

    private Color color;
    protected List<Node> nodes = new ArrayList<>();
    protected List<Edge> edges = new ArrayList<>();
    protected List<Edge> internalEdges = new ArrayList<>();
    private int ringLocation;
    float constant = .45f;
    float ratio = 0.05f;

    float elasticConstant = .2f;
    float elasticRatio = 1f;

    float internalConstant = .15f;

    public List<Edge> getEdges(){
        return edges;
    }

    public void setEdges(List<Edge> edges){
        this.edges = edges;
    }

    public void setInternalEdges(List<Edge> edges) {this.internalEdges = edges;}

    public void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }

    public void setRingLocation(int i){ringLocation = i;}

    public int getRingLocation(){
        return ringLocation;
    }

    public List<Node> getNodes(){
        return nodes;
    }

    /**
     * In Cell objects, this is where we update the forces acting on the cells.
     */
    @Override
    public void update()
    {
        // Edges have multiple varieties. We can target specific edge types to apply unique forces to them,
        // modeling their role in the cell. For example, apical edges model the apical membrane of the early
        // embryo and how it constricts during ventral furrow formation.
        for(Edge edge: edges) {
            edge.constrict(elasticConstant, elasticRatio);
        }
        for(Edge edge: internalEdges) edge.constrict(internalConstant, elasticRatio);
        for(Node node: nodes)
        {
            node.Move();
        }
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
