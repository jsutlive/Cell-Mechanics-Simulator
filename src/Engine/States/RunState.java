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
    MonoBehavior model;
    int count = 0;
    /**
     * Instantiation of monobehaviors occurs here. Each behavior will have its awake and start methods called.
     * @throws InstantiationException fails to create object
     * @throws IllegalAccessException improperly accesses memory during object creation
     */
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        model = findObjectWithTag(Tag.MODEL);
        if(model == null) {
            model = State.create(Model.class);
        }
        for(MonoBehavior obj: allObjects){
            obj.start();
        }
        //saveInitial();
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void Tick() {
        for(MonoBehavior obj : allObjects){
            obj.earlyUpdate();
        }
        for(MonoBehavior obj : allObjects){
            obj.update();
        }
        for(MonoBehavior obj : allObjects){
            obj.lateUpdate();
        }

        if(count%100 == 0) {
            //save();
        }
        count++;
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
    }
}
