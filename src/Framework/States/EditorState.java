package Framework.States;

import Framework.Object.Entity;
import Input.InputEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Yolk;

import java.util.Objects;

import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    Entity model;

    public EditorState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void enter() {
        InputEvents.onPlay.subscribe(this::goToRunState);
        // Find an object to act as base physics/ setup for the simulation
        model = findObjectWithTag(MODEL);
        if(model == null){
            // create a new model with specific components
            model = new Entity("Physics System", 0, MODEL).
                    with(new RingMesh()).
                    with(new MouseSelector()).
                    with(new ApicalGradient()).
                    with(new LateralGradient()).
                    with(new CellRingCollider()).
                    with(new RigidBoundary()).
                    with(new Yolk()
                    );
        }
        else {
            //ensure mesh object is not null, then reset
            for(Entity obj: stateMachine.allObjects) {
                Objects.requireNonNull(obj.getComponent(Mesh.class)).reset();
            }
        }
        //loadModel();
    }

    @Override
    public void tick() {
    }

    private void goToRunState(Boolean b){
        if(b) stateMachine.changeState(new RunState(stateMachine));
    }


    @Override
    void exit() {
        InputEvents.onPlay.unSubscribe(this::goToRunState);
    }

}
