package Framework.States;

import Framework.Object.Entity;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Renderer.Graphics.IRender;

import java.util.ConcurrentModificationException;
import static Framework.Object.Tag.MODEL;

public class EditorState extends State
{
    Entity model;
    @Override
    public void Init() {
        State.reset();

        if(State.findObjectWithTag(MODEL) == null){
            model = State.create(new Entity("Physics System", 0, MODEL).
                    with(new RingMesh()).
                    with(new MouseSelector()).
                    with(new ApicalGradient()).
                    with(new LateralGradient()).
                    with(new CellRingCollider()).
                    with(new RigidBoundary())
                            );
        }
        //loadModel();
        for(Entity obj: allObjects){
            obj.start();
        }
    }

    @Override
    public void Tick() {
    }

    @Override
    public void Render() {
        try {
            for(IRender rend: renderBatch)
            {
                rend.render();
            }
        } catch (ConcurrentModificationException e){
        }
    }

    @Override
    void OnChangeState() {
        saveInitial();
        SetState(new RunState());
    }
}
