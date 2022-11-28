package Morphogenesis.Components.Physics.Collision;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Renderer.Graphics.IRender;
import Renderer.Graphics.Painter;
import Renderer.Graphics.Vector.CircleGraphic;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector.Vector2f;

import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;


import java.awt.*;
import java.util.List;

public class RigidBoundary extends Component{

    float outerRadius;
    RingMesh referenceRing;
    CircleGraphic graphic;
    Vector2f center = new Vector2f(0);

    @Override
    public void awake() {
        onMeshRebuilt.subscribe(this::regenerateGraphic);
        referenceRing = getComponent(RingMesh.class);
        createGraphic();
    }

    private void createGraphic() {
        outerRadius = referenceRing.outerRadius;
        graphic = new CircleGraphic(center.asInt(), (int)((outerRadius * 2) + 2), Color.gray);
        graphic.add(graphic);
    }

    private void regenerateGraphic(Mesh mesh){
        if(getComponent(Mesh.class) == mesh) {
            graphic.add(graphic);
            createGraphic();
        }
    }

    @Override
    public void earlyUpdate() {
        checkNodesWithinBoundary(getComponent(Mesh.class).nodes);
    }

    private void checkNodesWithinBoundary(List<Node2D> allNodes) {
        for(Node2D node: allNodes) {
            if(!Boundary.ContainsNode(node, center, outerRadius)) {
                Boundary.clampNodeToBoundary(node, center, outerRadius);
            }
        }
    }

    @Override
    public void onDestroy() {
        onMeshRebuilt.unSubscribe(this::regenerateGraphic);
        graphic.remove(graphic);
    }

}
