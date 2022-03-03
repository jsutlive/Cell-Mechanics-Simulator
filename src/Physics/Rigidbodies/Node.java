package Physics.Rigidbodies;

import Engine.Renderer;
import Engine.Simulation;
import Engine.States.State;
import GUI.IColor;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;

/**
 * Node: A vertex-like object which can implement physics for simulations.
 */
public class Node implements IRigidbody, IColor {

    private Vector2f position;
    private Vector2f resultantForce = new Vector2f(0);
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
    public boolean hasMoved = false;

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
        position.add(resultantForce);
        State.addToResultantForce(resultantForce);
        resetResultantForce();
    }

    /**
     * Sets resultant force to 0
     */
    public void resetResultantForce(){ resultantForce = new Vector2f(0f);}

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
}
