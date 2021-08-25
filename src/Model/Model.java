package Model;

import Physics.Forces.Force;
import Physics.Forces.Springs.ApicalSpring;
import Physics.PhysicsSystem;

import java.awt.*;

public class Model
{
    public static int TOTAL_CELLS = 0;
    PhysicsSystem physicsSystem = new PhysicsSystem();
    IOrganism organism = new DrosophilaEmbryo();

    Force apicalSprings = ApicalSpring.configureNew(new float[]{0.75f, 1.75f}, new float[]{0.01f, 0.01f});


    public void start()
    {
        DrosophilaEmbryo embryo = (DrosophilaEmbryo)organism;
        designOrganism();
        embryo.lateralConstrictingCells.setColor(Color.GREEN);
        embryo.apicalConstrictingCells.setColor(Color.BLUE);
//        physicsSystem.addForce(elasticMembrane, embryo.allCells);
//        physicsSystem.addForce(lateralSprings, embryo.lateralConstrictingCells);
        physicsSystem.addForce(apicalSprings, embryo.apicalConstrictingCells);

    }

    private void designOrganism()
    {
        organism.generateOrganism();
        TOTAL_CELLS = organism.getAllCells().getCells().size();
    }

    public void update()
    {
        physicsSystem.update();
        organism.getAllCells().update();
    }

    public IOrganism getOrganism()
    {
        return organism;
    }

}
