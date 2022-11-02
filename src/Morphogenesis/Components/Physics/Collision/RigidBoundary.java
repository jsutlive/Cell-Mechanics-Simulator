package Morphogenesis.Components.Physics.Collision;

import Framework.Object.Component;
import Framework.States.State;
import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Renderer.Graphics.Vector.CircleGraphic;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.util.List;

public class RigidBoundary extends Component {

    float outerRadius = 300f;

    @Override
    public void awake() {
        State.addGraphicToScene(new CircleGraphic(new Vector2i(400), (int)((outerRadius * 2) + 2), Color.gray));
    }

    @Override
    public void earlyUpdate() {
        checkNodesWithinBoundary(getComponent(Mesh.class).nodes);
    }

    private void checkNodesWithinBoundary(List<Node2D> allNodes) {
        for(Node2D node: allNodes)
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), outerRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), outerRadius);
            }

        }
    }
}
