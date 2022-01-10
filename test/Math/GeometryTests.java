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
    void check_min_max_coordinates_of_cell()
    {
        Vector2f[] minMax = Geometry.getMinMaxBoundary(cell);

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

    @Test
    void check_if_two_lines_between_1_0_and_1_2_and_perpendicular_values_intersect(){
        Vector2f p1 = new Vector2f(1,0);
        Vector2f p2 = new Vector2f(1, 2);
        Vector2f p3 = new Vector2f(0,1);
        Vector2f p4 = new Vector2f(2,1);
        assertTrue(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_two_lines_that_meet_at_a_point_intersect(){
        Vector2f p1 = new Vector2f(0,0);
        Vector2f p2 = new Vector2f(0, 2);
        Vector2f p3 = new Vector2f(0,0);
        Vector2f p4 = new Vector2f(2, 0);
        assertTrue(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_two_nonperpendicular_lines_intersect(){
        Vector2f p1 = new Vector2f(0,0);
        Vector2f p2 = new Vector2f(2,2);
        Vector2f p3 = new Vector2f(0,1);
        Vector2f p4 = new Vector2f(2,1);
        assertTrue(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_two_parallel_lines_do_not_intersect(){
        Vector2f p1 = new Vector2f(0,0);
        Vector2f p2 = new Vector2f(0,2);
        Vector2f p3 = new Vector2f(2, 0);
        Vector2f p4 = new Vector2f(2, 2);
        assertFalse(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_line_to_infinity_intersects(){
        Vector2f p1 = new Vector2f(0,2);
        Vector2f p2 = new Vector2f(2, 2);
        Vector2f p3 = new Vector2f(1, 1.5f);
        Vector2f p4 = Geometry.APPROX_INF;
        assertTrue(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_line_to_infinity_intersects_does_not_intersect(){
        Vector2f p1 = new Vector2f(2,0);
        Vector2f p2 = new Vector2f(0, 0);
        Vector2f p3 = new Vector2f(1, 1.5f);
        Vector2f p4 = Geometry.APPROX_INF;
        assertFalse(Geometry.doEdgesIntersect(p1,p2,p3,p4));
    }

    @Test
    void check_if_nearest_point_from_1_0_to_line_between_0_1_and_2_1_is_1_1(){
        Vector2f p1 = new Vector2f(1, 0);
        Vector2f p2 = new Vector2f(1, 2);

        Edge e = new BasicEdge(new Node(p1), new Node(p2));
        Vector2f n = new Vector2f(0,1);

        Vector2f intersection = Geometry.getNearestPointOnLine(e, n);
        assertEquals(intersection.x, 1);
        assertEquals(intersection.y, 1);
    }

    @Test
    void check_if_edge_closest_to_point_1_1o5_is_e2(){
        Node test = new Node(new Vector2f(1, 1.5f));
        Edge e = Geometry.getClosestEdgeToPoint(cell, test);
        System.out.println(testEdges.get(0));
        System.out.println(testEdges.get(1)); // TOP EDGE
        System.out.println(testEdges.get(2));
        System.out.println(testEdges.get(3));

        assertEquals(e, testEdges.get(1) );
    }


}
