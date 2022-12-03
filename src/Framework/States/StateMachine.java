package Framework.States;

import Framework.Object.Entity;
import Framework.Object.ModelLoader;
import Framework.Timer.Time;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Yolk;

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

    public void clearStateMachine(boolean keepSameModel){
        if(keepSameModel){
            for(int i = allObjects.size()-1; i>= 0; i-- ){
                allObjects.get(i).destroy();
            }
        }
    }

    public void loadModel(String modelName){
        clearStateMachine(true);
        changeState(new EditorState(this));
        if(modelName.equals("Embryo")) ModelLoader.loadDrosophilaEmbryo();
        if(modelName.equals("Hexagons")) ModelLoader.loadHexMesh();
    }

    /**
     *  unsubscribe from events when application is exited.
     */
    public void deactivate(){
        Entity.onAddEntity.close();
        Entity.onRemoveEntity.close();

    }

}
