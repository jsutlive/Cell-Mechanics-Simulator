package Model.Components.Physics.Spring;

import Model.Components.Meshing.CellMesh;
import Utilities.Physics.ForceType;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Edges.LateralEdge;
import Physics.Rigidbodies.Nodes.Node2D;

public class LateralShorteningSpringForce extends SpringForce {

    private float shorteningSpringConstant = 3f;
    private float shorteningSpringRatio = .7f;

    @Override
    public void update() {
        for(Edge edge: edges){
            Node2D[] nodes = edge.getNodes();
            calculateSpringForce(edge, shorteningSpringConstant);
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }


    @Override
    public void awake() {
        forceVector.setType(ForceType.lateralConstriction);
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof LateralEdge) edges.add(edge);
        setTargetLengthRatio(shorteningSpringRatio);
    }
}
