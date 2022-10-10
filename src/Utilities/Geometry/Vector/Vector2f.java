package Utilities.Geometry.Vector;

import Utilities.Math.CustomMath;

public class Vector2f extends Vector
{
    public static Vector2f zero = new Vector2f(0);
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Apply the same value (n) to both x and y
     * @param n floating point value assigned to x and y
     */
    public Vector2f(float n) {
        this.x = n;
        this.y = n;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f add(Vector vec)
    {
        Vector2f vec2f = (Vector2f) vec;
        x += vec2f.x;
        y += vec2f.y;
        return this;
    }

    public Vector2f mul(int i)
    {
        x *= i;
        y *= i;
        return this;
    }

    @Override
    public Vector2f sub(Vector vec) {
        Vector2f vec2f = (Vector2f) vec;
        x -= vec2f.x;
        y -= vec2f.y;
        return this;
    }

    public Vector2f mul(float f)
    {
        x *= f;
        y *= f;
        return this;
    }

    @Override
    public Vector div(float f) {
        return null;
    }

    @Override
    public Vector dot(Vector vec) {
        return null;
    }

    @Override
    public Vector cross(Vector vec) {
        return null;
    }

    @Override
    public Vector2f unit() {
        return unit(new Vector2f(0), this);
    }

    @Override
    public float distanceTo(Vector b) {
        return (float)Math.hypot(b.get(0) -x, b.get(1) - y);
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

    public static float sqDist(Vector2f a, Vector2f b){
        return CustomMath.sq(a.x - b.x) + CustomMath.sq(a.y - b.y);
    }

    @Override
    public float mag() {
        return(float)Math.sqrt(x*x + y*y);
    }

    @Override
    public boolean equals(Vector vec) {
        Vector2f vec2f = (Vector2f) vec;
        return (this.x == vec2f.x && this.y == vec2f.y);
    }

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

    @Override
    public Vector2f zero() {
        return new Vector2f(0,0);
    }

    public boolean isNull(){
        return Float.isNaN(x) || Float.isNaN(y);
    }

    @Override
    public Vector2f get() {
        return this;
    }

    @Override
    public float get(int index) {
        if(index == 0) return x;
        if(index == 1) return y;
        else throw new IllegalArgumentException("Out of range argument");
    }
}
