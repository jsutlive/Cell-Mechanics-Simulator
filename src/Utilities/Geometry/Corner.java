package Utilities.Geometry;

import Physics.Rigidbodies.Node;

import java.util.ArrayList;
import java.util.List;

public class Corner
{
    Node _a;
    Node _b;
    Node _c;

    public List<Node> nodes = new ArrayList<>();
    public Vector2f direction = new Vector2f(0);

    public Corner(Node a, Node b, Node c){
        nodes.add(a); nodes.add(b); nodes.add(c);
        _a = a;
        _b = b;
        _c = c;
    }
}
