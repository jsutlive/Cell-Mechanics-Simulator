package Physics.Rigidbodies.Nodes;

import Physics.Rigidbodies.IRigidbody;
import Utilities.Geometry.Vector.Vector;

import java.util.*;

public abstract class Node implements IRigidbody {
    protected Vector position;
    protected HashMap<String, Vector> forceVectors = new HashMap<>();
    protected Vector resultantForceVector;
    public abstract Vector getPosition();
    protected  abstract void setPosition(Vector vector);
    public abstract Vector getResultantForce();

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

    public abstract void resetResultantForce();

}
