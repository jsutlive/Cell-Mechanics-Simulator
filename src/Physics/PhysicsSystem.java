package Physics;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Model.Cell;

import java.util.HashSet;
import java.util.List;

public class PhysicsSystem extends MonoBehavior
{
    private HashSet<Force> forces = new HashSet<>();

    public HashSet<Force> getForces()
    {
        return forces;
    }

    @Override
    public void awake() {
        this.addTag(Tag.PHYSICS);
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy()
    {

    }

    /*public <T extends Force> Force getComponent(Class<T> forceClass)
    {
        for (Force force:forces) {
            if(forceClass.isAssignableFrom(force.getClass())) return force;
        }
        return null;
    }*/


    public PhysicsSystem(){}
}
