package Physics.Rigidbodies;

import GUI.IColor;
import Utilities.Geometry.Vector2f;

import java.awt.*;

public class Node implements IRigidbody, IColor {

    private Vector2f position;
    private Vector2f resultantForce;
    private Color color;

    public Vector2f getPosition()
    {
        return position;
    }

    public Vector2f getResultantForce()
    {
        return resultantForce;
    }

    public Node()
    {
        position = new Vector2f(0);
    }
    public Node(Vector2f pos)
    {
        position = pos;
    }

    @Override
    public void AddForceVector(Vector2f forceVector) {

    }

    @Override
    public void MoveTo(Vector2f newPosition) {
        position = newPosition;
    }

    @Override
    public void Move() {

    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {

    }
}
