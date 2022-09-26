package Physics.Rigidbodies.Nodes;

import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceVector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Node {
    protected Vector position;
    public abstract Vector getPosition();
    abstract void setPosition(Vector vector);
    abstract Vector getResultantForce();

    public static  void addIfAvailable(List<Node> nodes, Node n){
        if(!nodes.contains(n)) nodes.add(n);
    }

    public static List<Node> getAllUnique(Node[] a, Node[] b){
        List<Node> uniqueNodes = new ArrayList<>();
        uniqueNodes.addAll(Arrays.asList(a));
        for(Node n_b: b){
            addIfAvailable(uniqueNodes, n_b);
        }
        return uniqueNodes;
    }


}
