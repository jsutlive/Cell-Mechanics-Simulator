package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Model.Cells.ApicalConstrictingCell;
import Model.Cells.Cell;
import Model.Cells.ShorteningCell;
import Model.Organisms.*;
import Physics.Forces.*;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.BasalEdge;
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
    List<Node> allNodes = new ArrayList<>();
    float shellRadius = 302f;
    List<Node> yolkNodes = new ArrayList<>();
    Vector2f center = new Vector2f(400);
    Yolk yolk;
    float yolkArea;
    float yolkConstant = .05f;
    LJForceType ljType = LJForceType.simple;
    public static Vector2f largestResultantForce = new Vector2f(0);
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
        apicalGradient = new GaussianGradient(0f, 1f);
        organism.generateOrganism();
        yolk = (Yolk) State.create(Yolk.class);

        setCellColors();
        allNodes = organism.getAllNodes();
        yolk.build(organism.getAllCells(), yolkNodes);
    }


    public void printCells(){
        // pick specific cells to print using cell.print() to debug using this function
    }

    @Override
    public void start() {
        for(Cell cell : organism.getAllCells()){
            cell.overrideElasticConstants();
        }
    }

    @Override
    public void run(){}

    /**
     * Update all forces, at node level and cellular level.
     */
    @Override
    public void update()
    {
        float maxRadius = 70f;
        float ljConstant = .1e5f;
        calculateYolkRestoringForce();

        checkNodesWithinBoundary(allNodes);
        for(Cell cell: organism.getAllCells())
        {
            cell.update();
        }
        //calculateLennardJonesForces(maxRadius, ljConstant);

        for(Node node: allNodes)
        {
            node.Move();
        }
    }

    private void checkNodesWithinBoundary(List<Node> allNodes) {
        for(Node node: allNodes)
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), shellRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), shellRadius);
            }

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
                    Vector2f forceVector = Force.GetLennardJonesLikeForce(ljConstant, edge, n, ljType);
                    if(!Float.isNaN(forceVector.x) && !Float.isNaN(forceVector.y)) {
                       n.AddForceVector(forceVector);
                   }
                }
            }
            edge.hasActed = false;
        }
    }

    private void calculateLennardJonesForces(float maxRadius, float ljConstant){
        List<Node> allNodes = organism.getAllNodes();
        for (Node n: allNodes){
            for(Cell c: organism.getAllCells()) {
                for(Edge e: c.getEdges()) {
                    if (n.getPosition().isNull()) {
                        System.out.println("NULL NODE");
                    }
                    if (!e.contains(n)) {
                        float dist = CustomMath.pDistanceSq(n, e);
                        if (Float.isNaN(dist)) {
                            n.setColor(Color.GREEN);
                            System.out.println("NULL DISTANCE AT CELL " + c.getId());
                        } else {
                            if (dist < maxRadius) {
                                Vector2f forceVector = Force.GetLennardJonesLikeForce(ljConstant, e, n, ljType);
                                n.AddForceVector(forceVector);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public Model() throws InstantiationException, IllegalAccessException {
        this.start();
    }

    private void setCellColors() {
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
    }




}
