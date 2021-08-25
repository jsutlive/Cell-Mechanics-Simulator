package Physics.Bodies;

import Engine.Object.MonoBehavior;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static GUI.Painter.DEFAULT_COLOR;

public abstract class PhysicsBody extends MonoBehavior
{
    protected Map<Force, Vector2f> forceMap = new HashMap<Force, Vector2f>();
    protected Color color = DEFAULT_COLOR;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void addForce(Force force, Vector2f vector)
    {
        forceMap.put(force, vector);
    }

    protected void clearForce(Force force)
    {
        forceMap.remove(force);
    }

    public void update()
    {
        for(Object forceVector: forceMap.values())
        {
            MovePosition((Vector2f)forceVector);
        }

    }

    protected abstract void MovePosition(Vector2f deltaPosition);


}
