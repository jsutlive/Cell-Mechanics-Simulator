package Framework.States;

import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.Timer.Time;
import Framework.Object.Component;

import java.util.ArrayList;
import java.util.List;

import static Framework.Data.File.save;
import static Framework.Data.File.load;
import static Input.InputPanel.onTimestepSliderChanged;

public abstract class State
{
    protected StateMachine stateMachine;
    private static int _ID_COUNTER = 0;

    public State(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    /**
     * Initializes entities when the state starts. Only called once.
     */
    public abstract void enter();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void tick();

    /**
     * Look for a tagged object and return the first object with that tag
     * @param tag specified tag to search all state entities for
     * @return the first entity found with the specified tag
     */
    public Entity findObjectWithTag(Tag tag)
    {
        for (Entity mono: stateMachine.allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    /**
     * Remove object and its associated render component from the scene
     * @param obj object to be destroyed
     */
    public static void destroy(Entity obj) {
    }

    /**
     * save a json file with initial position
     */
    protected void saveInitial(){
       save(stateMachine.allObjects);
    }

    /**
     * Actions performed by state prior to creation of new state
     * Culminates in setting of new state
     */
    abstract void exit();
}
