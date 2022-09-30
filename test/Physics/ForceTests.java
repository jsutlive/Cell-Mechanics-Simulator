package Physics;

import Physics.Forces.Force;
import Physics.Rigidbodies.Edges.BasicEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
