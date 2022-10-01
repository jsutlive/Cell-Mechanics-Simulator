package Physics;

import Morphogenesis.Rigidbodies.Edges.BasicEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeEach;

public class ForceTests {

    Edge edge;
    private final Vector2f pos00 = new Vector2f(0, 0);
    private final Vector2f pos01 = new Vector2f(0, 1);

    @BeforeEach
    void setup() {
        Node2D a = new Node2D(pos00);
        Node2D b = new Node2D(pos01);
        edge = new BasicEdge(a, b);
    }
}
