package Model.Components.Physics.Spring;

import Model.Components.Meshing.CellMesh;
import Utilities.Physics.ForceType;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
public class ElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(CellMesh.class).edges;
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
