package Morphogenesis.Rigidbodies.Nodes;

import Framework.Engine;
import Morphogenesis.Rigidbodies.IRigidbody;
import Utilities.Geometry.Vector.Vector;

import java.util.*;

public abstract class Node implements IRigidbody {
    protected Vector position;
    protected HashMap<String, Vector> forceVectors = new HashMap<>();
    protected Vector resultantForceVector;
    public abstract Vector getPosition();
    protected  abstract void setPosition(Vector vector);
    public Vector getResultantForce(){
        return resultantForceVector;
    }

    public static void addIfAvailable(List<Node> nodes, Node n){
        if(!nodes.contains(n)) nodes.add(n);
    }

    public static List<Node> getAllUnique(Node[] a, Node[] b){
        List<Node> uniqueNodes = new ArrayList<>(Arrays.asList(a));
        for(Node n_b: b){
            addIfAvailable(uniqueNodes, n_b);
        }
        return uniqueNodes;
    }

    public abstract Node clone();

    public abstract void mirrorAcrossXAxis();
    public abstract void mirrorAcrossYAxis();

    @Override
    public void addForceVector(Vector forceVector) {
        forceVectors.put("", forceVector);
        resultantForceVector.add(forceVector);
    }

    public void addForceVector(String type, Vector forceVector){
        resultantForceVector.add(forceVector);
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
        position.add(resultantForceVector.mul(Engine.TIMESTEP));
    }

    /**
     * Sets resultant force to 0 and clears the forceVectors hashmap
     */
    public void resetResultantForce(){
        forceVectors.clear();
        resultantForceVector = resultantForceVector.zero();
    }
}
