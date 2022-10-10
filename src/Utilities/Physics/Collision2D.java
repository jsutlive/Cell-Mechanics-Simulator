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
        // calculate vector distance between points in line segments
        Vector2f edgeVector = segment[1].copy().sub(segment[0]);

        // calculate vector distance between point to first point on edge segment
        Vector2f pointToEdgeP0 = point.copy().sub(segment[0]);

        float p0Dot = pointToEdgeP0.dot(edgeVector);
        float magnitudeSquared = edgeVector.dot(edgeVector);

        // If the magnitude is either less than zero or greater than the square of the magnitude, set it equal to
        // either one of the points in the segment
        /*if (p0Dot <= 0) return segment[0].copy().unit();
        if (p0Dot > magnitudeSquared) return segment[1].copy().unit();
        */
        float t = Math.max(0, Math.min(1, p0Dot/magnitudeSquared));
        Vector2f edgeVectorTFactor = (edgeVector).mul(-t);  // Projection falls on the segment
        Vector2f projection = edgeVectorTFactor.add(point);
        /*
        Vector2f closestPoint = segment[0].copy();
        System.out.println(edgeVector.mul(p0Dot/magnitudeSquared).print());
        closestPoint.add(edgeVector.mul(p0Dot/magnitudeSquared));
        */
        return projection;
    }


}
