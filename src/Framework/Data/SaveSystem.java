package Framework.Data;

import Framework.Object.Component;
import Framework.States.StateMachine;
import Framework.Timer.Time;
import Input.InputEvents;
import Morphogenesis.ReloadComponentOnChange;

import static Framework.Data.FileBuilder.setFullPathName;
import static Framework.Data.FileBuilder.setSaveFrequency;

@ReloadComponentOnChange
public class SaveSystem extends Component {
    public String saveFolder = "";
    public float secondsPerSaveInterval = 20;
    public float stopTime = 15;
    @Override
    public void awake() {
        setFullPathName(saveFolder);
        setSaveFrequency(secondsPerSaveInterval);
    }

    @Override
    public void update() {
        if(StateMachine.timer.elapsedTime < Time.asNanoseconds(stopTime * 60)){
            InputEvents.toggleSimulation(false);
        }
    }
}
