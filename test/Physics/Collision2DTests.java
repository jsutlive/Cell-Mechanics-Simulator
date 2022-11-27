package Physics;

import Utilities.Geometry.Vector.Vector2f;
import Utilities.Physics.Collision2D;
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

        point2Dmesh = new Vector2f[]{NW_point, NE_point, SE_point, SW_point};
    }

    @Test
    void check_point_on_edge_closest_to_point_on_east_edge(){
        Vector2f checkPoint = new Vector2f(0.8f, 0.5f);
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[1], point2Dmesh[2]};
        Vector2f pointOnEdge = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);
        System.err.println(pointOnEdge.print());
        assertEquals(1.0f, pointOnEdge.x);
        assertEquals(0.5f, pointOnEdge.y);
    }

    @Test
    void check_point_on_edge_closest_to_point_on_west_edge(){
        Vector2f checkPoint = new Vector2f(0.2f, 0.5f);
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[3], point2Dmesh[0]};
        Vector2f pointOnEdge = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);
        System.err.println(pointOnEdge.print());
        assertEquals(0.0f, pointOnEdge.x);
        assertEquals(0.5f, pointOnEdge.y);
    }

    @Test
    void check_unit_vector_when_point_matches_SE_point(){
        Vector2f SE_point = point2Dmesh[2];
        Vector2f checkPoint = SE_point.copy();
        Vector2f[] eastSegment = new Vector2f[]{point2Dmesh[2], point2Dmesh[3]};

        Vector2f testPoint = Collision2D.closestPointToSegmentFromPoint(checkPoint, eastSegment);

        System.err.println(testPoint.print());

        assertEquals(SE_point.x, testPoint.x);
        assertEquals(SE_point.y, testPoint.y);
    }

}
