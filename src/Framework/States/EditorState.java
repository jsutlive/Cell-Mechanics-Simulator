package Framework.States;

import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Yolk;
import Renderer.Graphics.IRender;

import java.util.Objects;

import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    Entity model;
    @Override
    public void Init() {
        // Find an object to act as base physics/ setup for the simulation
        model = findObjectWithTag(MODEL);
        if(model == null){
            // create a new model with specific components
            model = State.create(new Entity("Physics System", 0, MODEL).
                    with(new RingMesh()).
                    with(new MouseSelector()).
                    with(new ApicalGradient()).
                    with(new LateralGradient()).
                    with(new CellRingCollider()).
                    with(new RigidBoundary()).
                    with(new Yolk())
                            );
        }
        else {
            //ensure mesh object is not null, then reset
            Objects.requireNonNull(model.getComponent(Mesh.class)).reset();
        }
        //loadModel();
    }

    @Override
    public void Tick() {
    }

    @Override
    public void Render() {
            for(IRender rend: renderBatch)
            {
                rend.render();
            }
    }

    @Override
    void OnChangeState() {
        saveInitial();
        SetState(new RunState());
    }

}
