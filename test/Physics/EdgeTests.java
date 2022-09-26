package Physics;

import Physics.Rigidbodies.Edges.BasicEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Edges.LateralEdge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests
{
    @Test
    void geometric_functions_work_for_simple_right_triangle_3_4_5()
    {
        // Make a 3-4-5 right triangle
        Node2D a = new Node2D(0f,0f);
        Node2D b = new Node2D(3f, 0f);
        Node2D c = new Node2D(3f, 4f);

        Edge A = new LateralEdge(a, b);
        Edge B = new LateralEdge(b,c);
        Edge C = new LateralEdge(a, c);

        assertEquals(5, C.getLength());
        assertEquals(3, A.getLength());
        assertEquals(4, B.getLength());
        assertEquals( A.getLength(), C.getXUnit() * C.getLength());
        //assertEquals(B.getLength(), C.getYUnit() * C.getLength());
    }

    @Test
    void get_correct_length_of_edge(){
        Node2D a = new Node2D(0,0);
        Node2D b = new Node2D(0,5);
        Edge e = new BasicEdge(a,b);
        Node2D[] nodes = e.getNodes();
        Vector2f aVec = nodes[0].getPosition();
        Vector2f bVec = nodes[1].getPosition();
        float dist = Vector2f.dist(aVec, bVec);
        assertEquals(5,dist);
    }

    @Test
    void determine_edge_contains_node(){
        Node2D n = new Node2D(0,0);
        Edge e =  new BasicEdge(
                new Node2D(0,0),
                new Node2D(0,1)
        );
        assertTrue(e.contains(n));
    }

    @Test
    void determine_edge_nodes_instances_not_altered(){
        Node2D a = new Node2D(0,0);
        Node2D b = new Node2D(0,1);
        Edge e = new BasicEdge(a,b);

        assertSame(a, e.getNodes()[0]);
        assertSame(b, e.getNodes()[1]);
    }
}
