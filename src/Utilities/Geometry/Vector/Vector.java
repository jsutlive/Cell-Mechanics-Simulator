package Utilities.Geometry.Vector;

public abstract class Vector {

    public abstract Vector add(Vector vec);
    abstract Vector sub(Vector vec);
    abstract Vector mul(float f);
    abstract Vector div(float f);
    
    abstract Vector dot(Vector vec);
    abstract Vector cross(Vector vec);

    abstract Vector unit();
    public abstract float distanceTo(Vector b);
    abstract float mag();

    public abstract boolean equals(Vector vec);
    public abstract boolean isNull();
    public abstract Vector copy();
    public abstract Vector get();
    public abstract float get(int index);

    /**
     * Makes a copy of a vector and multiplies all elements by -1
     * @return "negative" of vector, i.e. returned vector + original  = a zero vector
     */
    public Vector neg(){
        Vector v = copy();
        v.mul(-1);
        return v;
    }


}
