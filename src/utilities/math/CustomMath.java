package utilities.math;

import framework.object.Entity;
import framework.rigidbodies.Edge;
import framework.rigidbodies.Node2D;
import utilities.geometry.Geometry;
import utilities.geometry.Vector.Vector;
import utilities.geometry.Vector.Vector2f;

public class CustomMath {

    public static Vector2f ClosestPointToSegmentFromPoint(Vector2f point, Vector2f[] segment){
        Vector2f edgeVector = segment[1].sub(segment[0]);
        Vector2f pointToEdgeP0 = point.sub(segment[0]);

        float p0Dot = pointToEdgeP0.dot(edgeVector);
        float magnitudeSquared = edgeVector.dot(edgeVector);
        if (p0Dot <= 0){return segment[0].copy();}
        if (p0Dot > magnitudeSquared){return segment[1].copy();}

        Vector2f closestPoint = segment[0].copy();
        closestPoint = closestPoint.add(edgeVector.mul(p0Dot/magnitudeSquared));

        return closestPoint;
    }

    /**
     * Find unit vector describing the angle of a given point from center given which segment it is and the distance
     * from the center
     * @param currentSegment current segment
     * @param circleSegments total segments in the circle
     * @return an x,y vector2 (floating point) describing the unit vector.
     */
    public static Vector2f GetUnitVectorOnCircle(int currentSegment, float circleSegments)
    {
        float theta = (currentSegment/circleSegments) * 360f;
        return GetUnitVectorOnCircle(theta);
    }

    public static Vector2f GetUnitVectorOnCircle(float theta){
        float thetaRadians = (float)Math.toRadians(theta);
        float x = (float)Math.sin(thetaRadians);
        float y = (float)Math.cos(thetaRadians);
        return new Vector2f(x, y);
    }

    /**
     * change a coordinate from polar values to cartesian values.
     * @param unitVector equivalent of "theta" when considering polar coordinates
     * @param radius equivalent of "r" when considering polar coordinates
     * @return 2D vector indicating a point in world space
     */
    public static Vector2f TransformToWorldSpace(Vector2f unitVector, float radius) {
        return new Vector2f(unitVector.x * radius, unitVector.y * radius);
    }

    public static float sq(float f)
    {
        return f*f;
    }

    public static int sq(int i)
    {
        return i*i;
    }

    /**
     * find the average of two floating point numbers
     * @param a float 1
     * @param b float 2
     * @return average
     */
    public static float avg(float a, float b){
        float[] floats = new float[]{a,b};
        return avg(floats);
    }

    /**
     * average all floating point numbers in the array
     * @param floats an array of floating point numbers
     * @return the average
     */
    public static float avg(float[] floats)
    {
        int len = floats.length;
        float sum = 0;
        for(float f: floats)
        {
            sum += f;
        }
        return sum/len;
    }

    /**
     * Round a given floating point variable to a certain number of decimal places where
     * @param val the value to be rounded
     * @param places the number of decimal places to round the number to
     * @return the rounded number
     */
    public static float round(float val, float places)
    {
        double scale = Math.pow(10, places);
        return (float)( Math.round(val*scale)/scale);
    }

    public static Vector2f round(Vector2f vec, float places)
    {
        float x =round(vec.x, places);
        float y = round(vec.y, places);
        return new Vector2f(x, y);
    }

    /**
     * returns absolute value of a number
     * @param val a floating point number
     * @return absolute value of val
     */
    public static float abs(float val)
    {
        if(val<0) val *= -1f;
        return val;
    }

    public static Vector2f abs(Vector2f vec)
    {
        vec.x = abs(vec.x);
        vec.y = abs(vec.y);
        return vec;
    }

    public static float slope(Vector2f a, Vector2f b){
        return (b.y - a.y)/ (b.x - a.x);
    }

    /**
     * Returns a unit vector corresponding to the normal of an edge
     * @param edge home edge
     * @return normal to input edge
     */
    public static Vector normal(Edge edge){
        Vector2f[] positions = edge.getPositions();
        return normal(positions[1], positions[0]);
    }

    public static Vector2f normalFlipped(Edge edge){
        Vector2f[] positions = edge.getPositions();
        return normalFlipped(positions[0], positions[1]);
    }

    /**
     * Return inverse of a floating point value
     * @param val a floating point number
     * @return the inverse of this number
     */
    public static float inv(float val){
        return -(1/val);
    }

