package Math;
import Model.Cell;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GeometryTests {
    static List<Node> testNodes;
    static List<Edge> testEdges;
    static Cell cell;
    @BeforeAll
    static void set_test_variables()
    {
        testNodes = new ArrayList<>();
        testEdges = new ArrayList<>();

        Node a = new Node(new Vector2f(0,0));
        Node b = new Node(new Vector2f(0,2));
        Node c = new Node(new Vector2f(2,2));
        Node d = new Node(new Vector2f(2, 0));
        testNodes.add(a); testNodes.add(b); testNodes.add(c); testNodes.add(d);

        Edge e1 = new BasicEdge(a, b);
        Edge e2 = new BasicEdge(b, c);
        Edge e3 = new BasicEdge(c, d);
        Edge e4 = new BasicEdge(d, a);
        testEdges.add(e1); testEdges.add(e2); testEdges.add(e3); testEdges.add(e4);
        cell = new Cell();
        cell.setNodes(testNodes);
        cell.setEdges(testEdges);
    }

    @Test
    void check_min_max_coordinates_of_list_of_vertices()
    {
        Vector2f[] minMax = Geometry.getMinMaxBoundary(testNodes);

        Vector2f min = minMax[0];
        Vector2f max = minMax[1];
        min.print();
        max.print();
        assertEquals(min.x, 0);
        assertEquals(max.x, 2);
        assertEquals(min.y, 0);
        assertEquals(max.y, 2);
    }

    @Test
    void check_if_point_x1_y1_within_cell()
    {
        Node point = new Node(1,1.5f);
        assertTrue(Geometry.polygonContainsPoint(cell, point));
    }


}
