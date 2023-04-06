package Component;

import Framework.Data.FileBuilder;
import Framework.Utilities.Debug;
import Framework.States.StateMachine;
import Framework.Utilities.Time;

import Input.InputEvents;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;

public class SaveSystem extends Component {
    public StateMachine stateMachine;
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;
    @Override

    public void awake() {
        StateMachine.onSaveStateInfo.subscribe(this::saveData);
    }

    @Override
    public void start() {
        if(saveFolder.equals("")){
            Debug.LogWarning("No save file specified, saving to root directory");
        }
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }

    @Override
    public void update() {
        if(StateMachine.timer.elapsedTime > Time.asNanoseconds(stopTime)){
            InputEvents.toggleSimulation(false);
        }
    }


    private void saveData(String title){
        FileBuilder.cacheMeshData(String.valueOf(title));
    }

    @Override
    public void onDestroy(){
        StateMachine.onSaveStateInfo.unSubscribe(this::saveData);
    }
}
