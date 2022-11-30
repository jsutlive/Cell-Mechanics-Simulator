package Framework.States;

import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.HexMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.MeshCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Yolk;

import java.util.Objects;

import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    public EditorState(StateMachine stateMachine) {
        super(stateMachine);
    }

    boolean hexMesh = false;
    @Override
    public void enter() {
//        InputEvents.onPlay.subscribe(this::goToRunState);
        // Find an object to act as base physics/ setup for the simulation
        if(findObjectWithTag(MODEL) != null){
            //ensure mesh object is not null, then reset
            for(Entity obj: stateMachine.allObjects) {
                Objects.requireNonNull(obj.getComponent(Mesh.class)).reset();
            }
        }
        System.err.println(stateMachine.allObjects.size());
        //loadModel();
    }

    @Override
    public void tick() {
    }

    @Override
    void exit() {
        saveInitial();
//        InputEvents.onPlay.unSubscribe(this::goToRunState);
    }

}
