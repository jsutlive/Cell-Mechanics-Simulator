package Morphogenesis.Rigidbodies;

import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;

public class ApicalEdgeTests {

    static ApicalEdge e;
    @BeforeAll
    static void setup() {
        Vector2f pos_1 = new Vector2f(0, 0);
        Vector2f pos_2 = new Vector2f(0, 1);

        Node2D a = new Node2D(pos_1);
        Node2D b = new Node2D(pos_2);

        e = new ApicalEdge(a, b);
    }

}
