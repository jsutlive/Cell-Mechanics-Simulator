package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.States.State;
import Morphogenesis.Components.Meshing.CircleMesh;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Render.MeshRenderer;
import Utilities.Geometry.Vector.Vector2f;

import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;

public class Yolk extends Component {

    RingMesh referenceRing;
    Entity yolk;

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::rebuildYolk);
        referenceRing = getComponent(RingMesh.class);
        yolk = State.create(new Entity("Yolk").
                with(new CircleMesh().build(referenceRing.innerNodes, referenceRing.basalEdges)).
                with(new OsmosisForce()));
        yolk.getComponent(MeshRenderer.class).enabled = false;
        yolk.getComponent(OsmosisForce.class).osmosisConstant = -0.0005f;
    }

    private void rebuildYolk(Mesh mesh){
        if (mesh == getComponent(Mesh.class)){
            yolk.getComponent(CircleMesh.class).build(referenceRing.innerNodes, referenceRing.basalEdges);
        }
    }

    public Entity checkSelection(Vector2f point){
        Entity selected = yolk.getComponent(CircleMesh.class).returnCellContainingPoint(point);
        if(selected == null)return parent;
        else return selected;

    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::rebuildYolk);
        yolk.destroy();
    }
}
