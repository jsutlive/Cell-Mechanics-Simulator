package Framework.States;

import Framework.Object.Entity;
import Framework.Timer.Time;

import java.util.ArrayList;
import java.util.List;

import static Framework.Data.File.load;

public class StateMachine {
    public State currentState;

    public List<Entity> allObjects = new ArrayList<>();

    public StateMachine(){
        Entity.onAddEntity.subscribe(this::addEntityToList);
        Entity.onRemoveEntity.subscribe(this::removeEntityFromList);
        changeState(new EditorState(this));
    }

    /**
     * Add entity to state and invoke the on add entity method for GUI
     * @param e entity added
     */
    private void addEntityToList(Entity e) {
        allObjects.add(e);
        e.awake();
    }

    private void removeEntityFromList(Entity e){
        allObjects.remove(e);
    }

    public void changeState(State newState){
        if(currentState!=null)currentState.exit();
        currentState = newState;
        Time.reset();
        currentState.enter();
    }

    protected void loadModel()
    {
        load();
    }

}
