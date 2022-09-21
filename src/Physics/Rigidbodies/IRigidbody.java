package Physics.Rigidbodies;

import Model.Components.Physics.ForceVector.ForceVector;
import Utilities.Geometry.Vector2f;

public interface IRigidbody {
    void addForceVector(ForceVector forceVector);
    void addForceVector(Vector2f forceVector);

    public void MoveTo(Vector2f newPosition);
    public void Move();
}
