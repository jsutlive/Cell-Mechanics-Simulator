package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.States.State;
import Morphogenesis.Components.Meshing.CircleMesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Render.MeshRenderer;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

public class Yolk extends Component {

    RingMesh referenceRing;
    Entity yolk;

    @Override
    public void awake() {
        referenceRing = getComponent(RingMesh.class);
        yolk = State.create(new Entity("Yolk").
                with(new CircleMesh().build(referenceRing.innerNodes, referenceRing.basalEdges)).
                with(new OsmosisForce()));
        yolk.getComponent(MeshRenderer.class).enabled = false;
        yolk.getComponent(OsmosisForce.class).osmosisConstant = -0.0005f;
    }

    public Entity checkSelection(Vector2f point){
        Entity selected = yolk.getComponent(CircleMesh.class).returnCellContainingPoint(point);
        if(selected == null)return parent;
        else return selected;

    }

    @Override
    public void onDestroy() {
        yolk.destroy();
    }
}
