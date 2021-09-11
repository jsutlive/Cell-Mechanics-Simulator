package Model;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.States.State;
import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellGroup;
import Physics.Bodies.Edge;
import Physics.Forces.Force;
import Physics.Forces.Springs.ApicalSpring;
import Physics.Forces.Springs.ElasticSpring;
import Physics.Forces.Springs.SimpleApicalSpring;
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

    Force apicalSprings = SimpleApicalSpring.configureNew(.15f, .5f);

    @Override
    public void start() throws InstantiationException, IllegalAccessException {
        physicsSystem = (PhysicsSystem) State.findObjectWithTag(Tag.PHYSICS);
        SimpleFourCell embryo = (SimpleFourCell) organism;
        designOrganism();
        //embryo.lateralConstrictingCells.setColor(Color.GREEN);
        embryo.allCells.setColor(Color.MAGENTA);
        //Collection<CellEdge<ApicalEdge>> apicalEdges =
          //      embryo.lateralConstrictingCells.findAllEdgesOfType(ApicalEdge.class);
        //embryo.getAllCells().getCell(79).setColor(Color.MAGENTA);
        physicsSystem.addForce(apicalSprings, embryo.allCells);
        //physicsSystem.addForce(apicalSprings, embryo.allCells.getCell(3));
    }

    private void designOrganism() throws InstantiationException, IllegalAccessException {
        organism.generateOrganism();
        CellGroup allCells = organism.getAllCells();
        List<Cell> allCellsList = allCells.getCells();
        int tot = allCellsList.size();
        TOTAL_CELLS = tot;
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
