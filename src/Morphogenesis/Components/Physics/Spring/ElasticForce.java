package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edges.Edge;

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
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
