package Morphogenesis.Components.Physics.Spring;

import Framework.Timer.Time;
import Morphogenesis.Components.Meshing.RingCellMesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edge;

import static Framework.States.StateMachine.timer;


public class ApicalConstrictingSpringForce extends SpringForce {
    public float rampTime = 5f;

    @Override
    public void awake() {
        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        edges.add(mesh.edges.get(mesh.lateralResolution));
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            if(timer.elapsedTime <  Time.asNanoseconds(rampTime)) {
                force  = calculateSpringForce(edge, constant * timer.elapsedTime / Time.asNanoseconds(rampTime));
            }
            else {
                force = calculateSpringForce(edge, constant);
            }
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
