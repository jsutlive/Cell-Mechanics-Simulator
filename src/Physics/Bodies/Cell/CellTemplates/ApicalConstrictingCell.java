package Physics.Bodies.Cell.CellTemplates;

import Engine.Object.Tag;
import Engine.States.State;
import Physics.Bodies.Cell.Cell;
import Physics.Forces.Force;
import Physics.Forces.Springs.ApicalSpring;
import Physics.PhysicsSystem;

public class ApicalConstrictingCell extends Cell
{
    Force constrictionForce;
    @Override
    public void awake()
    {
        PhysicsSystem physics = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        constrictionForce = physics.getComponent(ApicalSpring.class);
        if (constrictionForce != null)
        {
            attachForce(constrictionForce);
        }
    }
}
