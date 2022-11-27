package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.States.State;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.CircleMesh;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.CellGroups.GroupSelector;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Render.MeshRenderer;
import Utilities.Geometry.Vector.Vector2f;

import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;
import static Input.SelectionEvents.onSelectionButtonPressed;
@GroupSelector
public class Yolk extends Component {

    RingMesh referenceRing;
    Entity yolk;

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::rebuildYolk);
        onSelectionButtonPressed.subscribe(this::selectThis);
        referenceRing = getComponent(RingMesh.class);
        yolk = new Entity("Yolk").
                with(new CircleMesh().build(referenceRing.innerNodes, referenceRing.basalEdges)).
                with(new OsmosisForce());
        yolk.getComponent(MeshRenderer.class).enabled = false;
        yolk.getComponent(OsmosisForce.class).osmosisConstant = -0.0005f;
    }

    private void selectThis(Component component){
        if(component == this) {
            SelectionEvents.selectEntity(yolk);
        }
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
        onSelectionButtonPressed.unSubscribe(this::selectThis);
        yolk.destroy();
    }
}
