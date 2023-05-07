package utilities.geometry.Vector;

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

    public void add(Vector2i vec)
    {
        x += vec.x;
        y += vec.y;
    }

    public Vector2i sub(Vector2i vec)
    {
        Vector2i v = new Vector2i();
        v.x = this.x - vec.x;
        v.y = this.y - vec.y;

        return v;
    }

    public Vector2f asFloat()
    {
        float fx = (float)x;
        float fy = (float)y;

        return new Vector2f(fx, fy);
    }

    public String print(){
        return x + ", " + y;
    }

}
