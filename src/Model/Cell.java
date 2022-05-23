package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.CellRenderer;
import Physics.Forces.Force;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Cell extends MonoBehavior {

    protected List<Node> nodes = new ArrayList<>();
    protected List<Edge> edges = new ArrayList<>();
    protected List<Edge> internalEdges = new ArrayList<>();
    private int ringLocation;
    private int id;
    private float restingArea;
    public float getRestingArea() {return restingArea;}

    float elasticConstant = .15f;

    float internalConstant = .05f;
    float osmosisConstant = .001f;

    public List<Edge> getEdges(){
        return edges;
    }
    public HashSet<Vector2f> nodePositions = new HashSet<>();

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public void setEdges(List<Edge> edges){
        this.edges = edges;
    }

    public boolean Contains(Node n){
        Vector2f nodePos = n.getPosition();
        for(Vector2f v : nodePositions){
            if (Vector2f.dist(nodePos, v) < 0.01f) {
                return true;
            }
        }
        return false;
    }

    public List<Edge> getInternalEdges(){
        return internalEdges;
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
    public void start()
    {
        restingArea = getArea();
        setNodePositions();
        if(id == 0){
            //flipApicalAndBasalEdges();
        }
    }

    private void setNodePositions()
    {
        nodePositions.clear();
        for(Node node: nodes){
            nodePositions.add(node.getPosition());
        }
    }

    @Override
    public void run()
    {
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
            if(edge instanceof BasalEdge){
                Force.elastic(edge, elasticConstant);
            }
            else {
                Force.elastic(edge, edge.getElasticConstant());
            }
        }
        for(Edge edge: internalEdges) Force.elastic(edge, internalConstant);
        Force.restore(this, osmosisConstant);
    }

    public void move()
    {
        for(Node node: nodes)
        {
            node.Move();
        }
    }

    public void flipApicalAndBasalEdges(){
        for(Edge edge: edges){
            if (edge instanceof ApicalEdge || edge instanceof BasalEdge){
                edge.flip();
            }
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
        if(currentArea < restingArea)
        {
            for (Edge edge: edges)
            {
                float forceMagnitude = 0.25f * (currentArea - restingArea);
                Vector2f forceVector = CustomMath.normal(edge);
                forceVector.mul(forceMagnitude);
                edge.AddForceVector(forceVector);
            }
        }
    }

    public Vector2f getCenter(){
        float x = 0;
        float y = 0;
        for (Node node: nodes){
            x += node.getPosition().x;
            y += node.getPosition().y;
        }
        x = x/nodes.size();
        y = y/nodes.size();
        return new Vector2f(x,y);
    }

    public void print()
    {
        String cellClass = "";
        int numberOfApicalEdges = 0;
        int numberOfBasalEdges = 0;
        int numberOfLateralEdges = 0;

        if(this instanceof ApicalConstrictingCell){
            cellClass = "Apical Constricting Cell";
        }else if(this instanceof ShorteningCell){
            cellClass = "Lateral Shortening Cell";
        }else {
            cellClass = "Basic Cell";
        }
        for(Edge edge:edges){
            if(edge instanceof ApicalEdge){
                numberOfApicalEdges++;
            }else if (edge instanceof BasalEdge){
                numberOfBasalEdges++;
            }else if (edge instanceof LateralEdge){
                numberOfLateralEdges++;
            }
        }
        System.out.println("--CELL VARS--");
        System.out.println("CELL ID: " + id);
        System.out.println("CELL TYPE: " + cellClass);
        System.out.println("RING LOCATION: " + getRingLocation());
        System.out.println("APICAL EDGES: " + numberOfApicalEdges);
        System.out.println("BASAL EDGES: " + numberOfBasalEdges);
        System.out.println("LATERAL EDGES: " + numberOfLateralEdges);
    }
}
