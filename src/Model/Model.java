package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Physics.PhysicsSystem;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Model extends MonoBehavior
{
    public static int TOTAL_CELLS = 4;
    PhysicsSystem physicsSystem;
    IOrganism organism = new SimpleFourCell();

    @Override
    public void start() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        SimpleFourCell embryo = (SimpleFourCell) organism;
    }

    @Override
    public void update()
    {
        organism.getAllCells().update();


    }

    @Override
    public void destroy() {

    }

    public IOrganism getOrganism()
    {
        return organism;
    }

    public static Model createObject() {
        return new Model();
    }

    public Model(){}
}
