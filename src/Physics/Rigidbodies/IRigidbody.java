package Physics.Rigidbodies;

import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceVector2D;
import Utilities.Geometry.Vector.Vector2f;

public interface IRigidbody {
    void addForceVector(Vector forceVector);

    void moveTo(Vector newPosition);
    void move();

    void addForceVector(String name, Vector vec);
}
