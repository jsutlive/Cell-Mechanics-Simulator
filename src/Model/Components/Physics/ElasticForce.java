package Model.Components.Physics;

import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

public class ElasticForce extends SpringForce{

    @Override
    public void setup() {
        CellMesh mesh = (CellMesh) parent.getComponent(CellMesh.class);
        edges = mesh.edges;
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
