package Model.Components.Physics;

import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

public class InternalElasticForce extends SpringForce{

    @Override
    public void awake() {
        edges = getComponent(Lattice.class).edgeList;
        targetLengthRatio = 1;
        forceVector.setType(ForceType.elastic);
    }

    @Override
    public void update() {
        for(Edge edge: edges){
            Node[] nodes = edge.getNodes();
            calculateSpringForce(edge, edge.getElasticConstant());
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }
}
