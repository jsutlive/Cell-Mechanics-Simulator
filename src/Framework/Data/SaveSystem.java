package Framework.Data;

import Framework.Object.Component;
import Framework.States.StateMachine;
import Framework.Timer.Time;

import Input.InputEvents;
import Morphogenesis.ReloadComponentOnChange;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;
import static Input.InputEvents.toggleSimulation;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;
    boolean hasStopped;
    @Override
    public void awake() {
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
        InputEvents.onToggleSimulation.subscribe(this::toggleStopped);
    }

    @Override
    public void update() {
        if(hasStopped) return;
        if(StateMachine.timer.elapsedTime > Time.asNanoseconds(stopTime)){
            toggleSimulation(false);
        }
    }

    public void toggleStopped(boolean hasStopped){
        this.hasStopped = !hasStopped;
    }

    @Override
    public void onDestroy() {
        InputEvents.onToggleSimulation.unSubscribe(this::toggleStopped);
    }
}
