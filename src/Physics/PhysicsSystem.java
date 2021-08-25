package Physics;

import Physics.Bodies.Cell.CellGroup;
import Physics.Forces.Force;

import java.util.HashSet;

public class PhysicsSystem
{
    private HashSet<Force> forces = new HashSet<>();

    public HashSet<Force> getForces()
    {
        return forces;
    }

    public void addForce(Force force)
    {
        forces.add(force);
    }

    public void addForce(Force force, CellGroup group)
    {
        forces.add(force);
        force.attach(group);
        force.start();
    }

    public void removeForce(Force force)
    {
        forces.remove(force);
    }

    public void update()
    {
        for(Force force: forces)
        {
            force.update();
        }
    }
}
