package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;


public class LateralShorteningSpringForce extends SpringForce {

    @Override
    public void awake() {
        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof LateralEdge) edges.add(edge);
        targetLengthRatio = 0.7f;
        constant = 3f;
    }
}
