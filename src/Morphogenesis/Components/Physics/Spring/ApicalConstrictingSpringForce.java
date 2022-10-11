package Morphogenesis.Components.Physics.Spring;

import Framework.Timer.Time;
import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;


public class ApicalConstrictingSpringForce extends SpringForce {
    float rampTime = 5f;
    @Override
    public void awake() {
        Mesh mesh = parent.getComponent(Mesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof ApicalEdge) edges.add(edge);
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            if(Time.elapsedTime <  Time.asNanoseconds(rampTime)) {
                force  = calculateSpringForce(edge, constant * Time.elapsedTime / Time.asNanoseconds(rampTime));
            }
            else {
                force = calculateSpringForce(edge, constant);
            }
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
