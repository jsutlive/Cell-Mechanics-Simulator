package Morphogenesis.Components.Physics.Collision;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Renderer.Graphics.Vector.CircleGraphic;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

import static Framework.States.State.addGraphicToScene;
import static Framework.States.State.removeGraphicFromScene;
import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;

import java.awt.*;
import java.util.List;

public class RigidBoundary extends Component {

    float outerRadius;
    RingMesh referenceRing;
    CircleGraphic graphic;

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::regenerateGraphic);
        referenceRing = getComponent(RingMesh.class);
        createGraphic();
    }

    private void createGraphic() {
        outerRadius = referenceRing.outerRadius;
        graphic = new CircleGraphic(new Vector2i(400), (int)((outerRadius * 2) + 2), Color.gray);
        addGraphicToScene(graphic);
    }

    private void regenerateGraphic(Mesh mesh){
        if(getComponent(Mesh.class) == mesh) {
            removeGraphicFromScene(graphic);
            createGraphic();
        }
    }

    @Override
    public void earlyUpdate() {
        checkNodesWithinBoundary(getComponent(Mesh.class).nodes);
    }

    private void checkNodesWithinBoundary(List<Node2D> allNodes) {
        for(Node2D node: allNodes) {
            if(!Boundary.ContainsNode(node, new Vector2f(400), outerRadius)) {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), outerRadius);
            }
        }
    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::regenerateGraphic);
        removeGraphicFromScene(graphic);
    }
}
