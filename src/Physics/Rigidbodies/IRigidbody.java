package Physics.Rigidbodies;

import Utilities.Geometry.Vector2f;

public interface IRigidbody {
    public void AddForceVector(Vector2f forceVector);
    public void MoveTo(Vector2f newPosition);
    public void Move();
}
