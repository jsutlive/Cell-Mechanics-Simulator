package Utilities.Geometry;

import java.util.Vector;

public class Vector2f
{
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(float x) {
        this.x = x;
        this.y = x;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public void add(Vector2f vec)
    {
        x += vec.x;
        y += vec.y;
    }

    public void sub(Vector2f vec)
    {
        x -= vec.x;
        y -= vec.y;
    }

    public void mul(int i)
    {
        x *= i;
        y *= i;
    }

    public void mul(float f)
    {
        x *= f;
        y *= f;
    }

    public Vector2f copy()
    {
        Vector2f c = new Vector2f();
        c.x = this.x;
        c.y = this.y;
        return c;
    }

    public float cross(Vector2f vec)
    {
        return (x * vec.y) - (vec.x * y);
    }

    public float dot(Vector2f vec)
    {
        return (x * vec.x) + (y * vec.y);
    }

    public Vector2i asInt()
    {
        int xi = (int)x;
        int yi = (int)y;
        return new Vector2i(xi,yi);
    }

    public float greater()
    {
        return Math.max(x,y);
    }

    public float lesser()
    {
        return Math.min(x,y);
    }

    public static float dist(Vector2f a, Vector2f b){
        return (float)Math.hypot(b.x -a.x, b.y - a.y);
    }

    public float mag() {return(float)Math.sqrt(x*x + y*y);}

    public static Vector2f unit(Vector2f a, Vector2f b)
    {
        float d = Vector2f.dist(a, b);
        float x = b.x-a.x;
        float y = b.y - a.y;
        return new Vector2f(x/d,y/d);
    }

    public static Vector2f unit(Vector2f vec){
        return unit(new Vector2f(0), vec);
    }

    public static Vector2f unit(float f){
        return unit(new Vector2f(f, 1f));
    }

    public String print(){
        return x + "," + y;
    }

    public Vector2f project(Vector2f v){
        return new Vector2f(0);
    }

    public boolean isNull(){
        if(Float.isNaN(x) || Float.isNaN(y)) return true;
        else return false;
    }
}
