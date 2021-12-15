package Physics;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Physics.Forces.Force;

import java.util.ArrayList;

public class PhysicsSystem extends MonoBehavior
{
    private ArrayList<Force> forces = new ArrayList<>();

    public ArrayList<Force> getForces()
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
    public void run()
    {}
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
