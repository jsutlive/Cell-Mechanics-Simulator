package Physics.Bodies;

import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

public abstract class Edge extends PhysicsBody
{
    protected Vertex[] vertices = new Vertex[2];



    public Vector2f[] getPositions()
    {
        Vector2f[] positions = new Vector2f[2];
        positions[0] =  vertices[0].getPosition();
        positions[1] = vertices[1].getPosition();
        return positions;
    }

    public Vertex[] getVertices()
    {
        return vertices;
    }

    public float getLength()
    {
        Vector2f[] positions = getPositions();
        float deltaX = getXLength(positions);
        float deltaY = getYLength(positions);
        float val = (float)Math.hypot(deltaX, deltaY);
        return CustomMath.round(val, 3);
    }

    public float getXLength(Vector2f[] positions)
    {
        return positions[0].x - positions[1].x;
    }

    public float getXLength()
    {
        Vector2f[] positions = getPositions();
        float val =  positions[0].x - positions[1].x;
        return CustomMath.round(val, 3);
    }

    public float getYLength(Vector2f[] positions)
    {
        return positions[0].y - positions[1].y;
    }

    public float getYLength()
    {
        Vector2f[] positions = getPositions();
        float val = positions[0].y - positions[1].y;
        return CustomMath.round(val, 3);
    }

}
