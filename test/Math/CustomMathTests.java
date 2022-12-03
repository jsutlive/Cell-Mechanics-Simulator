package Math;

import Morphogenesis.Rigidbodies.Edge;
import Morphogenesis.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomMathTests {
    @Test
    void calculate_normal_perpendicular_horiz_line()
    {
        Vector2f a = new Vector2f(0);
        Vector2f b = new Vector2f(0, 1);
        Vector2f norm = (Vector2f)CustomMath.normal(a, b);
        assertEquals(0, norm.y);
        assertEquals(-1, norm.x);
    }

    @Test
    void calculate_normal_flipped_perpendicular_horiz_line()
    {
        Vector2f a = new Vector2f(0);
        Vector2f b = new Vector2f(0, 1);
        Vector2f norm = CustomMath.normalFlipped(a, b);
        assertEquals(1f, norm.y);
        assertEquals(-0.0f, norm.x);
    }

    @Test
    void calculate_edge_normal_horiz_line(){
        Node2D a = new Node2D(0,0);
        Node2D b = new Node2D(0, 1);
        Edge e = new Edge(a, b);
        Vector2f norm = (Vector2f)CustomMath.normal(e);
        assertEquals(0.0f, norm.y);
        assertEquals(1.0f, norm.x);
    }

    @Test
    /*
    Should return zero since there a perpendicular line could not be drawn
     */
    void check_accuracy_of_pDistanceSq_point_on_line_extension(){
        Vector2f a = new Vector2f(0,0);
        Vector2f b = new Vector2f(5,0);
        Vector2f p = new Vector2f(6,0);
        assertEquals(0f, CustomMath.pDistanceSq(p, a, b));
    }

    @Test
    void check_accuracy_pDistanceSq_from_outside_line_segment(){
        Vector2f a = new Vector2f(0,5);
        Vector2f b = new Vector2f(5,5);
        Vector2f p = new Vector2f(6,0);
        assertEquals(25f, CustomMath.pDistanceSq(p, a, b));
    }

    @Test
    void calculate_edge_normal_45_degree_angle()
    {
        float root2 = (float) (Math.sqrt(2)/2);
        Node2D a = new Node2D(new Vector2f(0));
        Node2D b = new Node2D(new Vector2f(root2));
        Edge e = new Edge(a,b);
        Vector2f norm = (Vector2f) CustomMath.normal(e);
        //assertEquals(-root2, norm.y);
        assertEquals(root2, norm.x);
    }

    @Test
    void calculate_unit_vector_30_degrees(){
        int totalSegments = 12;
        int currentSegment = 1;
        Vector2f v = CustomMath.GetUnitVectorOnCircle(currentSegment, totalSegments);
        assertEquals(.5f, v.x);
        assertEquals(.8660254f, v.y);
    }

    @Test
    void calculate_inverse()
    {
        assertEquals(.2f, CustomMath.inv(-5f));
        assertEquals(Float.NEGATIVE_INFINITY, CustomMath.inv(0f));
        assertEquals(-10f, CustomMath.inv(0.1f));
    }

    @Test
    void calculate_slope()
    {
        assertEquals(.75f, CustomMath.slope(new Vector2f(0), new Vector2f(4,3)));
    }

    @Test
    void check_perpendicular_distance_simple_line()
    {
        Node2D n = new Node2D(0,1);
        Edge e = new Edge(
                new Node2D(-1,0),
                new Node2D(1,0));

        float dist = CustomMath.pDistanceSq(n, e);
        assertEquals(1f, dist);
    }

    @Test
    void check_perpendicular_distance_simple_line_at_end_of_edge()
    {
        Node2D n = new Node2D(-1,1);
        Edge e = new Edge(
                new Node2D(-1,0),
                new Node2D(1,0));
        float dist = CustomMath.pDistanceSq(n, e);
        assertEquals(1f, dist);
    }

    @Test
    void check_perpendicular_distance_simple_line_diagonal_to_edge()
    {
        Node2D n = new Node2D(-4,4);
        Edge e = new Edge(
                new Node2D(-1,0),
                new Node2D(1,0));
        float dist = CustomMath.pDistanceSq(n, e);
        assertEquals(16f, dist);
    }

    @Test
    void check_point_slope_works()
    {
        Vector2f p = new Vector2f(1, 2);
        Vector2f a = new Vector2f(1, 1);
        Vector2f b = new Vector2f(2, 2);

        Vector2f pointOnLine = CustomMath.pointSlope(p,a,b);
        assertEquals(1.5f, pointOnLine.x);
        assertEquals(1.5f, pointOnLine.y);
    }

    @Test
    void check_point_slope_works_with_point_on_line()
    {
        Vector2f p = new Vector2f(3, 3);
        Vector2f a = new Vector2f(1, 1);
        Vector2f b = new Vector2f(2, 2);

        Vector2f pointOnLine = CustomMath.pointSlope(p,a,b);
        assertEquals(3f, pointOnLine.x);
        assertEquals(3f, pointOnLine.y);
    }
}
