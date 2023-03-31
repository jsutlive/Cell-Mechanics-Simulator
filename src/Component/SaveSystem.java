package Component;

import Framework.Data.FileBuilder;
import Framework.Debug.Debug;
import Framework.States.StateMachine;
import Framework.Timer.Time;

import Annotations.ReloadComponentOnChange;
import Input.InputEvents;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public StateMachine stateMachine;
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;
    @Override

    public void awake() {
        clearEvents();
        StateMachine.onSaveStateInfo.subscribe(this::saveData);
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }

    @Override
    public void start() {
        if(saveFolder.equals("")){
            Debug.LogWarning("No save file specified, saving to root directory");
        }
    }

    @Override
    public void update() {
        if(StateMachine.timer.elapsedTime > Time.asNanoseconds(stopTime)){
            InputEvents.toggleSimulation(false);
        }
    }


    private void saveData(String title){
        FileBuilder.saveMeshData(String.valueOf(title));
    }

    void clearEvents(){
        StateMachine.onSaveStateInfo.unSubscribe(this::saveData);
    }
}
