package Utilities.Geometry.Vector;

public abstract class Vector {

    public abstract Vector add(Vector vec);
    abstract Vector sub(Vector vec);
    abstract Vector mul(float f);
    abstract Vector div(float f);
    
    abstract Vector dot(Vector vec);
    abstract Vector cross(Vector vec);

    abstract Vector unit();
    abstract float distanceTo(Vector b);
    abstract float mag();

    abstract boolean equals();
    abstract boolean isNull();
    abstract Vector copy();

    abstract float[] get();
    public abstract float get(int index);
}
