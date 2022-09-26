package Utilities.Geometry.Vector;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(int x) {
        this.x = x;
        this.y = x;
    }

    public Vector2i() {
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

    public Vector2f asFloat()
    {
        float fx = (float)x;
        float fy = (float)y;

        return new Vector2f(fx, fy);
    }
}
