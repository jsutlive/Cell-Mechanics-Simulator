package Engine.States;

import Engine.Object.MonoBehavior;
import GUI.Painter;
import Model.Model;
import Physics.PhysicsSystem;
import Utilities.Geometry.Vector2i;

import java.awt.*;

public class RunState extends State
{
    Model model;
    MonoBehavior physicsSystem;

    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        physicsSystem = State.create(PhysicsSystem.class);
        model = (Model)State.create(Model.class);
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
        System.out.println(renderBatch.size());
        for(MonoBehavior mono: renderBatch)
        {
            mono.render();
        }
        Painter.drawPoint(new Vector2i(400,400));
        Painter.drawCircle(new Vector2i(400), 604, Color.gray);
        System.out.println("--------");
    }
}
