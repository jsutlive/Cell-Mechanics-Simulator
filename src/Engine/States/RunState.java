package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import Engine.Simulation;
import GUI.IRender;
import GUI.Painter;
import Model.*;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2i;

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
        for(MonoBehavior obj : allObjects){
            obj.earlyUpdate();
            obj.update();
            obj.lateUpdate();
        }
        save();
    }

    /**
     * Render loop. All renderable objects added to the render batch will be made visible in the viewport
     * through referencing its renderer component.
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
        /*for (Node n: Simulation.FORCE_HISTORY.keySet()) {
            Painter.drawForce(n, Simulation.FORCE_HISTORY.get(n));
        }*/
        Simulation.FORCE_HISTORY.clear();
    }
}
