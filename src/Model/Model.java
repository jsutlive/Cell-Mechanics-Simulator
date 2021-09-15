package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Physics.PhysicsSystem;

public class Model extends MonoBehavior
{
    public static int TOTAL_CELLS = 4;
    PhysicsSystem physicsSystem;
    IOrganism organism = new SimpleFourCell();

    @Override
    public void start() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        SimpleFourCell embryo = (SimpleFourCell) organism;
        embryo.generateOrganism();
    }

    @Override
    public void update()
    {
        for(Cell cell: organism.getAllCells()) cell.update();
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public Model() throws InstantiationException, IllegalAccessException {
        this.start();
    }
}
