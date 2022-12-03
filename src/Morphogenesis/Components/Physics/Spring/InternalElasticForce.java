package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Lattice.Lattice;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edge;

public class InternalElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Lattice.class).edgeList;
        targetLengthRatio = 1;
        constant = 0.5f;
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
