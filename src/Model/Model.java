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
    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException Cannot instantiate cells in model due to an error
     * @throws IllegalAccessException Problems accessing memory during cell creation
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        apicalGradient = new GaussianGradient(0f, 0.35f);
        organism.generateOrganism();

        setCellColors();
        allNodes = organism.getAllNodes();

        List<Node> yolkNodes = new ArrayList<>();
        for(Cell cell : organism.getAllCells()){
            cell.overrideElasticConstants();
            for(Edge edge :cell.getEdges()) {
                if(edge instanceof BasalEdge) {
                    yolkNodes.add(edge.getNodes()[0]);
                    break;
                }
            }
        }
        yolk = Yolk.build(yolkNodes);
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

            }
            if(cell instanceof ShorteningCell)
            {
                cell.setColor(Color.BLUE);
            }
            if(cell instanceof BasicCell)
            {
                cell.setColor(Color.WHITE);
            }
            for(Edge edge: cell.getEdges()){
                if(edge instanceof BasalEdge){
                    if(edge instanceof ApicalEdge){
                        edge.setColor(Color.RED);
                    }
                    edge.setColor(Color.GREEN);
                }
            }
        }
    }




}
