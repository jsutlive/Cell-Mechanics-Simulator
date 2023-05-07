package component;

import framework.object.Entity;
import input.SelectionEvents;
import annotations.GroupSelector;
import utilities.geometry.Vector.Vector2f;

import static component.Mesh.onMeshRebuilt;
import static input.SelectionEvents.onSelectionButtonPressed;
@GroupSelector
public class Yolk extends Component {

    RingMesh referenceRing;
    Entity yolk;

    public float yolkOsmosisConstant = 0.0005f;

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::rebuildYolk);
        onSelectionButtonPressed.subscribe(this::selectThis);
        referenceRing = getComponent(RingMesh.class);
        yolk = new Entity("Yolk").
                with(new CircleMesh().build(referenceRing.innerNodes, referenceRing.basalEdges)).
                with(new OsmosisForce());
        yolk.getComponent(MeshRenderer.class).enabled = false;
    }

    private void selectThis(Component component){
        if(component == this) {
            SelectionEvents.selectEntity(yolk);
        }
    }

    @Override
    public void start() {
        yolk.getComponent(OsmosisForce.class).osmosisConstant = -yolkOsmosisConstant;
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
