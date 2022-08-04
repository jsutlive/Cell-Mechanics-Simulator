package Physics.Rigidbodies;

import Engine.Simulation;
import Engine.States.State;
import GUI.IColor;
import Model.Model;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Model.Builder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Node: A vertex-like object which can implement physics for simulations.
 */
public class Node implements IRigidbody, IColor {

    private Vector2f position;
    private Vector2f resultantForce = Vector2f.zero;
    private Color color;

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
        position = new Vector2f(0);
    }
    public Node(Vector2f pos)
    {
        position = pos;
    }

    public Node(float a, float b){position = new Vector2f(a, b);}

    /**
     * Add a force vector to move the node on update, is added to the resultant force, a vector composed of all the
     * forces acting on this specific node.
     * @param forceVector
     */
    @Override
    public void AddForceVector(Vector2f forceVector) {
        resultantForce.add(forceVector);
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
        resetResultantForce();
    }

    /**
     * Sets resultant force to 0
     */
    public void resetResultantForce(){
        Simulation.FORCE_HISTORY.put(this, resultantForce.copy());
        resultantForce = new Vector2f(0);
    }




    public Node clone(){
        return new Node(this.getPosition());
    }

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
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
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
