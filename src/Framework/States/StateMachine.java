package Framework.States;

import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.ModelLoader;
import Framework.Object.Tag;
import Framework.Timer.Time;
import Input.InputEvents;
import Input.SelectionEvents;
import Renderer.Camera;

import java.util.ArrayList;
import java.util.List;

public final class StateMachine {

    public State currentState;
    public List<Entity> allObjects = new ArrayList<>();
    public static Time timer;
    public static EventHandler<String> onSaveStateInfo = new EventHandler<>();

    public StateMachine(Time referenceTime){
        timer = referenceTime;
        Entity.onAddEntity.subscribe(this::addEntityToList);
        Entity.onRemoveEntity.subscribe(this::removeEntityFromList);
        SelectionEvents.onTagSelected.subscribe(this::selectEntityWithTag);
        InputEvents.onToggleSimulation.subscribe(this::handleSimulationToggle);
        InputEvents.onClear.subscribe(this::clearStateMachine);
        InputEvents.onLoadModel.subscribe(this::loadModel);
        new Entity("Camera", -1, Tag.CAMERA).with(new Camera());
        changeState(new EditorState(this));
    }

    /**
     * Add entity to state and have it run its initial state
     * @param e entity added
     */
    private void addEntityToList(Entity e) {
        if(allObjects.contains(e))return;
        allObjects.add(e);
        e.awake();
    }

    private void selectEntityWithTag(Tag t){
        Entity e = currentState.findObjectWithTag(t);
        if(e!=null) SelectionEvents.selectEntity(e);
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

    /**
     * Manage GUI events for switching between editor and simulator     *
     * @param isPlaying is true if need to go to RunState.
     */
    private void handleSimulationToggle(boolean isPlaying){
        if(isPlaying) changeState(new RunState(this));
        else changeState(new EditorState(this));
    }

    public void clearStateMachine(boolean keepCamera){
            for(int i = allObjects.size()-1; i>= 0; i-- ){
                if(keepCamera && allObjects.get(i).getTag() == Tag.CAMERA) continue;
                allObjects.get(i).destroy();
            }

    }

    public void loadModel(String modelName){
        clearStateMachine(true);
        changeState(new EditorState(this));
        if(modelName.equals("Embryo")) ModelLoader.loadDrosophilaEmbryo();
        if(modelName.equals("Hexagons")) ModelLoader.loadHexMesh();
        if(modelName.equals("Debug")) ModelLoader.loadDebugMesh();
    }

    /**
     *  unsubscribe from events when application is exited.
     */
    public void deactivate(){
        Entity.onAddEntity.close();
        Entity.onRemoveEntity.close();
        InputEvents.onToggleSimulation.close();
        InputEvents.onClear.close();
        SelectionEvents.onTagSelected.close();
    }

}
