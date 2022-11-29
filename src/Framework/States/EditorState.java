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
        // Find an object to act as base physics/ setup for the simulation
        if(findObjectWithTag(MODEL) == null){
            Entity physics;
            // create a new model with specific components
            if(!hexMesh) {
                 physics = new Entity("Physics System", 0, MODEL).
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
                 physics = new Entity("Physics System", 0, MODEL).
                        with(new HexMesh()).
                        with(new MouseSelector()).
                        with(new MeshCollider()
                        );
            }
            SelectionEvents.selectEntity(physics);
        }
        else{
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

    @Override
    void exit() {
        saveInitial();
    }

}
