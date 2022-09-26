package Physics.Rigidbodies;

import Utilities.Physics.ForceVector;
import Utilities.Geometry.Vector.Vector2f;

public interface IRigidbody {
    void addForceVector(ForceVector forceVector);
    void addForceVector(Vector2f forceVector);

    public void MoveTo(Vector2f newPosition);
    public void Move();
}
