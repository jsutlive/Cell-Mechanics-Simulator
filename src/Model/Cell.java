package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.CellRenderer;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Math.Gauss;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cell extends MonoBehavior {

    protected List<Node> nodes = new ArrayList<>();
    protected List<Edge> edges = new ArrayList<>();
    protected List<Edge> internalEdges = new ArrayList<>();
    private int ringLocation;
    private float restingArea;

    float constant = .25f;
    float ratio = 0.00000001f;

    float elasticConstant = .12f;
    float elasticRatio = 1f;

    float internalConstant = .5f;

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
     * Add components here that are needed for all cells. This includes more generic forces
     * and the rendering mechanism.
     * In each unique cell type's awake method, configure type-specific physics and characteristics.
     */
    @Override
    public void awake(){
        addRenderer(new CellRenderer());
    }

    @Override
    public void start() {
        restingArea = getArea();
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
            edge.constrict(edge.getElasticConstant(), elasticRatio);
        }
        for(Edge edge: internalEdges) edge.constrict(internalConstant, elasticRatio);
        for(Node node: nodes)
        {
            node.Move();
        }
    }

    public float getArea()
    {
        return Gauss.nShoelace(nodes);
    }

    public void setColor(Color color){
        CellRenderer rend = (CellRenderer)getComponent(CellRenderer.class);
        rend.setColor(color);
    }

    public void addRenderer(CellRenderer renderer){
        addComponent(renderer);
        State.setFlagToRender(this);
    }

    protected void osmosis(){
        float currentArea = getArea();
    }
}
