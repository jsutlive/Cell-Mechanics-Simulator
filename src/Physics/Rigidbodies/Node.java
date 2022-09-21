package Physics.Rigidbodies;

import Engine.Simulation;
import Engine.States.State;

import GUI.Vector.LineGraphic;
import Model.Components.Physics.ForceVector.ForceType;
import Model.Components.Physics.ForceVector.ForceVector;
import Model.Model;
import Utilities.Geometry.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Node: A vertex-like object which can implement physics for simulations.
 */
public class Node implements IRigidbody {

    private Vector2f position;
    private transient ForceVector resultantForce = new ForceVector();
    private List<ForceVector> forceVectors = new ArrayList<>();

    private transient LineGraphic debugger;

    public Vector2f getPosition()
    {
        return position;
    }

    private void setPosition(Vector2f pos){
        position = pos;
    }

    public Vector2f getResultantForce()
    {
        return resultantForce;
    }

    public Node()
    {
        resultantForce.setType(ForceType.RESULTANT);
        forceVectors.add(resultantForce);
        position = new Vector2f(0);
        debugger = new LineGraphic(position.asInt(),position.asInt());
        State.addGraphicToScene(debugger);
    }
    public Node(Vector2f pos)
    {
        resultantForce.setType(ForceType.RESULTANT);
        forceVectors.add(resultantForce);
        position = pos;
        debugger = new LineGraphic(position.asInt(),position.asInt());
        State.addGraphicToScene(debugger);
    }

    public Node(float a, float b){
        resultantForce.setType(ForceType.RESULTANT);
        forceVectors.add(resultantForce);
        position = new Vector2f(a, b);
        debugger = new LineGraphic(position.asInt(),position.asInt());
        State.addGraphicToScene(debugger);
    }

    /**
     * Add a force vector to move the node on update, is added to the resultant force, a vector composed of all the
     * forces acting on this specific node.
     * @param forceVector
     */
    @Override
    public void addForceVector(ForceVector forceVector) {
        if(forceVector.isNull()){
            return;
        }
        forceVectors.add(forceVector);
        resultantForce.add(forceVector);
    }

    @Override
    public void addForceVector(Vector2f forceVector) {

    }

    /**
     * Override the current position of the node and move it to a new position
     * @param newPosition
     */
    @Override
    public void MoveTo(Vector2f newPosition) {
        setPosition(newPosition);
    }

    /**
     * Move the node based on its resultant force
     */
    @Override
    public void Move() {
        resultantForce.mul(Simulation.TIMESTEP);
        position.add(resultantForce);
        State.addToResultantForce(resultantForce);
        if(resultantForce.mag() > Model.largestResultantForce.mag())
        {
            Model.largestResultantForce = resultantForce;
        }
        debugger.posA = position.asInt();
        debugger.posB = position.add(resultantForce.mul(10)).asInt();
    }

    /**
     * Sets resultant force to 0
     */
    public void resetResultantForce(){
        forceVectors.clear();
        resultantForce.set(new Vector2f(0));
        forceVectors.add(resultantForce);
    }

    /**
     * Clone a node at this current position
     * @return
     */
    public Node clone(){
        return new Node(this.getPosition());
    }

    /**
     * Mirror a node across the y (below this function, same for x)
     * axis as determined by the boundaries of the simulation window
     */
    public void mirrorAcrossYAxis(){
        int xOffset = Simulation.bounds.x;
        Vector2f pos = getPosition();
        setPosition(new Vector2f(-pos.x + xOffset, pos.y));
    }

    public void mirrorAcrossXAxis(){
        int yOffset = Simulation.bounds.y;
        Vector2f pos = getPosition();
        setPosition(new Vector2f(pos.x, -pos.y + yOffset));
    }

    public static void addIfAvailable(List<Node> nodes, Node n){
        if(!nodes.contains(n)) nodes.add(n);
    }

    public static List<Node> getAllUnique(Node[] a, Node[] b){
        List<Node> uniqueNodes = new ArrayList<>();
        for(Node n_a: a){
            uniqueNodes.add(n_a);
        }
        for(Node n_b: b){
            addIfAvailable(uniqueNodes, n_b);
        }
        return uniqueNodes;
    }
}
