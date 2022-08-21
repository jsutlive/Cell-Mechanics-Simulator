package Model;

import Engine.Object.MonoBehavior;
import Model.Cells.*;
import Model.Organisms.*;
import Physics.Forces.*;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.awt.*;
import java.rmi.ConnectIOException;
import java.util.ArrayList;
import java.util.List;

public class Model extends MonoBehavior
{
    PhysicsSystem physicsSystem;
    IOrganism organism = new DrosophilaEmbryo();
    List<Node> allNodes = new ArrayList<>();
    float shellRadius = 302f;
    Cell yolk;
    LJForceType ljType = LJForceType.simple;
    public static Vector2f largestResultantForce = new Vector2f(0);
    public static Gradient apicalGradient;
    List<Node> basalNodes = new ArrayList<>();
    List<Node> apicalNodes = new ArrayList<>();
    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException Cannot instantiate cells in model due to an error
     * @throws IllegalAccessException Problems accessing memory during cell creation
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        apicalGradient = new GaussianGradient(0f, 0.8f);
        organism.generateOrganism();

        setCellColors();
        allNodes = organism.getAllNodes();

        for(Cell cell : organism.getAllCells()){
            cell.overrideElasticConstants();
            for(Edge edge :cell.getEdges()) {
                if(edge instanceof BasalEdge) {
                    basalNodes.add(edge.getNodes()[0]);
                }else if(edge instanceof ApicalEdge){
                    apicalNodes.add(edge.getNodes()[0]);
                }
            }
        }
        yolk = Yolk.build(basalNodes);
    }


    public void printCells(){
        // pick specific cells to print using cell.print() to debug using this function
    }

    @Override
    public void start() {
        for(Cell cell : organism.getAllCells()) cell.start();
    }

    @Override
    public void run(){}

    /**
     * Update all forces, at node level and cellular level.
     */
    @Override
    public void update()
    {
        yolk.update();

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

        checkCollision();
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

    private void checkCollision(){
        for(Cell cell: organism.getAllCells()){
            for(Node node: apicalNodes){
                Vector2f nodePosition = node.getPosition();
                if(cell.nodeIsInside(node) && !cell.Contains(node)){
                    for(Edge e: cell.getEdges()) {
                        if(e.contains(node)){continue;}
                        if(!(e instanceof ApicalEdge)){continue;}
                        Vector2f closePoint = CustomMath.ClosestPointToSegmentFromPoint(node.getPosition(),e.getPositions());
                        float dist = CustomMath.sq(nodePosition.x - closePoint.x) + CustomMath.sq(nodePosition.y - closePoint.y);
                        Vector2f normal = CustomMath.normal(e);
                        Node[] nodes = e.getNodes();
                        nodes[0].AddForceVector(normal);
                        nodes[1].AddForceVector(normal);
                        normal.mul(-2);
                        node.AddForceVector(normal);
                    }
                }
            }
            for(Node node: basalNodes){
                Vector2f nodePosition = node.getPosition();
                if(cell.nodeIsInside(node) && !cell.Contains(node)){
                    for(Edge e: cell.getEdges()) {
                        if(e.contains(node)){continue;}
                        if(!(e instanceof BasalEdge)){continue;}
                        Vector2f closePoint = CustomMath.ClosestPointToSegmentFromPoint(node.getPosition(),e.getPositions());
                        float dist = CustomMath.sq(nodePosition.x - closePoint.x) + CustomMath.sq(nodePosition.y - closePoint.y);
                        Vector2f normal = CustomMath.normal(e);
                        Node[] nodes = e.getNodes();
                        nodes[0].AddForceVector(normal);
                        nodes[1].AddForceVector(normal);
                        normal.mul(-2);
                        node.AddForceVector(normal);
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
    public Model() {
        this.start();
    }

    private void setCellColors() {
        for(Cell cell: organism.getAllCells())
        {

            System.out.println(cell.getId());
            if(cell instanceof ApicalConstrictingCell)
            {
                cell.setColor(Color.MAGENTA);
                for(Edge edge: cell.getEdges()){

                    if(edge instanceof ApicalEdge){
                        edge.setColor(Color.RED);
                    }
                }
            }
            if(cell instanceof ShorteningCell)
            {
                cell.setColor(Color.BLUE);
            }
            if(cell instanceof BasicCell)
            {
                cell.setColor(Color.WHITE);
            }

        }
    }




}
