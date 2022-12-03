package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edge;

public class ElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Mesh.class).edges;
        targetLengthRatio = 1;
        constant = 25f;
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            force = calculateSpringForce(edge, constant);
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
