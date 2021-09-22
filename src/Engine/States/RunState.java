package Engine.States;

import Engine.Object.MonoBehavior;
import GUI.Painter;
import Model.Components.CellRenderer;
import Model.Model;
import Physics.PhysicsSystem;
import Utilities.Geometry.Vector2i;

import javax.swing.*;
import java.awt.*;

public class RunState extends State
{
    int count = 1;
    Model model;
    MonoBehavior physicsSystem;

    /**
     * Instantiation of monobehaviors occurs here. Each behavior will have its awake and start methods called.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        physicsSystem = State.create(PhysicsSystem.class);
        model = (Model)State.create(Model.class);
        for(MonoBehavior obj: allObjects){
            obj.start();
        }
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void Tick()
    {
        for (MonoBehavior obj: allObjects) {
            obj.update();
        }
    }

    /**
     * Render loop. Every monobehavior added to the render batch will be made visible in the viewport
     * through referencing its renderer component. Throws a null reference exception if there is no
     * renderer on an object.
     */
    @Override
    public void Render()
    {
        System.out.println("FRAME " + count + ":");
        count++;
        for(CellRenderer rend: renderBatch)
        {
            rend.render();
        }
        Painter.drawPoint(new Vector2i(400,400));
        Painter.drawCircle(new Vector2i(400), 604, Color.gray);
        System.out.println("--------");
    }
}
