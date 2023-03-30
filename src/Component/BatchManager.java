package Component;

import Framework.Debug.Debug;
import Framework.Object.Entity;
import Framework.States.EditorState;
import Framework.States.RunState;
import Framework.States.StateMachine;
import Annotations.ReloadComponentOnChange;
import Annotations.DoNotEditInGUI;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@ReloadComponentOnChange
public class BatchManager extends Component {

    public boolean useBatchTesting;
    public StateMachine stateMachine;

    public File batchFile;

    @DoNotEditInGUI
    public int currentBatch;
    @DoNotEditInGUI
    public int longestBatch;

    private List<String[]> args = new ArrayList<>();

    @Override
    public void awake() {
        if(batchFile == null) return;
        try {
            Scanner scanner = new Scanner(batchFile);

            while (scanner.hasNextLine()) {
                args.add(parseCommands(scanner.nextLine()));
            }

            scanner.close();
            Debug.Log("Loaded batch file to system");
        } catch (FileNotFoundException e) {
            // Alert user if file can't be used
            Debug.LogError("File invalid");
        }
        longestBatch = 0;
        for(String[] s : args){
            longestBatch = Math.max(longestBatch, s.length - 2);
        }
    }

    public String[] parseCommands(String commandLine){
        return commandLine.split(",");
    }

    public void alterParameters(){
        for(String[] s : args){
            if(s.length < currentBatch + 1) continue;
            try {
                Class c = Class.forName("Component." + s[0]);
                for(Entity e: stateMachine.allObjects){
                    Component component = e.getComponent(c);
                    if(component!=null){
                        Field f = c.getField(s[1]);
                        if(f!=null){
                            Type type = f.getType();
                            if(type == int.class){
                                component.changeFieldOnGUI(f.getName(), Integer.parseInt(s[currentBatch + 2]));
                            }else if(type == float.class){
                                component.changeFieldOnGUI(f.getName(), Float.parseFloat(s[currentBatch + 2]));
                            }else if(type == String.class){
                                component.changeFieldOnGUI(f.getName(), s[currentBatch + 2]);
                            }else if(type == boolean.class){
                                component.changeFieldOnGUI(f.getName(), Boolean.parseBoolean(s[currentBatch + 2]));
                            }
                        }
                    }
                }

            }
            // Don't crash the program, but log to user if there is a problem.
            catch(ClassNotFoundException e){
                Debug.LogError("Component not found in library, skipping parameter");
                continue;
            } catch (NoSuchFieldException e){
                Debug.LogError("Field not found in component parameters, skipping parameter");
                continue;
            }
        }
    }

    public void runNextBatch(boolean isRunning) {
        //if no batch file, throw error and return
        if(batchFile == null){
            Debug.LogError("No batch file found, simulation cancelled");
            return;
        }
        // If out of batches, stop the simulation, return to editor state
         if(currentBatch >= longestBatch) {
            stateMachine.changeState(new EditorState(stateMachine));
            StateMachine.onStateMachineStateChange.invoke(false);
            currentBatch = 0;
             getComponent(SaveSystem.class).hasStopped = true;
             Debug.Log("Batch process complete");
             return;
        }
        // Run simulation on two cases: if the simulation has been toggled to stop, or if the simulation was triggered
        // to start when the batch number was 0
        else if(currentBatch == 0){
            alterParameters();
            stateMachine.changeState(new RunState(stateMachine));
             StateMachine.onStateMachineStateChange.invoke(true);
             currentBatch++;
             getComponent(SaveSystem.class).hasStopped = false;
             Debug.Log("Running batch " + currentBatch + "/" + longestBatch);
         }else if (!isRunning){
             alterParameters();
             stateMachine.changeState(new RunState(stateMachine));
             StateMachine.onStateMachineStateChange.invoke(true);
             currentBatch++;
             getComponent(SaveSystem.class).hasStopped = false;
             Debug.Log("Running batch " + currentBatch + "/" + longestBatch);
         }
        else{
            getComponent(SaveSystem.class).hasStopped = true;
         }

    }
}
