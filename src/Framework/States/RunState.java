package Framework.States;

import Framework.Object.Entity;

import java.util.Collections;

public class RunState extends State
{
    protected static float dt  = 1e-3f;
    public static float deltaTime;

    public RunState(StateMachine stateMachine) {
        super(stateMachine);
    }

    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void enter() {
        Collections.shuffle(stateMachine.allObjects);
//        onTimestepSliderChanged.subscribe(this::setTimeStep);
//        InputEvents.onStop.subscribe(this::goToEditorState);
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
    }

    void exit()
    {
//        onTimestepSliderChanged.unSubscribe(this::setTimeStep);
//        InputEvents.onStop.unSubscribe(this::goToEditorState);
    }
}
