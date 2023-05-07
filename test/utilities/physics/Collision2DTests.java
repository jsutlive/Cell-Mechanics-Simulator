package utilities.physics;

import framework.rigidbodies.Edge;
import utilities.geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Collision2DTests {

    static Vector2f[] point2Dmesh;

    @BeforeAll
    static void setup(){
        Vector2f NW_point = new Vector2f(0f, 1f);
        Vector2f NE_point = new Vector2f(1f, 1f);
        Vector2f SE_point = new Vector2f(1f, 0f);
        Vector2f SW_point = new Vector2f(0f, 0f);

        Edge W = new Edge(NW_point, SW_point);
        Edge S = new Edge(SW_point, SE_point);
        Edge E = new Edge(SE_point, NE_point);
        Edge N = new Edge(NE_point, NW_point);

        point2Dmesh = new Vector2f[]{NW_point, NE_point, SE_point, SW_point};
    }

    @Test
    void check_point_on_edge_closest_to_point_on_east_edge(){
        Vector2f checkPoint = new Vector2f(0.8f, 0.5f);
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[1], point2Dmesh[2]};
        Vector2f pointOnEdge = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);
        assertNotNull(pointOnEdge);
        assertEquals(1.0f, pointOnEdge.x);
        assertEquals(0.5f, pointOnEdge.y);
    }

    @Test
    void check_value_throws_exception_if_node_on_segment_is_null(){
        Vector2f checkPoint = new Vector2f(0,0);
        Vector2f[] segment = new Vector2f[]{new Vector2f(2,2), null};
        assertThrows(NullPointerException.class, () -> Collision2D.testPoints(checkPoint, segment));
    }

    @Test
    void check_value_when_segment_points_are_equal(){
        Vector2f checkPoint = new Vector2f(0,0);
        Vector2f[] segment  = new Vector2f[]{new Vector2f(1,1), new Vector2f(1, 1)};
        Vector2f result = Collision2D.testPoints(checkPoint, segment);
        assertEquals(1f, result.x);
        assertEquals(1f, result.y);
    }

    @Test
    void check_value_when_segment_point_and_check_points_are_equal(){
        Vector2f checkPoint = new Vector2f(0,0);
        Vector2f[] segment  = new Vector2f[]{new Vector2f(0,0), new Vector2f(1, 1)};
        Vector2f result = Collision2D.testPoints(checkPoint, segment);
        assertEquals(0f, result.x);
        assertEquals(0f, result.y);
    }

    @Test
    void check_point_on_edge_closest_to_point_on_west_edge(){
        Vector2f checkPoint = new Vector2f(0.2f, 0.5f);
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[3], point2Dmesh[0]};
        Vector2f pointOnEdge = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);
        assertNotNull(pointOnEdge);
        assertEquals(0.0f, pointOnEdge.x);
        assertEquals(0.5f, pointOnEdge.y);
    }

    @Test
    void check_unit_vector_when_point_matches_SE_point(){
        Vector2f SE_point = point2Dmesh[2];
        Vector2f checkPoint = SE_point.copy();
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[2], point2Dmesh[3]};

        Vector2f testPoint = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);
        assertNotNull(testPoint);
        assertEquals(SE_point.x, testPoint.x);
        assertEquals(SE_point.y, testPoint.y);
    }

    @Test
    void check_closest_edge_to_point_is_west_edge(){

    }

}
