package Utilities.Physics;

import Utilities.Geometry.Vector.Vector2f;

public class Collision2D {

    /**
     * Find unit vector describing the angle of a given point from center given which segment it is and the distance
     * from the center
     * @param segment current segment
     * @param point total segments in the circle
     * @return an x,y vector2 (floating point) describing the unit vector.
     */
    public static Vector2f closestPointToSegmentFromPoint(Vector2f point, Vector2f[] segment){
        Vector2f edgeVector = segment[1].copy().sub(segment[0]);
        Vector2f pointToEdgeP0 = point.copy().sub(segment[0]);

        float p0Dot = pointToEdgeP0.dot(edgeVector);
        float magnitudeSquared = edgeVector.dot(edgeVector);
        if (p0Dot <= 0){return segment[0].copy();}
        if (p0Dot > magnitudeSquared){return segment[1].copy();}

        Vector2f closestPoint = segment[0].copy();
        closestPoint.add(edgeVector.mul(p0Dot/magnitudeSquared));

        return closestPoint;
    }


}
