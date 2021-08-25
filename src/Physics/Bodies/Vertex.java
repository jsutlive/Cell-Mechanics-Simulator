package Physics.Bodies;

import Utilities.Geometry.Vector2f;

public abstract class Vertex extends PhysicsBody {
    protected Vector2f position;

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setPosition(float x, float y){this.position = new Vector2f(x, y);}

}
