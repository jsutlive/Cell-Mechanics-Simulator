package Model.Components.Physics.Spring;

import Model.Components.Meshing.CellMesh;
import Utilities.Geometry.Vector.Vector;
import Utilities.Physics.ForceType;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Edges.LateralEdge;
import Physics.Rigidbodies.Nodes.Node2D;

public class LateralShorteningSpringForce extends SpringForce {

    @Override
    public void awake() {
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof LateralEdge) edges.add(edge);
        targetLengthRatio = 0.9f;
        constant = 3f;
    }
}
