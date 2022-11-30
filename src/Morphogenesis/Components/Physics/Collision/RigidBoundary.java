package Morphogenesis.Components.Physics.Collision;

import Framework.Object.Component;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

import static Morphogenesis.Components.Meshing.Mesh.onMeshRebuilt;


import java.awt.*;
import java.util.List;

public class RigidBoundary extends Component{

    float outerRadius;
    RingMesh referenceRing;
    Vector2f center = new Vector2f(0);

    @Override
    public void awake() {
        referenceRing = getComponent(RingMesh.class);
        if(referenceRing == null) outerRadius = 320f;
        else outerRadius = referenceRing.outerRadius;
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
    }
}
