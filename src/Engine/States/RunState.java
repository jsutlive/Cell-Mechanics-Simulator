package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.Simulation;
import Engine.Timer.Time;
import GUI.IRender;
import GUI.Painter;
import Input.Input;
import Model.Components.CellRenderer;
import Model.*;
import Physics.PhysicsSystem;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;

public class RunState extends State
{
    Model model;

    /**
     * Instantiation of monobehaviors occurs here. Each behavior will have its awake and start methods called.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        model = (Model)findObjectWithTag(Tag.MODEL);
        if(model == null) {
            model = (Model) State.create(Model.class);
        }
        for(MonoBehavior obj: allObjects){
            obj.start();
        }
    }

    boolean flag = true;
    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void Tick() {
        if(flag && model != null) {
            model.update();

            //flag = false;

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
        try {
            for(IRender rend: renderBatch)
            {
                rend.render();
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
        for (Node n: Simulation.FORCE_HISTORY.keySet()) {
            Painter.drawForce(n, Simulation.FORCE_HISTORY.get(n));
        }
        Simulation.FORCE_HISTORY.clear();
        
        Painter.drawPoint(new Vector2i(400,400));
        Painter.drawCircle(new Vector2i(400), 604, Color.gray);
        Painter.drawCircle(new Vector2i(400), 605, Color.gray);
        Painter.drawCircle(new Vector2i(400), 606, Color.gray);
    }
}
