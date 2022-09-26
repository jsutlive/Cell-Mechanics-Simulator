package Component;

import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.Edges.BasicEdge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MeshTests {
    static CellMesh testMesh;

    @BeforeAll
    static void createMeshes()
    {
        testMesh = new CellMesh();
        Node2D a = new Node2D(-1,-1); testMesh.nodes.add(a);
        Node2D b = new Node2D(-1, 0); testMesh.nodes.add(b);
        Node2D c = new Node2D(-1, 1); testMesh.nodes.add(c);
        Node2D d = new Node2D(0,1);  testMesh.nodes.add(d);
        Node2D e = new Node2D(1,1);  testMesh.nodes.add(e);
        Node2D f = new Node2D(1,0);  testMesh.nodes.add(f);
        Node2D g = new Node2D(1,-1);  testMesh.nodes.add(g);
        Node2D h = new Node2D(0,-1);  testMesh.nodes.add(h);

        testMesh.edges.add(new BasicEdge(a,b));
        testMesh.edges.add(new BasicEdge(b,c));
        testMesh.edges.add(new BasicEdge(c,d));
        testMesh.edges.add(new BasicEdge(d,e));
        testMesh.edges.add(new BasicEdge(e,f));
        testMesh.edges.add(new BasicEdge(f,g));
        testMesh.edges.add(new BasicEdge(g,h));
    }

    @Test
    void check_if_node_collision_algorithm_returns_no_null_positions_case_inside(){
        Node2D test = new Node2D(Vector2f.zero);
        testMesh.collidesWithNode(test);

        assertNotNull(test.getPosition().x);
        assertNotNull(test.getPosition().y);

        for(Node2D node: testMesh.nodes)
        {
            assertNotNull(node.getPosition().x);
            assertNotNull(node.getPosition().y);
        }
    }

    @Test
    void check_if_node_collision_algorithm_returns_no_null_positions_case_outside(){
        Node2D test = new Node2D(3,3);
        testMesh.collidesWithNode(test);

        assertNotNull(test.getPosition().x);
        assertNotNull(test.getPosition().y);

        for(Node2D node: testMesh.nodes)
        {
            assertNotNull(node.getPosition().x);
            assertNotNull(node.getPosition().y);
        }
    }

    @Test
    void check_if_node_collision_algorithm_returns_no_null_positions_case_coincides(){
        Node2D test = new Node2D(-1,-1);
        testMesh.collidesWithNode(test);

        assertNotNull(test.getPosition().x);
        assertNotNull(test.getPosition().y);

        for(Node2D node: testMesh.nodes)
        {
            assertNotNull(node.getPosition().x);
            assertNotNull(node.getPosition().y);
        }
    }

    @Test
    void check_if_node_collision_algorithm_returns_no_null_positions_case_on_line(){
        Node2D test = new Node2D(-0.5f,-1);
        testMesh.collidesWithNode(test);

        assertNotNull(test.getPosition().x);
        assertNotNull(test.getPosition().y);

        for(Node2D node: testMesh.nodes)
        {
            assertNotNull(node.getPosition().x);
            assertNotNull(node.getPosition().y);
        }
    }

    @Test
    void check_if_node_area_calculates_correctly_for_basic_square(){
        assertEquals(4.0f, testMesh.getArea());
    }

    @Test
    void check_if_node_area_calculated_as_larger_after_area_enlarged(){
        float initialArea = testMesh.getArea();
        testMesh.nodes.get(0).MoveTo(new Vector2f(-1,-2));
        float newArea = testMesh.getArea();
        assertTrue(newArea > initialArea);
    }

    @Test
    void check_ifi_initial_area_assigned_on_awake(){
        assertEquals(testMesh.getRestingArea(), testMesh.getArea());
    }

    @Test
    void check_if_node_area_calculated_as_smaller_after_area_lessened(){
        float initialArea = testMesh.getArea();
        testMesh.nodes.get(0).MoveTo(new Vector2f(-0.5f,-0.5f));
        float newArea = testMesh.getArea();
        assertTrue(newArea < initialArea);
    }

    @BeforeEach
    void reset_first_node(){
        testMesh.nodes.get(0).MoveTo(new Vector2f(-1,-1));
    }
}
