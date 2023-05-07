package utilities.geometry.Vector;

public class Vector3f extends Vector{
    public float x,y,z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public Vector add(Vector vec) {
        Vector3f v = (Vector3f)vec;
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    @Override
    public Vector sub(Vector vec) {
        Vector3f v = (Vector3f)vec;
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    @Override
    public Vector mul(float f) {
        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    @Override
    public Vector div(float f) {
        x *= 1/f;
        y *= 1/f;
        z *= 1/f;
        return this;
    }

    @Override
    public Vector dot(Vector vec) {
        Vector3f v = (Vector3f) vec;
        x = x * v.x;
        y = y * v.y;
        z = z * v.z;
        return this;
    }

    @Override
    public Vector cross(Vector vec) {
        return null;
    }

    @Override
    public Vector unit() {
        return null;
    }

    @Override
    public float distanceTo(Vector b) {
        return 0;
    }

    @Override
    public float mag() {
        return(float)Math.sqrt(x*x + y*y + z*z);
    }

    @Override
    public boolean equals(Vector vec) {
        return false;
    }

    @Override
    public boolean isNull() {
        return Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z);
    }

    @Override
    public Vector copy() {
        return null;
    }

    @Override
    public Vector get() {
        return null;
    }

    @Override
    public float get(int index) {
        return 0;
    }

    @Override
    public String print() {
        return x + "," + y + "," + z;
    }

    @Override
    public Vector3f zero() {
        return new Vector3f(0f, 0f, 0f);
    }

}
