package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Utilities.Debug;
import Framework.States.StateMachine;
import Framework.Utilities.Time;

import Input.InputEvents;
import Utilities.Math.CustomMath;

import static Framework.Data.FileBuilder.*;

/**
 * SaveSystem is a special component which allows for the saving of mesh data from physics objects in the GUI.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
@DoNotDestroyInGUI
public class SaveSystem extends Component {
    public String saveFolder = "";
    public float secondsPerSaveInterval = 5;
    public float stopTime = 15;

    private int count = 0;

    @Override
    public void start() {
        if (saveFolder.equals("")) {
            Debug.LogWarning("No save file specified, saving to root directory");
        }
        setFullPathName(saveFolder);
    }

    @Override
    public void update() {
        checkForSaveTimepoint();
        if (StateMachine.timer.elapsedTime >= Time.asNanoseconds(stopTime)) {
            InputEvents.toggleSimulation(false);
            count = 0;
        }
    }

    // Determine if it is time to save files,  if so, invoke the saveStateInfo event.
    private void checkForSaveTimepoint() {
        float currentTime = StateMachine.timer.elapsedTime;
        float targetTime = count * Time.asNanoseconds(secondsPerSaveInterval);
        if (currentTime >= targetTime) {
            float simpleTime = CustomMath.round(Time.fromNanoseconds((long) targetTime), 1);
            String saveName = String.valueOf(simpleTime);
            StateMachine.onSaveStateInfo.invoke(saveName);
            cacheMeshData(saveName);
            count++;
        }
    }
}
