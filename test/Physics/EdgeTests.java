package Physics;

import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.LateralEdge;
import Physics.Rigidbodies.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests
{
    @Test
    void geometric_functions_work_for_simple_right_triangle_3_4_5()
    {
        // Make a 3-4-5 right triangle
        Node a = new Node(0f,0f);
        Node b = new Node(3f, 0f);
        Node c = new Node(3f, 4f);

        Edge A = new LateralEdge(a, b);
        Edge B = new LateralEdge(b,c);
        Edge C = new LateralEdge(a, c);

        assertEquals(5, C.getLength());
        assertEquals(3, A.getLength());
        assertEquals(4, B.getLength());
        assertEquals( A.getLength(), C.getXUnit() * C.getLength());
        //assertEquals(B.getLength(), C.getYUnit() * C.getLength());
    }
}
