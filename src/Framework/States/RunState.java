package Framework.States;

import Framework.Data.FileBuilder;
import Framework.Object.Entity;
import Framework.Utilities.Time;
import Utilities.Math.CustomMath;

import java.util.Collections;

import static Renderer.UIElements.Panels.PlayPanel.onTimestepSliderChanged;

public class RunState extends State
{
    protected static float dt  = (float) Math.pow(2,-14);
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
        for (Entity obj :(stateMachine.allObjects)) {
            obj.start();
        }
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
        saveDataToCSV();
    }

    private void saveDataToCSV() {
        float currentTime = StateMachine.timer.elapsedTime;
        float targetTime = count * FileBuilder.saveFrequency;
        if (currentTime > targetTime){
            float simpleTime = CustomMath.round(Time.fromNanoseconds((long)targetTime), 1);
            StateMachine.onSaveStateInfo.invoke(String.valueOf(simpleTime));
            count++;
        }
    }

    void exit()
    {
        onTimestepSliderChanged.unSubscribe(this::setTimeStep);
    }
}
