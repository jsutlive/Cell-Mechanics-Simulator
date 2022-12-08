package Framework.Rigidbodies;

import Utilities.Geometry.Vector.Vector;

import static Framework.States.RunState.deltaTime;

import java.util.*;

public abstract class Node implements IRigidbody {
    protected Vector position;
    protected transient Vector initialPosition;
    protected transient HashMap<String, Vector> forceVectors = new HashMap<>();
    protected transient Vector resultantForceVector;

    public abstract Vector getPosition();
    protected  abstract void setPosition(Vector vector);

    public void reset(){
        try {
            position = initialPosition;
        }catch(NullPointerException e){
            throw new NullPointerException("Node position or node initial position is null");
        }
    }

    public abstract Node clone();

    public abstract void mirrorAcrossYAxis();

    @Override
    public void addForceVector(Vector forceVector) {
        forceVectors.put("", forceVector);
        resultantForceVector = resultantForceVector.add(forceVector);
    }

    public void addForceVector(String type, Vector forceVector){
        resultantForceVector = resultantForceVector.add(forceVector);
        forceVectors.put(type, forceVector);
    }

    /**
     * Override the current position of the node and move it to a new position
     * @param newPosition position to manually move node without physics calculation
     */
    public void moveTo(Vector newPosition) {
        setPosition(newPosition);
    }

    /**
     * Move the node based on its resultant force
     */
    @Override
    public void move() {
        position = position.add(resultantForceVector.mul(deltaTime));
    }

    /**
     * Sets resultant force to 0 and clears the forceVectors hashmap
     */
    public void resetResultantForce(){
        forceVectors.clear();
        resultantForceVector = resultantForceVector.zero();
    }
}
