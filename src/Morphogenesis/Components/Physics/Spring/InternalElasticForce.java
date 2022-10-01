package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Lattice.Lattice;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edges.Edge;

public class InternalElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Lattice.class).edgeList;
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
