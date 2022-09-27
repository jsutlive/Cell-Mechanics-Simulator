package Utilities.Geometry.Vector;

public class Vector3f extends Vector{
    public float x,y,z;


    @Override
    public Vector add(Vector vec) {
        Vector3f v = (Vector3f)vec;
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    @Override
    Vector sub(Vector vec) {
        Vector3f v = (Vector3f)vec;
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    @Override
    Vector mul(float f) {
        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    @Override
    Vector div(float f) {
        x *= 1/f;
        y *= 1/f;
        z *= 1/f;
        return this;
    }

    @Override
    Vector dot(Vector vec) {
        Vector3f v = (Vector3f) vec;
        x = x * v.x;
        y = y * v.y;
        z = z * v.z;
        return this;
    }

    @Override
    Vector cross(Vector vec) {
        return null;
    }

    @Override
    Vector unit() {
        return null;
    }

    @Override
    float distanceTo(Vector b) {
        return 0;
    }

    @Override
    float mag() {
        return 0;
    }

    @Override
    boolean equals() {
        return false;
    }

    @Override
    boolean isNull() {
        return false;
    }

    @Override
    Vector copy() {
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
}
