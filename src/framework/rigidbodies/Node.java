package framework.rigidbodies;

import utilities.geometry.Vector.Vector;

import static framework.states.RunState.deltaTime;

import java.util.*;
/**
 * Node is the base class for independent physics agents ultimately responsible for driving the simulation forward. As
 * of May 2023 only 2D nodes have been implemented.
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public abstract class Node implements IRigidbody {
    protected Vector position;
    protected transient Vector initialPosition;
    protected transient HashMap<String, Vector> forceVectors = new HashMap<>();
    protected transient Vector resultantForceVector;

    public abstract Vector getPosition();
    protected  abstract void setPosition(Vector vector);

    /**
     * Make a copy of this node
     * @return a new Node instance with the same parameters as this
     */
    public abstract Node clone();

    /**
     * Make a copy of this node, mirrored across the y-axis plane
     */
    public abstract void mirrorAcrossYAxis();


    /**
     * Return the node to its original position
     */
    public void reset(){
        try {
            position = initialPosition;
        }catch(NullPointerException e){
            throw new NullPointerException("Node position or node initial position is null");
        }
    }

    @Override
    public void addForceVector(Vector forceVector) {
        forceVectors.put("", forceVector);
        resultantForceVector = resultantForceVector.add(forceVector);
    }

    @Override
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
     * Move the node based on its resultant force, limited to avoid major "spikes"
     */
    @Override
    public void move() {
        Vector moveVector = resultantForceVector.mul(deltaTime);
        if (!(moveVector.mag() > 3)) {
            position = position.add(moveVector);
        }
    }

    /**
     * Sets resultant force to 0 and clears the forceVectors hashmap
     */
    public void resetResultantForce(){
        forceVectors.clear();
        resultantForceVector = resultantForceVector.zero();
    }
}
