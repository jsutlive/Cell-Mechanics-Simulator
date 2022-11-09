package Morphogenesis.Components.Physics.Spring;

import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Physics.Force;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edges.Edge;

public class ElasticForce extends SpringForce {

    @Override
    public void awake() {
        edges = getComponent(Mesh.class).edges;
        targetLengthRatio = 1;
        constant = 15f;
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
