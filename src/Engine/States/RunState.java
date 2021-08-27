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
        physicsSystem = State.create(PhysicsSystem.class);
        model = (Model)State.create(Model.class);
        for (MonoBehavior obj: allObjects) {
            obj.start();
        }
    }

    @Override
    public void Tick()
    {
        for (MonoBehavior obj: allObjects) {
            obj.update();
        }
    }

    @Override
    public void Render()
    {
        for(MonoBehavior mono: renderBatch)
        {
            mono.render();
        }
        Painter.drawPoint(new Vector2i(400,400));
        System.out.println("--------");
    }
}
