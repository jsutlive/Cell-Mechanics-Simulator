package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import GUI.Painter;
import Model.Organisms.*;
import Physics.Forces.GaussianGradient;
import Physics.Forces.Gradient;
import Physics.Forces.LinearGradient;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model extends MonoBehavior
{
    PhysicsSystem physicsSystem;
    //IOrganism organism = new SimpleFourCellBox();
    IOrganism organism = new DrosophilaEmbryo();
    float shellRadius = 302f;
    List<Node> yolkNodes = new ArrayList<>();
    Vector2f center = new Vector2f(400);
    Yolk yolk;
    float yolkArea;
    float yolkConstant = .01f;
    public static Gradient apicalGradient;
    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        this.addTag(Tag.MODEL);
        apicalGradient = new GaussianGradient(0f, 0.8f);
        organism.generateOrganism();
        yolk = (Yolk) State.create(Yolk.class);
        for(Cell cell: organism.getAllCells())
        {
            System.out.println(cell.getId());
            if(cell instanceof ApicalConstrictingCell)
            {
                cell.setColor(Color.MAGENTA);
            }
            if(cell instanceof ShorteningCell)
            {
                cell.setColor(Color.BLUE);
            }
            for(Edge edge: cell.getEdges()){
                if(edge instanceof BasalEdge){
                    yolkNodes.add(edge.getNodes()[0]);
                    break;
                }
            }
        }
        yolk.build(organism.getAllCells(), yolkNodes);
    }


    public void printCells(){
        //ApicalConstrictingCell cell0 = (ApicalConstrictingCell) organism.getAllCells().get(0);
        //System.out.println(apicalGradient.getConstants()[cell0.getRingLocation()-1]);
        //cell0.setColor(Color.GREEN);
        //ApicalConstrictingCell cell40 = (ApicalConstrictingCell) organism.getAllCells().get(40);
        //System.out.println(apicalGradient.getConstants()[cell40.getRingLocation()-1]);
        //cell40.setColor(Color.GREEN);


    }

    @Override
    public void start() {
    }

    @Override
    public void run(){}

    /**
     * Update all forces, at node level and cellular level.
     */
    @Override
    public void update()
    {
        for(Node node: organism.getAllNodes()) node.resetResultantForce();
        Edge e;
        float maxRadius = 100f;
        float ljConstant = 3e-5f;
        calculateYolkRestoringForce();
        //yolk.update();
        for(Node node: organism.getAllNodes())
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), shellRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), shellRadius);
            }

        }
        for(Cell cell: organism.getAllCells())
        {
            cell.update();
            //calculateLennardJonesForces(maxRadius, ljConstant, cell);
            //
            //checkCellCellCollision(cell);
        }
        for(Node node: organism.getAllNodes())
        {
            node.Move();
            //checkCellCellCollision(cell);
        }
    }

    private void calculateYolkRestoringForce() {
        float currentYolkArea = Gauss.nShoelace(yolkNodes);
        if( currentYolkArea< yolkArea)
        {
            for(Cell cell: organism.getAllCells())
            {
                for(Edge edge:cell.getEdges())
                {
                    if(edge instanceof BasalEdge)
                    {
                        Vector2f force = CustomMath.normal(edge);
                        force.mul(-yolkConstant);
                        edge.AddForceVector(force);
                    }
                }
            }
        }
        else if(currentYolkArea == yolkArea){
        }
        else
        {
            for(Cell cell: organism.getAllCells())
            {
                for(Edge edge:cell.getEdges())
                {
                    if(edge instanceof BasalEdge)
                    {
                        Vector2f force = CustomMath.normal(edge);
                        force.mul(yolkConstant);
                        edge.AddForceVector(force);
                    }
                }
            }
        }
    }

    private void checkCellCellCollision(Cell cell){
        Vector2f[] bound = Geometry.getMinMaxBoundary(cell);
        float minX = bound[0].x;
        float minY = bound[0].y;
        float maxX = bound[1].x;
        float maxY = bound[1].y;

        for(Node node: organism.getAllNodes()){
            if(!cell.Contains(node))
            {
                Vector2f pos = node.getPosition();

                if (pos.x > minX && pos.y > minY && pos.x < maxX && pos.y < maxY)
                {

                    if(Geometry.polygonContainsPoint(cell, node)){
                        Edge e = Geometry.getClosestEdgeToPoint(cell, node);

                        if(!e.isNull) {
                            Vector2f adjustedPosition = Geometry.getNearestPointOnLine(e, pos);
                            node.MoveTo(adjustedPosition);
                        }
                        System.out.println("COLLISION IDENTIFIED:");
                        node.setColor(Color.red);
                        cell.print();
                        pos.print();
                    }
                }
            }

        }
    }

    private void calculateLennardJonesForces(float maxRadius, float ljConstant, Cell cell) {
        for (Edge edge: cell.getEdges())
        {
            for(Node n: organism.getAllNodes()){
                if(edge.contains(n)) continue;
                float dist = CustomMath.pDistanceSq(n, edge);
                if(Float.isNaN(dist)) continue;
                if(dist < maxRadius){
                   Vector2f pointOnEdge = CustomMath.pointSlope(n, edge);
                   Vector2f forceVector = Vector2f.unit(pointOnEdge, n.getPosition());
                    forceVector.mul(ljConstant);
                    Edge temp;
                    Node t = new Node(pointOnEdge);
                    temp = new BasicEdge(n, t);
                    float forceMagnitude = Math.min(4f, ljConstant * (1f/ (float)Math.pow(temp.getLength(), 3)));
                    forceVector.mul(forceMagnitude);
                   if(!Float.isNaN(forceVector.x) && !Float.isNaN(forceVector.y)) {
                       n.AddForceVector(forceVector);
                   }
                }
            }
            edge.hasActed = false;
        }
    }

    private void addLJForceBasicNodeSystem(float ljConstant, Node node, Node t) {
        Edge e;
        e = new BasicEdge(node, t);
        float forceMagnitude = Math.min(3f, ljConstant * (1f/ e.getLength()));
        Vector2f forceVector = new Vector2f(e.getXUnit(), e.getYUnit());
        forceVector.mul(-forceMagnitude);
        node.AddForceVector(forceVector);
    }

    private void AddSimpleHydrostaticForce(int i, float v, float v2) {
        Vector2f yolkConservationForce = Vector2f.unit(yolkNodes.get(i).getPosition(), center);
        if (i >= 8 || i < 71) {
            yolkConservationForce.mul(v);
        } else {
            yolkConservationForce.mul(v2);
        }
        yolkNodes.get(i).AddForceVector(yolkConservationForce);
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public Model() throws InstantiationException, IllegalAccessException {
        this.start();
    }


}
