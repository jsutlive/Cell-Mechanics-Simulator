package Physics.Bodies;

import java.awt.*;
import java.util.Collection;

public abstract class Polygon extends PhysicsBody
{
    protected Collection<Edge> edges;
    protected Collection<Vertex> vertices;
    protected float area;




    @Override
    public void setColor(Color color)
    {

    }

}
