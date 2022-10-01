package Utilities.Geometry;

import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Corner
{
    Node2D _a;
    Node2D _b;
    Node2D _c;

    public List<Node2D> nodes = new ArrayList<>();
    public Vector2f direction = new Vector2f(0);

    public Corner(Node2D a, Node2D b, Node2D c){
        nodes.add(a); nodes.add(b); nodes.add(c);
        _a = a;
        _b = b;
        _c = c;
    }
}
