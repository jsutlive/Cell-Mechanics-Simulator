package Model.Components.Physics.Spring;

import Model.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Physics.Rigidbodies.Edges.Edge;

public class ElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Mesh.class).edges;
        targetLengthRatio = 1;
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            force = calculateSpringForce(edge, edge.getElasticConstant());
            addConstrictionForceToEdge(edge, force);
        }
    }
}
