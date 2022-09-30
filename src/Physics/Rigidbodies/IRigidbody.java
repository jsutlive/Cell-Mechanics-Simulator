package Physics.Rigidbodies;

import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceVector2D;
import Utilities.Geometry.Vector.Vector2f;

public interface IRigidbody {
    void addForceVector(ForceVector2D forceVector);
    void addForceVector(Vector forceVector);

    void moveTo(Vector2f newPosition);
    void move();

    void addForceVector(String name, Vector vec);
}
