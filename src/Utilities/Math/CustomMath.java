package Utilities.Math;

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
}
