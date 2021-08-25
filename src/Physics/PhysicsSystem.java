package Physics;

import Engine.Object.MonoBehavior;
import Physics.Bodies.Cell.CellGroup;
import Physics.Forces.Force;

import java.util.HashSet;

public class PhysicsSystem extends MonoBehavior
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

    @Override
    public void start() {

    }

    public void update()
    {
        for(Force force: forces)
        {
            force.update();
        }
    }

    @Override
    public void destroy() {

    }



    public PhysicsSystem(){}
}
