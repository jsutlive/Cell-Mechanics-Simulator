package Framework.States;

import Framework.Object.Entity;
import Renderer.Graphics.IRender;

import java.util.Collections;

public class RunState extends State
{
    int count = 0;
    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void Init() {
        Collections.shuffle(allObjects);
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void Tick() {
        for (Entity obj : allObjects) {
            obj.earlyUpdate();
        }
        for (Entity obj : allObjects) {
            obj.update();
        }
        for (Entity obj : allObjects) {
            obj.lateUpdate();
        }
        /*if(count%500 == 0) {

            ZoomRenderer instance = (ZoomRenderer) Renderer.getInstance();
            instance.GetDisplayWindow().exportImage();
        }*/
        count++;
    }

    /**
     * Render loop. All rendered objects added to the render batch will be made visible in the viewport
     * through referencing its renderer component.
     */
    @Override
    public void Render()
    {
        for(IRender rend: renderBatch)
        {
            rend.render();
        }
    }

    void OnChangeState()
    {
        SetState(new EditorState());
    }
}
