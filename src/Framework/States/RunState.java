package Framework.States;

import Framework.Object.Entity;
import Framework.Object.Tag;
import Renderer.Graphics.IRender;
import Morphogenesis.Models.DrosophilaRingModel;

import java.util.ConcurrentModificationException;

public class RunState extends State
{
    boolean isChangingState = false;
    Entity model;
    int count = 0;
    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void Init() {
        model = findObjectWithTag(Tag.MODEL);
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
            save();
        }
        count++;
    }

    /**
     * Render loop. All rendered objects added to the render batch will be made visible in the viewport
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
