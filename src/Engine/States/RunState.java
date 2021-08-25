package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import GUI.Painter;
import Model.Model;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellGroup;
import Physics.PhysicsSystem;
import Utilities.Geometry.Vector2i;

public class RunState extends State
{
    Model model;
    MonoBehavior physicsSystem;
    CellGroup renderedCells;

    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        model = (Model)State.create(Model.class);
        physicsSystem = State.create(PhysicsSystem.class);
        physicsSystem.addTag(Tag.PHYSICS);

        model.start();
        renderedCells = model.getOrganism().getAllCells();
    }

    @Override
    public void Tick() {
        model.update();
    }

    @Override
    public void Render()
    {
        for(Cell cell: renderedCells.getCells())
        {
            Painter.drawCell(cell);
        }
        Painter.drawPoint(new Vector2i(400,400));
        System.out.println("--------");
    }
}
