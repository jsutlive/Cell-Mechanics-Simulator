package component;

import utilities.geometry.Vector.Vector;
import framework.rigidbodies.Edge;

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
