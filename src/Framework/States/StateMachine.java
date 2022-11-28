package Framework.States;

import Framework.Object.Entity;
import Framework.Timer.Time;
import Input.InputEvents;

import java.util.ArrayList;
import java.util.List;

public final class StateMachine {

    public State currentState;
    public List<Entity> allObjects = new ArrayList<>();
    public static Time timer;

    public StateMachine(Time referenceTime){
        timer = referenceTime;
        Entity.onAddEntity.subscribe(this::addEntityToList);
        Entity.onRemoveEntity.subscribe(this::removeEntityFromList);
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        changeState(new EditorState(this));
    }

    /**
     * Add entity to state and have it run its initial state
     * @param e entity added
     */
    private void addEntityToList(Entity e) {
        allObjects.add(e);
        e.awake();
    }

    /**
     * Remove entity to state
     * @param e entity removed
     */
    private void removeEntityFromList(Entity e){
        allObjects.remove(e);
    }

    /**
     * Call exit events in current state, move to and call enter events in new state
     * @param newState state to change to
     */
    public void changeState(State newState){
        if(currentState!=null)currentState.exit();
        currentState = newState;
        timer.reset();
        currentState.enter();
    }

    /**Manage GUI events for switching between editor and simulator
     *
     * @param isPlaying is true if need to go to RunState.
     */
    private void handleSimulationToggle(boolean isPlaying){
        if(isPlaying) changeState(new RunState(this));
        else changeState(new EditorState(this));
    }

    /**
     *  unsubscribe from events when application is exited.
     */
    public void deactivate(){
        Entity.onAddEntity.unSubscribe(this::addEntityToList);
        Entity.onRemoveEntity.unSubscribe(this::removeEntityFromList);
        InputEvents.onToggleSimulation.unSubscribe(this::handleSimulationToggle);
    }

}
