package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Edge;
import Physics.Forces.Force;
import Physics.Forces.Springs.ApicalSpring;
import Physics.PhysicsSystem;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

public class Model extends MonoBehavior
{
    public static int TOTAL_CELLS = 0;
    PhysicsSystem physicsSystem;
    IOrganism organism = new DrosophilaEmbryo();

    Force apicalSprings = ApicalSpring.configureNew(new float[]{0.75f, 1.75f}, new float[]{0.01f, 0.01f});

    @Override
    public void start() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        DrosophilaEmbryo embryo = (DrosophilaEmbryo)organism;
        designOrganism();
        embryo.lateralConstrictingCells.setColor(Color.GREEN);
        embryo.apicalConstrictingCells.setColor(Color.MAGENTA);
        //Collection<CellEdge<ApicalEdge>> apicalEdges =
          //      embryo.lateralConstrictingCells.findAllEdgesOfType(ApicalEdge.class);
        embryo.getAllCells().getCell(79).setColor(Color.MAGENTA);
        physicsSystem.addForce(apicalSprings, embryo.apicalConstrictingCells);
    }

    private void designOrganism() throws InstantiationException, IllegalAccessException {
        organism.generateOrganism();
        TOTAL_CELLS = organism.getAllCells().getCells().size();
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
