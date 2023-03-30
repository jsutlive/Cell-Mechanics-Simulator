package Component;

import Framework.Data.FileBuilder;
import Framework.States.StateMachine;
import Framework.Timer.Time;

import Annotations.ReloadComponentOnChange;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;
import static Input.InputEvents.toggleSimulation;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public StateMachine stateMachine;
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;
    boolean hasStopped;

    @Override
    public void awake() {
        clearEvents();
        StateMachine.onSaveStateInfo.subscribe(this::saveData);
        StateMachine.onStateMachineStateChange.subscribe(this::toggleHasStopped);
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }

    @Override
    public void update() {
        if(hasStopped) return;
        if(StateMachine.timer.elapsedTime > Time.asNanoseconds(stopTime)){

        }
    }

    private void toggleHasStopped(boolean playing){
        hasStopped = !playing;
    }

    private void saveData(String title){
        FileBuilder.saveMeshData(String.valueOf(title));
    }

    void clearEvents(){
        StateMachine.onSaveStateInfo.unSubscribe(this::saveData);
        StateMachine.onStateMachineStateChange.unSubscribe(this::toggleHasStopped);

    }
}
