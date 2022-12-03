package Math;
import Framework.Object.Entity;
import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GeometryTests {
    float ninetyDegreesAsRadians = (float)Math.PI/2;
    float fortyFiveDegreesAsRadians = (float)Math.PI/4;
    float oneHundredEightyDegreesAsRadians = (float)Math.PI;
    float root2Over2 = CustomMath.round((float)Math.sqrt(2)/2,3);

    static List<Node2D> testNodes;
    static List<Edge> testEdges;
    static Entity cell;

    @BeforeAll
    static void set_test_variables()
    {
        testNodes = new ArrayList<>();
        testEdges = new ArrayList<>();

        Node2D a = new Node2D(new Vector2f(0,0));
        Node2D b = new Node2D(new Vector2f(0,2));
        Node2D c = new Node2D(new Vector2f(2,2));
        Node2D d = new Node2D(new Vector2f(2, 0));
        testNodes.add(a); testNodes.add(b); testNodes.add(c); testNodes.add(d);

        Edge e1 = new Edge(a, b);
        Edge e2 = new Edge(b, c);
        Edge e3 = new Edge(c, d);
        Edge e4 = new Edge(d, a);
        testEdges.add(e1); testEdges.add(e2); testEdges.add(e3); testEdges.add(e4);
        cell = new Entity();
        //cell.setNodes(testNodes);
        //cell.setEdges(testEdges);
    }

    @Test
    void check_approximation(){
        Vector2f base = new Vector2f(30f, -20f);
        Vector2f test = new Vector2f(30.02f,-20.05f);
        assertTrue(base.approx(test));
    }

    @Test
    void check_accuracy_of_vector2f_distance(){
        Vector2f a = new Vector2f(0,3);
        Vector2f b = new Vector2f(4,0);
        assertEquals(5f, Vector2f.dist(a,b));
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

        Edge e = new Edge(new Node2D(p1), new Node2D(p2));
        Vector2f n = new Vector2f(0,1);

        Vector2f intersection = Geometry.getNearestPointOnLine(e, n);
        assertEquals(intersection.x, 1);
        assertEquals(intersection.y, 1);
    }

    @Test
    void check_if_point_is_on_line()
    {
        Vector2f a = new Vector2f(0,0);
        Vector2f b = new Vector2f(2, 2);
        Vector2f c = new Vector2f(1, 1);
        Vector2f[] line = new Vector2f[]{a,b};
        boolean isOnLine = Geometry.lineSegmentContainsPoint(c, line);
        assertTrue(isOnLine);
    }

    @Test
    void check_if_point_is_not_on_line()
    {
        Vector2f a = new Vector2f(0,0);
        Vector2f b = new Vector2f(3, 2);
        Vector2f c = new Vector2f(1, 1);
        Vector2f[] line = new Vector2f[]{a,b};
        boolean isOnLine = Geometry.lineSegmentContainsPoint(c, line);
        assertFalse(isOnLine);
    }

    @Test
    void check_if_90_degree_angle_is_formed_by_three_points(){
        Vector2f p1 = new Vector2f(0,1);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(1,0);

        float ans = CustomMath.round(Math.abs(Geometry.calculateAngleBetweenPoints(p1,p2,p3)), 5);

        assertEquals(270, ans);
     }

    @Test
    void check_if_45_degree_angle_is_formed_by_three_points(){
        Vector2f p1 = new Vector2f(root2Over2, root2Over2);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(1,0);

        float ans = CustomMath.round(Math.abs(Geometry.calculateAngleBetweenPoints(p1,p2,p3)), 5);

        assertEquals(315, ans);
    }

    @Test
    void check_if_180_degree_angle_is_formed_by_three_points(){
        Vector2f p1 = new Vector2f(-1,0);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(1,0);
        float ans = CustomMath.round((Math.abs(Geometry.calculateAngleBetweenPoints(p1,p2,p3))),5);
        assertEquals(180, ans);
    }

    @Test
    // we show previously as a standard right angle, the correct angle is returned.
    // Does it still work when the angle is flipped?
    void check_if_270_degree_angle_formed(){
        Vector2f p1 = new Vector2f(-1,0);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(0,-1);

        float ans = CustomMath.round(Math.abs(Geometry.calculateAngleBetweenPoints(p1,p2,p3)),5);
        assertEquals(90, ans);
    }

    @Test
    void check_if_we_can_move_a_point_ninety_degrees_clockwise(){
        Vector2f p1 = new Vector2f(0,0);
        Vector2f p2 = new Vector2f(0,1);

        Vector2f p3 = Geometry.movePointAlongArc(p1, p2, 90f);
        assertEquals(1, CustomMath.round(p3.x, 5));
        assertEquals(0, CustomMath.round(p3.y, 5));
    }

    @Test
    void check_if_we_can_move_a_point_ninety_degrees_counter_clockwise(){
        Vector2f p1 = new Vector2f(0,0);
        Vector2f p2 = new Vector2f(0,1);

        Vector2f p3 = Geometry.movePointAlongArc(p1, p2, 270f);
        assertEquals(-1, CustomMath.round(p3.x, 5));
        assertEquals(0, CustomMath.round(p3.y, 5));
    }

    @Test
    void check_if_vector_to_move_force_calculated_correctly(){
        Vector2f start = new Vector2f(0,0);
        Vector2f end = new Vector2f(0,1);
        float angle = 90;
        Vector2f force = Geometry.getForceToMovePointAlongArc(start, end, angle);
        assertEquals(root2Over2, force.x);
        assertEquals(-root2Over2, force.y);
    }

    @Test
    void check_if_vector_to_move_force_does_not_add_if_angle_equals_zero(){
        Vector2f start = new Vector2f(0,0);
        Vector2f end = new Vector2f(0,1);
        float angle = 0;
        Vector2f force = Geometry.getForceToMovePointAlongArc(start, end, angle);
        assertEquals(0, force.x);
        assertEquals(0, force.y);
    }

    @Test
    void check_if_ninety_degree_angle_is_clockwise(){
        Vector2f p1 = new Vector2f(0,1);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(1,0);

        assertTrue(Geometry.isClockwise(p1,p2,p3));
    }

    @Test
    void check_if_two_seventy_degree_angle_is_counterclockwise(){
        Vector2f p1 = new Vector2f(0,-1);
        Vector2f p2 = new Vector2f(0,0);
        Vector2f p3 = new Vector2f(1,0);

        assertFalse(Geometry.isClockwise(p1,p2,p3));
    }
}
