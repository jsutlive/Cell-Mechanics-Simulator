package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.CellMesh;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;


public class LateralShorteningSpringForce extends SpringForce {

    @Override
    public void awake() {
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof LateralEdge) edges.add(edge);
        targetLengthRatio = 0.9f;
        constant = 3f;
    }
}
