package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Model.Organisms.*;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector2f;

public class Model extends MonoBehavior
{
    //TODO: Add yolk conservation
    //TODO: Add osmosis force
    //TODO: Fix LJ-type forces
    PhysicsSystem physicsSystem;
    //IOrganism organism = new SimpleFourCellBox();
    IOrganism organism = new DrosophilaEmbryo();

    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        organism.generateOrganism();
    }


    @Override
    public void start() {
    }

    /**
     * Update all forces, at node level and cellular level.
     */
    @Override
    public void update()
    {
        Edge e;
        float maxRadius = 5f;
        float ljConstant = 5.6f;
        for(Node node: organism.getAllNodes())
        {
            for(Node t: organism.getAllNodes())
            {
                if(node!=t && Boundary.ContainsNode(t, node.getPosition(), maxRadius))
                {
                    e = new BasicEdge(node, t);
                    float forceMagnitude = Math.min(3f, ljConstant * (1f/ e.getLength()));
                    Vector2f forceVector = new Vector2f(e.getXUnit(), e.getYUnit());
                    forceVector.mul(-forceMagnitude);
                    node.AddForceVector(forceVector);
                }
            }
        }
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
