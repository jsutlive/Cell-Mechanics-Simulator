package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Model.Organisms.*;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.List;

public class Model extends MonoBehavior
{
    //TODO: Add yolk conservation
    //TODO: Add osmosis force
    //TODO: Fix LJ-type forces
    PhysicsSystem physicsSystem;
    //IOrganism organism = new SimpleFourCellBox();
    IOrganism organism = new DrosophilaEmbryo();
    float shellRadius = 302f;
    List<Node> yolkNodes = new ArrayList<>();
    Vector2f center = new Vector2f(400);
    float yolkArea;

    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        organism.generateOrganism();
        for(Cell cell: organism.getAllCells())
        {
            for(Edge edge: cell.getEdges()){
                if(edge instanceof BasalEdge){
                    yolkNodes.add(edge.getNodes()[0]);
                    break;
                }
            }
        }
        System.out.println(yolkNodes.size() + "YOLKNODES");
    }


    @Override
    public void start() {
    }

    /**
     * Update all forces, at node level and cellular level.
     */
    @Override
    public void update()
    {
        Edge e;
        float maxRadius = 35f;
        float ljConstant = 5.6f;
        int yolkSmaller = 0; // false
        float currentYolkArea = Gauss.nShoelace(yolkNodes);
        if( currentYolkArea< yolkArea)
        {
            for (Node n: yolkNodes)
            {
                Vector2f yolkConservationForce = Vector2f.unit(n.getPosition(), center);
                yolkConservationForce.mul(.25f);
                n.AddForceVector(yolkConservationForce);
            }
        }
        else if(currentYolkArea == yolkArea){
        }
        else
        {
            for (Node n: yolkNodes)
            {
                Vector2f yolkConservationForce = Vector2f.unit(n.getPosition(), center);
                yolkConservationForce.mul(-.25f);
                n.AddForceVector(yolkConservationForce);
            }
        }
        for(Node node: organism.getAllNodes())
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), shellRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), shellRadius);
                //System.out.println(node.getPosition().x + "," + node.getPosition().y);
            }

            for(Node t: organism.getAllNodes())
            {

                if(node!=t && Boundary.ContainsNode(t, node.getPosition(), maxRadius))
                {
                    e = new BasicEdge(node, t);
                    float forceMagnitude = Math.min(3f, ljConstant * (1f/ e.getLength()));
                    Vector2f forceVector = new Vector2f(e.getXUnit(), e.getYUnit());
                    forceVector.mul(-forceMagnitude);
                    node.AddForceVector(forceVector);
                }
            }
        }
        for(Cell cell: organism.getAllCells())
        {
            for (Edge edge: cell.getEdges())
            {
                edge.hasActed = false;
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
}
