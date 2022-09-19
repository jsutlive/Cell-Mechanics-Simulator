package Engine.States;

import Engine.Object.Entity;
import Engine.Object.Tag;
import GUI.IRender;
import Model.*;

import java.util.ConcurrentModificationException;

public class RunState extends State
{
    boolean isChangingState = false;
    Entity model;
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
        for(Entity obj: allObjects){
            obj.start();
        }
        //saveInitial();
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void Tick() {
        for(Entity obj : allObjects){
            if(isChangingState) return;
            obj.earlyUpdate();
        }
        for(Entity obj : allObjects){
            if(isChangingState) return;
            obj.update();
        }
        for(Entity obj : allObjects){
            if(isChangingState) return;
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
                if(isChangingState) return;
                rend.render();
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    void OnChangeState()
    {
        isChangingState = true;
    }
}
