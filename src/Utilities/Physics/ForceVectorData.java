package Utilities.Physics;

import Utilities.Geometry.Vector.Vector;

public class ForceVectorData {
    public Vector vector;
    public String type;

    public void set(Vector _vector, String _type){
        vector = _vector;
        type = _type;
    }
}
