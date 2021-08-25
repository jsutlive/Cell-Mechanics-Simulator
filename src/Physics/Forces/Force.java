package Physics.Forces;

import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellGroup;
import Physics.Bodies.PhysicsBody;

import java.util.Collection;

public abstract class Force
{
    protected Collection<PhysicsBody> listeners;

    /**
     * start is called once when the force is added to the physics system
     */
    public abstract void start();

    public void update()
    {
        for(PhysicsBody body: listeners)
        {
            calculateForce(body);
        }
    }

    public void attach(CellGroup group)
    {
        listeners.addAll(group.getCells());
    }

    public void attach(Cell cell)
    {
        listeners.add(cell);
    }

    public abstract void calculateForce(PhysicsBody body);
}
