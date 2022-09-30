package Physics.Rigidbodies.Nodes;

import Engine.Simulation;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;

/**
 * Node: A vertex-like object which can implement physics for simulations.
 */
public class Node2D extends Node {

    public Vector2f getPosition()
    {
        return (Vector2f) position;
    }
    protected void setPosition(Vector pos){
        position = pos;
    }

    public Node2D()
    {
        resultantForceVector = Vector2f.zero;
        position = new Vector2f(0);
    }
    public Node2D(Vector2f pos)
    {
        resultantForceVector = Vector2f.zero;
        position = pos;
    }

    public Node2D(float a, float b){
        resultantForceVector = Vector2f.zero;
        position = new Vector2f(a, b);
    }

    /**
     * Clone a node at this current position
     * @return clone of node at current position
     */
    public Node2D clone(){
        return new Node2D(this.getPosition());
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


}
