package Framework.States;

import Framework.Object.Entity;
import Framework.Object.Tag;

import static Framework.Data.File.load;
import static Framework.Data.File.save;


public abstract class State
{
    protected StateMachine stateMachine;
    private static int _ID_COUNTER = 0;

    public State(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    /**
     * Actions ot be performed when the state starts up
     */
    public abstract void enter();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void tick();

    /**
     * Actions to be performed just before state is changed from this to another state
     */
    abstract void exit();


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
     * save a json file with initial position
     */
    protected void saveInitial(){
       save(findObjectWithTag(Tag.MODEL));
    }

    /**
     * load objects from json file
     */
    protected void loadModel(){
        load();
    }
}
