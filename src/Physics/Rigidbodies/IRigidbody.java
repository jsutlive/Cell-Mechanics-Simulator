package Physics.Rigidbodies;

import Utilities.Physics.ForceVector2D;
import Utilities.Geometry.Vector.Vector2f;

public interface IRigidbody {
    void addForceVector(ForceVector2D forceVector);
    void addForceVector(Vector2f forceVector);

    public void MoveTo(Vector2f newPosition);
    public void Move();
}
