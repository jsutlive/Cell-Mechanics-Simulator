package Physics.Rigidbodies.Nodes;

import Engine.Simulation;
import Engine.States.State;

import GUI.Vector.LineGraphic;
import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceType;
import Utilities.Physics.ForceVector2D;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Node: A vertex-like object which can implement physics for simulations.
 */
public class Node2D extends Node {

    private transient ForceVector2D resultantForce = new ForceVector2D();
    private List<ForceVector2D> forceVectors = new ArrayList<>();

    private transient LineGraphic debugger;

    public Vector2f getPosition()
    {
        return (Vector2f) position;
    }
    protected void setPosition(Vector pos){
        position = pos;
    }

    public Vector2f getResultantForce()
    {
        return resultantForce;
    }

    public Node2D()
    {
        resultantForce.setType(ForceType.RESULTANT);
        resultantForceVector = Vector2f.zero;
        forceVectors.add(resultantForce);
        position = new Vector2f(0);
        //debugger = new LineGraphic(getPosition().asInt(),getPosition().asInt());
        //State.addGraphicToScene(debugger);
    }
    public Node2D(Vector2f pos)
    {
        resultantForce.setType(ForceType.RESULTANT);
        resultantForceVector = Vector2f.zero;
        forceVectors.add(resultantForce);
        position = pos;
        //debugger = new LineGraphic(getPosition().asInt(),getPosition().asInt());
        //State.addGraphicToScene(debugger);
    }

    public Node2D(float a, float b){
        resultantForce.setType(ForceType.RESULTANT);
        resultantForceVector = Vector2f.zero;
        forceVectors.add(resultantForce);
        position = new Vector2f(a, b);
        //debugger = new LineGraphic(getPosition().asInt(),getPosition().asInt());
        //State.addGraphicToScene(debugger);
    }

    /**
     * Add a force vector to move the node on update, is added to the resultant force, a vector composed of all the
     * forces acting on this specific node.
     * @param forceVector object with physics vector and description of physics applied
     */
    @Override
    public void addForceVector(ForceVector2D forceVector) {
        if(forceVector.isNull()){
            return;
        }
        forceVectors.add(forceVector);
        resultantForce.add(forceVector);
    }


    /**
     * Override the current position of the node and move it to a new position
     * @param newPosition position to manually move node without physics calculation
     */
    @Override
    public void moveTo(Vector2f newPosition) {
        setPosition(newPosition);
    }

    /**
     * Move the node based on its resultant force
     */
    @Override
    public void move() {
        resultantForce.add(resultantForceVector);
        resultantForce.mul(Simulation.TIMESTEP);
        position.add(resultantForce);

        //debugger.posA = getPosition().asInt();
        //debugger.posB = getPosition().add(resultantForce.mul(10)).asInt();
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
