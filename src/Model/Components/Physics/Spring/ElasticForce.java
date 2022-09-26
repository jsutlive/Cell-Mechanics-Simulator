package Model.Components.Physics.Spring;

import Model.Components.Meshing.Mesh;
import Utilities.Physics.ForceType;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;
public class ElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Mesh.class).edges;
        targetLengthRatio = 1;
        forceVector.setType(ForceType.elastic);
    }

    @Override
    public void update() {
        for(Edge edge: edges){
            Node2D[] nodes = edge.getNodes();
            calculateSpringForce(edge, edge.getElasticConstant());
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }
}
