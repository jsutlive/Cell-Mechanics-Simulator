package Framework.States;

import Framework.Data.File;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Input.SelectionEvents;

import java.util.Collections;

import static Renderer.UIElements.Panels.PlayPanel.onTimestepSliderChanged;

public class RunState extends State
{
    protected static float dt  = 1e-4f;
    public static float deltaTime;
    private int count;

    public RunState(StateMachine stateMachine) {
        super(stateMachine);
    }

    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void enter() {
        Collections.shuffle(stateMachine.allObjects);
        onTimestepSliderChanged.subscribe(this::setTimeStep);
    }

    private void setTimeStep(float f){
        dt = f;
    }

    /**
     * Physics Loop. All physics objects updated here
     */
    @Override
    public void tick() {
        deltaTime = dt;

        for (Entity obj :(stateMachine.allObjects)) {
            obj.earlyUpdate();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.update();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.lateUpdate();
        }
        /*if (count %3000 ==0){
            StateMachine.onSaveStateInfo.invoke("noLat");
            File.save(findObjectWithTag(Tag.MODEL), String.valueOf(count));
        }*/
        count++;
    }

    void exit()
    {
        onTimestepSliderChanged.unSubscribe(this::setTimeStep);
    }
}