    /**
     * Calculate the normal of a line between two points
     * @param a point a
     * @param b point b
     * @return the normal of the line made by a and b
     */
    public static Vector normal(Vector2f a, Vector2f b){
        Vector2f unit;
        unit = new Vector2f(-(b.y-a.y), b.x-a.x);
        return unit.unit();
    }

    /**
     * Return "flipped" normal, or the negative of the regular normal
     * @param a a 2-dimensional vector representing a 2D point
     * @param b a 2-dimensional vector representing another 2D point
     * @return normal (counterclockwise) of the line segment between a and b
     */
    public static Vector2f normalFlipped(Vector2f a, Vector2f b){
        Vector2f unit = (Vector2f) normal(a,b);
        unit.mul(-1);
        Vector2f flipped = new Vector2f();
        flipped.x = unit.y;
        flipped.y = unit.x;
        return flipped;
    }
    /**
     * return perpendicular distance squared to an edge from a node
     * @param n a given node in space
     * @param e the edge we want to calculate distance to
     * @return float describing perpendicular distance from a node to the edge
     */
    public static float pDistanceSq(Node2D n, Edge e){
        Vector2f[] edgePositions = e.getPositions();
        return pDistanceSq(n.getPosition(), edgePositions[0], edgePositions[1]);
    }

    public static float pDistanceSq(Entity c, Node2D n, Edge e){
        Vector2f[] edgePositions = e.getPositions();
        return pDistanceSq(c, n.getPosition(), edgePositions[0], edgePositions[1]);
    }

    public static float pDistanceSq(Entity c, Vector2f p, Vector2f a, Vector2f b) {

        if(a.isNull()){
            throw new NullPointerException("Null value for vector a at cell " + c.getStateID());
        }
        if(b.isNull()){
            throw new NullPointerException("Null value for vector b");
        }
        if(p.isNull()){
            throw new NullPointerException("Null value for vector p");
        }
        float A = p.x - a.x; // position of point rel one end of line
        float B = p.y - a.y;
        float C = b.x - a.x; // vector along line
        float D = b.y - a.y;
        float E = -D; // orthogonal vector
        float F = C;

        float dot = A * E + B * F;
        float len_sq = sq(E) + sq(F);
        if(len_sq == 0){
            throw new IllegalArgumentException("DIVIDE BY ZERO ERROR");
        }
        return sq(dot) / len_sq;
    }

    /**
     * return perpendicular distance squared to the line made between two points from another point
     * @param p a point in space
     * @param a point 1 in the line
     * @param b point 2 in the line
     * @return a float describing perpendicular distance from p to the line made by a and b
     */
    public static float pDistanceSq(Vector2f p, Vector2f a, Vector2f b) {
        float A = p.x - a.x; // position of point rel one end of line
        float B = p.y - a.y;
        float C = b.x - a.x; // vector along line
        float D = b.y - a.y;
        float E = -D; // orthogonal vector
        float F = C;

        float dot = A * E + B * F;
        float len_sq = sq(E) + sq(F);
        if(len_sq == 0){
            throw new IllegalArgumentException("DIVIDE BY ZERO ERROR");
        }
        return sq(dot) / len_sq;
    }

    public static Vector2f pointSlope(Node2D n, Edge e){
        Vector2f[] edgePositions = e.getPositions();
        return pointSlope(n.getPosition(), edgePositions[0], edgePositions[1]);
    }

    public static Vector2f pointSlope(Vector2f p, Vector2f a, Vector2f b){

        if(Geometry.lineSegmentContainsPoint(p,a,b)) return Geometry.APPROX_INF;
        if(Geometry.lineSegmentContainsPoint(a,p,b)) return Geometry.APPROX_INF;
        if(Geometry.lineSegmentContainsPoint(p,b,a)) return  Geometry.APPROX_INF;
        float slope = (b.y - a.y)/(b.x - a.x);
        float intercept = a.y - (slope*a.x);
        float normalSlope = -1f/slope;
        float normalIntercept = p.y - (normalSlope * p.x);
        float x = (normalIntercept - intercept)/(slope-normalSlope);
        float y = (slope * x) + intercept;
        if(Float.isNaN(x) || Float.isNaN(y)) {
            return Geometry.APPROX_INF;

        }
        return new Vector2f(x, y);
    }

    public static float clamp(float val, float min, float max)
    {
        if(val > max) val = max;
        if(val < min) val = min;
        return val;
    }

    public static float floor(float val, float min)
    {
        if(val < min) val = min;
        return val;
    }

}
