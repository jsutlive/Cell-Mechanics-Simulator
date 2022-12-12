package Utilities.Physics;

import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;


public class Collision2D {

    /**
     * Find point that exists on a Vector2f[] (two point) segment that describes the closest distance from the point to
     * the center
     * @param segment current segment
     * @param point total segments in the circle
     * @return an x,y vector2 (floating point) describing the unit vector.
     */
    public static Vector2f closestPointToSegmentFromPoint(Vector2f point, Vector2f[] segment){
        Vector2f lineSegment = segment[1].sub(segment[0]);
        Vector2f firstSegmentVertexToPoint = segment[0].sub(point);

        float dotProduct = lineSegment.dot(firstSegmentVertexToPoint);
        float segmentLengthSquared = CustomMath.sq(lineSegment.x) + CustomMath.sq(lineSegment.y);
        float t = -dotProduct/segmentLengthSquared;

        if(t >=0 && t<= 1) return vectorToSegment2D(t, new Vector2f(), segment);
        Vector2f result = testPoints(point, segment);
        if(result.equals(point)) return null;
        else return result;
    }

    public static Vector2f testPoints(Vector2f point, Vector2f[] segment) {
        float zeroTest = squareDiagonal2D(vectorToSegment2D(0, point, segment));
        float oneTest = squareDiagonal2D(vectorToSegment2D(1, point, segment));
        Vector2f result =  zeroTest <= oneTest ? segment[0] : segment[1];
        return result;
    }

    private static Vector2f vectorToSegment2D(float t, Vector2f point, Vector2f[] segment){
        return new Vector2f(
                (1 - t) * segment[0].x  + t * segment[1].x - point.x,
                (1 - t) * segment[0].y  + t * segment[1].y - point.y
        );
    }

    private static float squareDiagonal2D(Vector2f point){
        return CustomMath.sq(point.x) + CustomMath.sq(point.y);
    }


}
