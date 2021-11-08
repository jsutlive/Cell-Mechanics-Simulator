package Utilities.Math;

import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;

public class CustomMath {
    /**
     *
     * @param index
     * @param circleSegments
     * @param lateralResolution
     * @return
     */
    public static Vector2f GetUnitVectorOnCircle(int index, float circleSegments, float lateralResolution)
    {
        float x = (float) Math.sin((float)(index*8.0f*Math.PI/circleSegments/lateralResolution));
        float y = (float) Math.cos((float)(index*8.0f*Math.PI/circleSegments/lateralResolution));
        return new Vector2f(x, y);
    }

    public static Vector2f TransformToWorldSpace(Vector2f unitVector, float radius, Vector2f axis)
    {
        Vector2f worldScale = new Vector2f(unitVector.x * radius, unitVector.y * radius);
        Vector2f axisWorld = new Vector2f(axis.greater()/2);
        worldScale.add(axisWorld);
        return worldScale;
    }

    public static float sq(float f)
    {
        return f*f;
    }

    public static int sq(int i)
    {
        return i*i;
    }

    public static float avg(float a, float b){
        float[] floats = new float[]{a,b};
        return avg(floats);
    }

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

    public static float round(float val, float places)
    {
        double scale = Math.pow(10, places);
        return (float)( Math.round(val*scale)/scale);
    }
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
     * @param edge
     * @return
     */
    public static Vector2f normal(Edge edge){
        Vector2f[] positions = edge.getPositions();
        return normal(positions[1], positions[0]);
    }

    public static Vector2f normalFlipped(Edge edge){
        Vector2f[] positions = edge.getPositions();
        return normalFlipped(positions[0], positions[1]);
    }

    public static float inv(float val){
        return -(1/val);
    }

    public static Vector2f normal(Vector2f a, Vector2f b){
        Vector2f unit = new Vector2f();
        float slope = inv(slope(a, b));
        //unit.x = 1/(float)(Math.sqrt(sq(slope) + 1));
        //unit.y = (float)Math.sqrt(sq(1 - unit.x));
        //unit = Vector2f.unit(slope);
        unit = new Vector2f(-(b.y-a.y), b.x-a.x);
        return Vector2f.unit(unit);
    }

    public static Vector2f normalFlipped(Vector2f a, Vector2f b){
        Vector2f unit = normal(a,b);
        unit.mul(-1);
        return unit;
    }

    public static float pDistanceSq(Node n, Edge e){
        Vector2f[] edgePositions = e.getPositions();
        return pDistanceSq(n.getPosition(), edgePositions[0], edgePositions[1]);
    }

    public static float pDistanceSq(Vector2f p, Vector2f a, Vector2f b) {

        float A = p.x - a.x; // position of point rel one end of line
        float B = p.y - a.y;
        float C = b.x - a.x; // vector along line
        float D = b.y - a.y;
        float E = -D; // orthogonal vector
        float F = C;

        float dot = A * E + B * F;
        float len_sq = E * E + F * F;

        return sq(dot) / len_sq;
    }

    public static Vector2f pointSlope(Node n, Edge e){
        Vector2f[] edgePositions = e.getPositions();
        return pointSlope(n.getPosition(), edgePositions[0], edgePositions[1]);
    }

    public static Vector2f pointSlope(Vector2f p, Vector2f a, Vector2f b){
        float slope = (b.y - a.y)/(b.x - a.x);
        float intercept = a.y - (slope*a.x);
        float normalSlope = -1f/slope;
        float normalIntercept = p.y - (normalSlope * p.x);
        float x = (normalIntercept - intercept)/(slope-normalSlope);
        float y = (slope * x) + intercept;
        return new Vector2f(x, y);
    }

}
