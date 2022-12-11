package Morphogenesis;

import Framework.Object.Component;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Meshing.CircleMesh;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Meshing.RingMesh;
import Morphogenesis.Physics.CellGroups.GroupSelector;
import Morphogenesis.Physics.OsmosisForce;
import Morphogenesis.Render.MeshRenderer;
import Utilities.Geometry.Vector.Vector2f;

import static Morphogenesis.Meshing.Mesh.onMeshRebuilt;
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
        return selected;

    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::rebuildYolk);
        onSelectionButtonPressed.unSubscribe(this::selectThis);
        yolk.destroy();
    }
}
