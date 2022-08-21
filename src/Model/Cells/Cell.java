package Model.Cells;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.CellRenderer;
import Physics.Forces.Force;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Corner;
import Utilities.Geometry.Geometry;
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

    protected float internalConstantOverride;
    protected float elasticConstantOverride;
    private List<Corner> corners = new ArrayList<>();

    float cornerAdjustConst = .07f;
    protected float osmosisConstant = .04f;

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
        State.setFlagToRender(this);
    }

    @Override
    public void start()
    {
        restingArea = getArea();
        generateInternalEdges(nodes);
        setNodePositions();
        //setCorners();
    }
    public void overrideElasticConstants(){
    }

    public void setCorners(){
        corners.add(new Corner(nodes.get(9), nodes.get(0), nodes.get(1)));
        corners.add(new Corner(nodes.get(3), nodes.get(4), nodes.get(5)));
        corners.add(new Corner(nodes.get(4), nodes.get(5), nodes.get(6)));
        corners.add(new Corner(nodes.get(8), nodes.get(9), nodes.get(0)));

        corners.get(0).direction = new Vector2f(1,-1);
        corners.get(1).direction = new Vector2f(1, 1);
        corners.get(2).direction = new Vector2f(-1, 1);
        corners.get(3).direction = new Vector2f(-1, -1);
    }

    protected void setNodePositions()
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
        setNodePositions();
        for(Edge edge: edges) {
           Force.elastic(edge);
        }
        for(Edge edge: internalEdges) {
            Force.elastic(edge);
        }
        Force.restore(this, osmosisConstant);
        //adjustCornersUsingHalfAngles();
        //adjustCorners();
    }

    public void move()
    {
        for(Node node: nodes)
        {
            node.Move();
        }
    }

    public boolean nodeIsInside(Node n){
        //checks whether point is inside polygon by drawing a horizontal ray from the point
        //if the num of intersections is even, then it is outside, else it is inside
        //because if a point crosses the shape a total of a even amount of times, then it must have entered inside then exited again. 
        Vector2f nodePos = n.getPosition();
        int intersections = 0;
        for(Edge edge: edges){
            Vector2f[] positions = edge.getPositions();
            Vector2f p1 = positions[0].copy();
            p1.sub(nodePos);
            Vector2f p2 = positions[1].copy();
            p2.sub(nodePos);

            //if they are both on same side of the y-axis, it doesn't intersect
            if(Math.signum(p1.y) == Math.signum(p2.y)){continue;}
            //intersection point (not the actual point, which would contain division, but changed in a way that it should still perserve sine)
            float intersectPoint = (p1.y * (p1.x - p2.x) - p1.x * (p1.y - p2.y)) * (p1.y - p2.y);

            if(intersectPoint < 0){continue;}

            intersections++;
        }
        return intersections%2 != 0;
    }

    protected void adjustCorners(){
        int sign = -1;
        Vector2f genericForce = new Vector2f(1,1);
        for (Corner corner: corners){
            Node n1 = corner.nodes.get(0);
            Node n2 = corner.nodes.get(1);
            Node n3 = corner.nodes.get(2);
            if(Geometry.calculateAngleBetweenPoints(corner) > (float)Math.PI)
            {
                if(Geometry.calculateAngleBetweenPoints(corner) > Geometry.ninetyDegreesAsRadians){
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), -10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), -10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
                else if(Geometry.calculateAngleBetweenPoints(corner) < Geometry.ninetyDegreesAsRadians){
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), 10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), 10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
            }
            else
            {
                if(Geometry.calculateAngleBetweenPoints(corner) > Geometry.ninetyDegreesAsRadians){
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), 10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), 10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
                else if(Geometry.calculateAngleBetweenPoints(corner) < Geometry.ninetyDegreesAsRadians){
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), -10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), -10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
            }

        }
    }

    protected void adjustCornersUsingHalfAngles(){
        for(Corner corner : corners){
            Vector2f forceVector = Geometry.getHalfAngleForceFromCorner(corner);
            forceVector.mul(cornerAdjustConst);
            corner.nodes.get(1).AddForceVector(forceVector);
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
        String cellClass;
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

    void generateInternalEdges(List<Node> nodes){
        int length = nodes.size();
        int halfLength = length/2;
        for(int i =0; i <halfLength - 1; i++){
            internalEdges.add(new BasicEdge(nodes.get(i), nodes.get(length - i- 2)));
            internalEdges.add(new BasicEdge(nodes.get(i + 1), nodes.get(length - i -1)));
        }
    }
}

