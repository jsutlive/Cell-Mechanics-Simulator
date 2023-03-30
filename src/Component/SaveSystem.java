package Component;

import Framework.Data.FileBuilder;
import Framework.Object.Entity;
import Framework.States.StateMachine;
import Framework.Timer.Time;

import Annotations.ReloadComponentOnChange;

import java.util.List;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;
import static Input.InputEvents.toggleSimulation;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;
    public boolean hasStopped;

    @Override
    public void awake() {
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }

    @Override
    public void update() {
        if(hasStopped) return;
        if(StateMachine.timer.elapsedTime > Time.asNanoseconds(stopTime)){
            toggleSimulation(false);
        }
    }

    private void saveData(String title){

        FileBuilder.saveMeshData(String.valueOf(title));
    }

    void clearEvents(){
        StateMachine.onSaveStateInfo.unSubscribe(this::saveData);
    }
}
