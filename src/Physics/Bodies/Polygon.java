package Physics.Bodies;

import java.awt.*;
import java.util.Collection;

public abstract class Polygon extends PhysicsBody
{
    private Collection<Edge> edges;
    private Collection<Edge> vertices;
    private float area;




    @Override
    public void setColor(Color color)
    {

    }

}
