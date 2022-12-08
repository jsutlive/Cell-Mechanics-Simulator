package Framework.Rigidbodies;

import Utilities.Geometry.Vector.Vector;

public interface IRigidbody {
    void addForceVector(Vector forceVector);

    void moveTo(Vector newPosition);
    void move();

    void addForceVector(String name, Vector vec);
}
